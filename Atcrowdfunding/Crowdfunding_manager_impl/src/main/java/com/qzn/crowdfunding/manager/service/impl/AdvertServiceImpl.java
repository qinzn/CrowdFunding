package com.qzn.crowdfunding.manager.service.impl;

import com.qzn.crowdfunding.bean.Advertisement;
import com.qzn.crowdfunding.manager.service.AdvertService;
import com.qzn.crowdfunding.util.Page;
import com.qzn.crowdfunding.vo.Data;
import com.qzn.crowdfunding.manager.dao.AdvertisementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdvertServiceImpl implements AdvertService {

	@Autowired
	private AdvertisementMapper advertDao;

	public Advertisement queryAdvert(Map<String, Object> advertMap) {
		return advertDao.queryAdvert(advertMap);
	}

	public Page pageQuery(Map<String, Object> paramMap) {
		Page advertPage = new Page((Integer)paramMap.get("pageno"),(Integer)paramMap.get("pagesize"));
		paramMap.put("startIndex", advertPage.getStartIndex());
		List<Advertisement> advertList= advertDao.pageQuery(paramMap);
		// 获取数据的总条数
		int count = advertDao.queryCount(paramMap);
		
		advertPage.setDatas(advertList);
		advertPage.setTotalsize(count);
		return advertPage;
	}

	public int queryCount(Map<String, Object> advertMap) {
		return advertDao.queryCount(advertMap);
	}

	public int insertAdvert(Advertisement advert) {
		return advertDao.insertAdvert(advert);
	}

	public Advertisement queryById(Integer id) {
		return advertDao.queryById(id);
	}

	public int updateAdvert(Advertisement advert) {
		return advertDao.updateAdvert(advert);
	}

	public int deleteAdvert(Integer id) {
		return advertDao.deleteAdvert(id);
	}

	public int deleteAdverts(Data ds) {
		return advertDao.deleteAdverts(ds);
	}

}
