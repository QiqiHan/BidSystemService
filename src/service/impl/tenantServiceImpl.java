package service.impl;

import DO.landlord;
import DO.tenant;
import VO.BidInfo;
import VO.OrderInfo;
import dao.daoImpl.tenantDao;
import dao.tenantMapper;
import dao.daoImpl.landlordDao;
import jade.core.AID;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import multiAgent.ontology.Bid;
import multiAgent.ontology.Order;
import multiAgent.ontology.Room;
import org.apache.ibatis.session.SqlSession;
import service.common.agentHandler;
import service.tenantService;
import util.CondVar;
import util.DBTools;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by H77 on 2017/5/15.
 */
public class tenantServiceImpl implements tenantService {

    private static tenantService tenantImpl = new tenantServiceImpl();
    private tenantServiceImpl(){
    }
    public static tenantService getInstance(){return tenantImpl;}
    public boolean createTenant(tenant user) {
        SqlSession sqlSession = DBTools.getSession();
        tenantMapper mapper = sqlSession.getMapper(dao.tenantMapper.class);
        mapper.insert(user);
        sqlSession.commit();
        return true;
    }

    public tenant findTenant(int tenantId) {
        SqlSession sqlSession = DBTools.getSession();
        tenantMapper mapper = sqlSession.getMapper(dao.tenantMapper.class);
        tenant user = mapper.selectByPrimaryKey(tenantId);
        return user;
    }
    public void createAgent(int tenantId) {
        tenant user = this.findTenant(tenantId);
        String name = user.getName();
        AgentContainer container = agentHandler.containers.get("main");
        Queue<BidInfo> bidQueue = new LinkedBlockingQueue<BidInfo>();
        try {
            CondVar startUpLatch = new CondVar();
            AgentController tenantAgent = container.createNewAgent(name,"multiAgent.agent.tenantAgent",new Object[] { startUpLatch ,user ,bidQueue});
            tenantAgent.start();
            try {
                startUpLatch.waitOn();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            agentHandler.agents.put(name,tenantAgent);
            agentHandler.aids.put(name,new AID(name,false));
            agentHandler.queues.put(name,bidQueue);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
    public void closeAgent(int tenantId) {
        tenant user = this.findTenant(tenantId);
        String name = user.getName();
        AgentController tenantAgent = agentHandler.agents.get(name);
        try {
            tenantAgent.kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        agentHandler.agents.remove(name);
        agentHandler.aids.remove(name);
        agentHandler.queues.remove(name);
    }

    public List<BidInfo> Order(OrderInfo o) {
        OrderInfo orderInfo = o;
        tenant user = tenantDao.getTenant(orderInfo.getUserId());
        String name = user.getName();

        jade.util.leap.List facilities = new  jade.util.leap.ArrayList();
        List<String> facilies = o.getFacilities();
        for(int i = 0 ; i < facilies.size() ; i++){
            facilities.add(facilies.get(i));
        }
        Order order = new Order(o.getUserId()+"",
                user.getName(),
                orderInfo.getAddress(),
                orderInfo.getHotelType(),
                orderInfo.getRoomType(),
                orderInfo.getRoomNum(),
                orderInfo.getStartTime(),
                orderInfo.getEndTime(),
                new Date(),
                orderInfo.getMinPrice(),
                orderInfo.getMaxPrice(),
                facilities,
                new AID(user.getName(),false));
        AgentController tenantAgent = agentHandler.agents.get(name);
        List<BidInfo> bids = new ArrayList<BidInfo>();
        try {
            tenantAgent.putO2AObject(order,false);
            LinkedBlockingQueue<List<BidInfo>> queues = (LinkedBlockingQueue<List<BidInfo>>) agentHandler.queues.get(name);
            bids = queues.take();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bids;
    }


}
