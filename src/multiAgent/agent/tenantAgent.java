package multiAgent.agent;

import DO.landlord;
import DO.tenant;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import multiAgent.agentHelper.ValueCal;
import multiAgent.behavior.listener.tenantApiListener;
import multiAgent.behavior.listener.tenantListener;
import multiAgent.behavior.logical.tenantBackOrderResult;
import multiAgent.ontology.Bid;
import multiAgent.ontology.BidOntology;
import multiAgent.ontology.Order;
import service.common.agentHandler;
import util.CondVar;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by h77 on 2017/5/5.
 * 简单的房客Agent
 */
public class tenantAgent extends Agent {


    private Codec codec = new SLCodec();
    private Ontology ontology = BidOntology.getInstance();
    private tenant owner = null;
    //tenantAgent 生命周期
    private boolean isDone = false;
    private Map<Integer,Order> tenantTOorder;
    private LinkedBlockingQueue<Bid> queues;

    protected void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        setEnabledO2ACommunication(true,10);
        Object[] args = getArguments();
        tenantTOorder = new HashMap<Integer, Order>();
        if (args.length > 0) {
            CondVar latch = (CondVar) args[0];
            owner = (tenant) args[1];
            queues = (LinkedBlockingQueue<Bid>)args[2];
            latch.signal();
        }
        System.out.println("创建 tenantAgent");
        ValueCal cal = new ValueCal();
//        cal.TrainrandomForest(this.getOwner().getId());
        addBehaviour(new tenantListener(this,cal));
        addBehaviour(new tenantApiListener(this));
        addBehaviour(new tenantBackOrderResult(null,null));
    }

//    public boolean done(){
//        //结束生命周期
//        return true;
//    }

    public tenant getOwner(){
        return owner;
    }
    public void setOrder(Integer id,Order order){ tenantTOorder.put(id,order);}
    public Order getOrder(Integer id){return tenantTOorder.get(id);}
    public void takeDown(){
        System.out.println("tenantAgent 被销毁");
        setEnabledO2ACommunication(false,0);
    }
    public void putResult(Bid bid){
        try {
            this.queues.put(bid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
