package dao;

import DO.tenant;

public interface tenantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(tenant record);

    int insertSelective(tenant record);

    tenant selectByPrimaryKey(Integer id);

    tenant selectByNameAndPassword(String tenantname, String password);

    int updateByPrimaryKeySelective(tenant record);

    int updateByPrimaryKey(tenant record);
}