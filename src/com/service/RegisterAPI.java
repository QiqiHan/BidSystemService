package com.service;

import VO.TenantInfo;
import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by zcy on 2017/6/22.
 *
 */
@Path("/register")
public class RegisterAPI {
    @Path("/tenant")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String tenantRegister(String json){
        TenantInfo tenantInfo = JSONObject.parseObject(json,TenantInfo.class);

        return null;
    }

    @Path("/landlord")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String landlordRegister(String json){
        return null;
    }
}
