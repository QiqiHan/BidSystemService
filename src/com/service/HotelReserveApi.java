package com.service;

import VO.OrderInfo;
import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by zcy on 2017/6/6.
 *
 */
public class HotelReserveApi {
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String getReserveInfo(String json){
        OrderInfo orderInfo = JSONObject.parseObject(json,OrderInfo.class);
        return null;
    }
}
