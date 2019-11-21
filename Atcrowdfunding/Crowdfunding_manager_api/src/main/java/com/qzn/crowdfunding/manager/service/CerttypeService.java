package com.qzn.crowdfunding.manager.service;

import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/11/15 14:49
 */
public interface CerttypeService {
    List<Map<String, Object>> queryCertAccttype();

    int insertAcctTypeCert(Map<String, Object> paramMap);

    int deleteAcctTypeCert(Map<String, Object> paramMap);
}
