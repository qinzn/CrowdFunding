package com.qzn.crowdfunding.manager.dao;

import com.qzn.crowdfunding.bean.Advertisement;
import com.qzn.crowdfunding.vo.Data;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AdvertisementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advertisement record);

    Advertisement selectByPrimaryKey(Integer id);

    List<Advertisement> selectAll();

    int updateByPrimaryKey(Advertisement record);

    Advertisement queryAdvert(Map<String, Object> advertMap);

    List<Advertisement> pageQuery(Map<String, Object> advertMap);

    int queryCount(Map<String, Object> advertMap);

    int insertAdvert(Advertisement advert);

    Advertisement queryById(Integer id);

    int updateAdvert(Advertisement advert);

    int deleteAdvert(Integer id);

    int deleteAdverts(Data ds);
}