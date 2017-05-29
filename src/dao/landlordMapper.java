package dao;

import DO.landlord;

import java.util.List;

public interface landlordMapper {
    int deleteByPrimaryKey(Integer landlordid);

    int insert(landlord record);

    int insertSelective(landlord record);

    landlord selectByPrimaryKey(Integer landlordid);

    List<landlord> selectAllLandlord();

    int updateByPrimaryKeySelective(landlord record);

    int updateByPrimaryKey(landlord record);
}