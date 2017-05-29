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
        Queue<Bid> bidQueue = new LinkedBlockingQueue<Bid>();
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

    public BidInfo Order(OrderInfo o) {
        OrderInfo orderInfo = o;
        tenant user = tenantDao.getTenant(orderInfo.getUserId());
        jade.util.leap.List facilities = new  jade.util.leap.ArrayList();
        List<String> facilitys = o.getFacilities();
        for(int i = 0 ; i < facilitys.size() ; i++){
            facilities.add(i,facilitys.get(i));
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
        AgentController tenantAgent = agentHandler.agents.get(user.getName());
        Bid bid = null;
        try {
            tenantAgent.putO2AObject(order,false);
            LinkedBlockingQueue<Bid> queues = (LinkedBlockingQueue<Bid>) agentHandler.queues.get(user.getName());
            bid = queues.take();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Room r = bid.getRoom();
        landlord l =  landlordDao.findlandlordByid(r.getLandlordId());
        List<String> bid_facilitys = new ArrayList<String>();
        for(int i = 0 ; i <bid.getFacilities().size() ; i++){
            bid_facilitys.add((String)bid.getFacilities().get(i));
        }
        BidInfo info = new BidInfo(l.getLandlordtype(),l.getLandlordtype(),r.getType(),bid.getPrice()+"",r.getPrice()+"",bid.getNum(),bid_facilitys);
        return info;
    }


}
