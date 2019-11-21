package com.qzn.crowdfunding.manager.service;

import com.qzn.crowdfunding.bean.Advertisement;
import com.qzn.crowdfunding.util.Page;
import com.qzn.crowdfunding.vo.Data;

import java.util.Map;

public interface AdvertService {
	Advertisement queryAdvert(Map<String, Object> advertMap);

	Page pageQuery(Map<String, Object> advertMap);

	int queryCount(Map<String, Object> advertMap);

	int insertAdvert(Advertisement advert);

	Advertisement queryById(Integer id);

	int updateAdvert(Advertisement advert);

	int deleteAdvert(Integer id);

	int deleteAdverts(Data ds);
}
