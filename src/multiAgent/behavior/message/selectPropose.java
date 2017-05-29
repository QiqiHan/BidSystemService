package multiAgent.behavior.message;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import multiAgent.agent.selectAgent;
import multiAgent.ontology.*;

import jade.util.leap.ArrayList;
import java.util.List;

/**
 * Created by H77 on 2017/5/6.
 */
public class selectPropose extends OneShotBehaviour{

    private Codec codec = new SLCodec();
    private Ontology ontology = BidOntology.getInstance();
    private Tender tender = null;
    private List<AID> aids = null;
    private selectAgent agent;

    public selectPropose (Agent agent, Tender tender ,List<AID> aids){
        super(agent);
        this.agent = (selectAgent) agent;
        this.tender = tender;
        this.aids = aids;
    }
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        Action act = new Action();
        act.setActor(myAgent.getAID());
        act.setAction(tender);
        if(aids.size() == 0) {  System.out.println("selectPropose 异常");  return;  }
        for(AID aid : aids){
            msg.addReceiver(aid);
        }

        msg.setLanguage(codec.getName());
        msg.setOntology(ontology.getName());
        try {
            myAgent.getContentManager().fillContent(msg,act);
            myAgent.send(msg);
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }


}
