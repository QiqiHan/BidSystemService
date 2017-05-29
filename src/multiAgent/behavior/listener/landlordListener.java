package multiAgent.behavior.listener;

import DO.landlord;
import DO.room;
import dao.daoImpl.roomDao;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import multiAgent.agent.landlordAgent;
import multiAgent.behavior.logical.landlordDealTender;
import multiAgent.ontology.*;
import util.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * Created by H77 on 2017/5/6.
 *
 */
public class landlordListener extends CyclicBehaviour {

    private Codec codec = new SLCodec();
    private Ontology ontology = BidOntology.getInstance();

    public landlordListener(Agent agent){
        super(agent);
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchLanguage(codec.getName()),
                MessageTemplate.MatchOntology(ontology.getName()));
        ACLMessage msg = myAgent.receive(mt);

        if(msg != null){
            if(msg.getPerformative() == ACLMessage.PROPOSE){
                ContentElement ce = null;
                try {
                    ce = myAgent.getContentManager().extractContent(msg);
                    Action act = (Action) ce;
                    Tender tender = (Tender) act.getAction();
                    Order order = tender.getOrder();
                    System.out.println("landlord" + myAgent.getName() + "收到信息地址" + order.getAddress() + " 价格:" + order.getMinPrice()+"—"+order.getMaxPrice());
                    myAgent.addBehaviour(new landlordDealTender(myAgent,tender,msg.getSender()));
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

            }else if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                //监听客户端协商消息
                try {
                    ContentElement ce = myAgent.getContentManager().extractContent(msg);
                    System.out.println(myAgent.getName()+" 收到房客的协商消息");
                    Action act = (Action) ce;
                    if(act.getAction() instanceof Negotiation){
                        Negotiation negotiation = (Negotiation)act.getAction();

                        Map<String,Order> map = ((landlordAgent)myAgent).getOrderToNegotiate();
                        Order order = map.get(negotiation.getId());
                        Date start = order.getStartTime();
                        Date end = order.getEndTime();

                        landlord lord = ((landlordAgent)myAgent).getOwner();
                        String economy = lord.getCharacteristic(); //房东经济情况

                        room room = roomDao.findRoomByLandlordAndType(lord.getLandlordid(),order.getRoomType());
                        int init_price = room.getPrice();  //该房间的标价
                        int current_price = negotiation.getActualPrice();  //此次协商前的房间价格
                        int max = negotiation.getMaxReduction();  //房客希望的最高降价幅度
                        int min = negotiation.getMinReduction();  //房客希望的最低降价幅度

                        if(DateUtil.isHoliday(start) || DateUtil.isHoliday(end)){
                            //节假日不接受降价
                            negotiation.setResult(0);
                        }else{

                            if(economy.equals("Tension")){
                                //不接受降价
                                negotiation.setResult(0);
                            }else if(economy.equals("Affordable")){
                                if((current_price*1.0/init_price) > 0.95){
                                    //接收降价，降价额为最低降价幅度
                                    negotiation.setResult(1);
                                    negotiation.setActualPrice(current_price-min);
                                }else{
                                    //不接受降价
                                    negotiation.setResult(0);
                                }
                            }else if(economy.equals("Amiable")){
                                if((current_price*1.0/init_price) > 0.95){
                                    //接收降价，降价额为最高降价幅度
                                    negotiation.setResult(1);
                                    negotiation.setActualPrice(current_price-max);
                                }else if((current_price*1.0/init_price) > 0.90){
                                    //接收降价，降价额为最低降价幅度
                                    negotiation.setResult(1);
                                    negotiation.setActualPrice(current_price-min);
                                }else{
                                    //不接受降价
                                    negotiation.setResult(0);
                                }
                            }else if(economy.equals("Promotion")){
                                if((current_price*1.0/init_price) > 0.9){
                                    //接收降价，降价额为最高降价幅度
                                    negotiation.setResult(1);
                                    negotiation.setActualPrice(current_price-max);
                                }else if((current_price*1.0/init_price) > 0.8){
                                    //接收降价，降价额为最低降价幅度
                                    negotiation.setResult(1);
                                    negotiation.setActualPrice(current_price-min);
                                }else{
                                    //不接受降价
                                    negotiation.setResult(0);
                                }
                            }
                        }

                        //回复给房客
                        Action sendAct = new Action();
                        sendAct.setActor(myAgent.getAID());
                        sendAct.setAction(negotiation);

                        ACLMessage message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        message.addReceiver(msg.getSender());
                        message.setLanguage(codec.getName());
                        message.setOntology(ontology.getName());

                        myAgent.getContentManager().fillContent(message, sendAct);
                        //发消息
                        myAgent.send(message);

                    }
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }
            }
            /*
               可以扩展监听其它类型的消息
             */
        }else{
            block();
        }

    }
}
