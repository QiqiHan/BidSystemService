package service;

import DO.orderRecord;
import DO.tenant;
import DO.user;
import VO.BidInfo;
import VO.OrderInfo;
import multiAgent.ontology.Bid;
import multiAgent.ontology.Order;

import java.util.List;

/**
 * Created by H77 on 2017/5/15.
 */
public interface tenantService {
    public boolean createTenant(tenant user);
    public tenant findTenant(int tenantId);
    public void createAgent(int tenantId);
    public void closeAgent(int tenantId);
    public List<BidInfo> Order(String name, OrderInfo o);

}
