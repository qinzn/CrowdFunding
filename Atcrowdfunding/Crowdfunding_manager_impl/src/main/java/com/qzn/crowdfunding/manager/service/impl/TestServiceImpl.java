package com.qzn.crowdfunding.manager.service.impl;

import com.qzn.crowdfunding.manager.dao.TestDao;
import com.qzn.crowdfunding.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/10/22 11:16
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    public void insert() {
        Map map = new HashMap();
        map.put("name", "zhangsan");
        testDao.insert(map);

    }
}
