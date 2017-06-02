package multiAgent.behavior.logical;

import DO.landlord;
import DO.room;
import dao.daoImpl.landlordDao;
import dao.daoImpl.roomDao;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import multiAgent.agent.landlordAgent;
import multiAgent.behavior.message.landlordPropose;
import multiAgent.ontology.*;
import util.DateUtil;

import java.util.Date;

/**
 * Created by H77 on 2017/5/8.
 * 这个Behaviour主要是处理招标书，并生成相应结果。
 */
public class landlordDealTender extends OneShotBehaviour{

    private Tender tender;
    private AID receive;
    private landlordAgent agent;


    public landlordDealTender(Agent agent , Tender tender, AID receive){
        super(agent);
        this.agent = (landlordAgent) agent;
        this.tender = tender;
        this.receive = receive;
    }
    public void action() {
        //决定是否要竞标
        //首先查看该房间类型是否有，如果有房间数量是否满足。如果没有该类型房间或者房间数量不满足，则放弃竞标
        //其次查看招标价格与该房间价格的差值，如果招标价格高于等于房间价格，直接参与竞标
        //如果招标价格低于房间价格，如果差值大于某个阀值，则放弃竞标
        //阀值由该房东的经济情况确定。
        //比如tension的threshold = 0
        //affordable的threshold = 10
        //amiable的threshold = 20
        //promotion的threshold = 40

        int type; //如果type是1代表要竞标，如果是0表示不竞标
        landlord landlord = agent.getOwner();  //该agent代表的房东
        String characteristic = landlord.getCharacteristic();  //该房东的经济情况
        Order order = tender.getOrder();
        int price_min_tender = order.getMinPrice();
        int price_max_tender = order.getMaxPrice();
        String roomType = order.getRoomType();
        int roomNum = order.getRoomNum();

        room r = roomDao.findRoomByLandlordAndType(landlord.getLandlordid(),roomType);
        String[] facility = landlord.getFeature().split(",");
        List facilitys = new ArrayList();
        for(int i = 0 ; i < facility.length ; i++){
            facilitys.add(i,facility[i]);
        }

        agent.getOrderToNegotiate().put(order.getId(),order);

//        landlord land = landlordDao.findlandlordByid(landlord.)
        if(r==null){
            //该房东没有该类型的房间,拒绝竞标
            type = 0;
        }else if(r.getRestnum()<roomNum){
            //该房东该类型的房间数量不满足用户需求，拒绝竞标
            type = 0;
        }else{
            int price_room = r.getPrice();  //该房东该类型房间的价格
            if(price_room<price_min_tender){
                //招标价格最小值高于房间价格，参与竞标
                type = 1;
            }else if(price_room>price_max_tender){
                //招标价格最大值低于房间价格，不参与竞标
                type = 0;
            }else{
                //房间价格在招标价格区间内
                int threshold = 0;
                if(characteristic.equals("tension")){
                    threshold = 0;
                }else if(characteristic.equals("affordable")){
                    threshold = 10;
                }else if(characteristic.equals("amiable")){
                    threshold = 20;
                }else if(characteristic.equals("promotion")){
                    threshold = 40;
                }

                if(price_room+threshold>price_max_tender){
                    type = 0;
                }else{
                    //价格符合了要求，判断订单的时间（是否是节假日）和空房率
                    Date startTime = order.getStartTime();
                    Date endTime = order.getEndTime();
                    double vacant = 0;
                    if(DateUtil.isHoliday(startTime)){
                        if(DateUtil.isHoliday(endTime)){
                            //起止时间都是节假日，只有当空房率高于50%时才接受招标
                            if(vacant>0.5){
                                type = 1;
                            }else{
                                type = 0;
                            }
                        }else{
                            //开始时间是节假日，结束时间不是节假日，当空房率高于30%时接受招标
                            if(vacant>0.3){
                                type = 1;
                            }else{
                                type = 0;
                            }

                        }
                    }else{
                        type = 1;
                    }
                }
            }
        }

        Bid bid = null;
        if(type == 1) {
            //决定竞价
            bid = new Bid(order.getId(),
                    new Room(r.getRoomid(),agent.getOwner().getLandlordid(), RoomType.Business+"",agent.getAID(),r.getPrice(),r.getValidstarttime(),r.getValidendtime(),r.getPrice()+"",2),
                    r.getPrice(),
                    facilitys,
                    null,
                    myAgent.getAID(),
                    order.getSource(),
                    type);
        }else{
            //拒绝竞价
            bid = new Bid(order.getId(),
                    new Room(1,1, RoomType.Business+"",agent.getAID(),200,new Date(2017,5,2),new Date(2017,5,9),"200",2),
                    0,
                    null,
                    null,
                    myAgent.getAID(),
                    order.getSource(),
                    type);
        }
        myAgent.addBehaviour(new landlordPropose(myAgent,bid,receive));
    }

}
