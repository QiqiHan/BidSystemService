package dao.daoImpl;

import DO.landlord;
import DO.tenant;
import DO.user;
import dao.landlordMapper;
import dao.tenantMapper;
import org.apache.ibatis.session.SqlSession;
import util.DBTools;

/**
 * Created by zcy on 2017/6/4.
 * 根据登录信息得到房东或房客的个人信息
 */
public class UserDao {
    public static user findUser(String name, String password, String type){
        if(type.equals("landlord")){
            SqlSession sqlSession = DBTools.getSession();
            landlordMapper landlordMapper = sqlSession.getMapper(dao.landlordMapper.class);
            landlord landlord = landlordMapper.selectByNameAndPassword(name,password);
            return landlord;
        }else if(type.equals("tenant")){
            SqlSession sqlSession = DBTools.getSession();
            tenantMapper tenantMapper = sqlSession.getMapper(dao.tenantMapper.class);
            tenant tenant =  tenantMapper.selectByNameAndPassword(name,password);
            return tenant;
        }else{
            return null;
        }
    }
}
