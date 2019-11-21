package com.qzn.crowdfunding.manager.controller;

import com.qzn.crowdfunding.bean.Cert;
import com.qzn.crowdfunding.manager.service.CertService;
import com.qzn.crowdfunding.manager.service.CerttypeService;
import com.qzn.crowdfunding.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/11/15 14:48
 */
@Controller
@RequestMapping("/certtype")
public class CerttypeController {

    @Autowired
    private CerttypeService certtypeService;

    @Autowired
    private CertService certService;

    @RequestMapping("/index")
    public String index(Map map) {

        List<Cert> queryAllCert = certService.queryAll();

        map.put("allCert",queryAllCert);

        //查询资质与账户类型之间的关系
        List<Map<String, Object>> certAccttypeList = certtypeService.queryCertAccttype();

        map.put("certAccttypeList",certAccttypeList);

        return "certtype/index";
    }

    @ResponseBody
    @RequestMapping("/insertAcctTypeCert")
    public Object insertAcctTypeCert( Integer certid, String accttype ) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("certid", certid);
            paramMap.put("accttype", accttype);

            int count = certtypeService.insertAcctTypeCert(paramMap);
            result.setSuccess(count==1);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("/deleteAcctTypeCert")
    public Object deleteAcctTypeCert( Integer certid, String accttype ) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("certid", certid);
            paramMap.put("accttype", accttype);

            int count = certtypeService.deleteAcctTypeCert(paramMap);
            result.setSuccess(count==1);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;

    }


}
