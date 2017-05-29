package util;

import dao.landlordMapper;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by zy on 17/5/15.
 */
public class DBTest {

    //调用数据库示例
    public static void main(String[] args){
        SqlSession sqlSession = DBTools.getSession();
        landlordMapper landlordMapper = sqlSession.getMapper(dao.landlordMapper.class);
        landlordMapper.selectByPrimaryKey(1);
    }
}
