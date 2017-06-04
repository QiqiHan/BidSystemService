package service.impl;

import DO.landlord;
import DO.tenant;
import DO.user;
import VO.LandLordInfo;
import VO.TenantInfo;
import VO.UserInfo;
import dao.daoImpl.UserDao;
import service.LoginService;


/**
 * Created by zcy on 2017/6/4.
 *
 */
public class LoginServiceImpl implements LoginService{

    private static LoginService login = new LoginServiceImpl();

    private LoginServiceImpl(){}

    public static LoginService getInstance(){
        return login;
    }

    public UserInfo login(String id, String pwd, String type) {
        user u = UserDao.findUser(id,pwd,type);
        if(u instanceof landlord){
            return new LandLordInfo();
        }else if(u instanceof tenant){
            return new TenantInfo();
        }else{
            return null;
        }
    }
}
