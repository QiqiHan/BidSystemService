package multiAgent.agent;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.*;
import jade.core.Agent;
import multiAgent.behavior.listener.consultListener;
import multiAgent.ontology.*;
import multiAgent.agentHelper.DFUtil;

/**
 * Created by h77 on 2017/5/5.
 * 简单的协商Agent
 */
public class consultAgent extends Agent {

    private Codec codec = new SLCodec();
    private Ontology ontology = BidOntology.getInstance();

    protected void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        DFUtil.registerService(this,"consult");
        System.out.println("创建 consultAgent");
        addBehaviour(new consultListener(this));
    }
}