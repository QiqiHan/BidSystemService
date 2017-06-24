package service.impl;

import DO.tenant;
import VO.TenantInfo;
import VO.UserInfo;
import dao.daoImpl.tenantDao;
import service.RegisterService;

/**
 * Created by zcy on 2017/6/24.
 *
 */
public class RegisterServiceImpl implements RegisterService {

    private static RegisterService registerService = new RegisterServiceImpl();

    private RegisterServiceImpl(){}

    public static RegisterService getInstance(){
        return registerService;
    }

    public String register(UserInfo userInfo) {

        tenant tenant = new tenant(((TenantInfo)userInfo).getId(),
                ((TenantInfo)userInfo).getName(),
                ((TenantInfo)userInfo).getPassword(),
                ((TenantInfo)userInfo).getGender(),
                ((TenantInfo)userInfo).getPhonenum(),
                ((TenantInfo)userInfo).getPreference(),
                ((TenantInfo)userInfo).getEducation(),
                ((TenantInfo)userInfo).getVocation(),
                ((TenantInfo)userInfo).getEconomic());
        if(tenantDao.registerTenant(tenant)){
            return "Register Success!";
        }
        return "Register Fail!";
    }
}
