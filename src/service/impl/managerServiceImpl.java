package service.impl;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import service.common.agentHandler;
import service.managerService;

/**
 * Created by H77 on 2017/5/15.
 */
public class managerServiceImpl implements managerService {

    private static managerService manager = new managerServiceImpl();
    private managerServiceImpl(){};
    public static managerService getInstance(){return manager;}
    public void initSystem() {
        Runtime rt = Runtime.instance();
        rt.setCloseVM(true);
        Profile pMain = new ProfileImpl("127.0.0.1",1099,null);
        AgentContainer container = rt.createMainContainer(pMain);
        agentHandler.containers.put("main",container);
        System.out.println("系统初始化");
        System.out.println("---------------------");
        System.out.println("初始化ManagerAgent");
        try {
            AgentController consultAgent = container.createNewAgent("f1","multiAgent.agent.consultAgent",null);
            consultAgent.start();
            agentHandler.agents.put(consultAgent.getName(),consultAgent);
            AgentController selectAgent = container.createNewAgent("f3","multiAgent.agent.selectAgent",null);
            selectAgent.start();
            agentHandler.agents.put(selectAgent.getName(),selectAgent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
