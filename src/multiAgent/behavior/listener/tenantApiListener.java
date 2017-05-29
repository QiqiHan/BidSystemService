package multiAgent.behavior.listener;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import multiAgent.agent.tenantAgent;
import multiAgent.behavior.message.tenantRequest;
import multiAgent.ontology.Order;

/**
 * Created by H77 on 2017/5/13.
 */
public class tenantApiListener extends CyclicBehaviour {

    tenantAgent agent = null;
    public tenantApiListener(Agent agent){
        super(agent);
        this.agent = (tenantAgent) agent;
    }
    public void action() {
        Object obj = agent.getO2AObject();
        if(obj != null) {
            if (obj instanceof Order) {
                Order o = (Order) obj;
                agent.addBehaviour(new tenantRequest(agent, o));
                agent.setOrder(agent.getOwner().getId(),o);
            }
        }else{
            block();
        }
    }
}
