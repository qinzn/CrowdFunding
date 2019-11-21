package com.qzn.crowdfunding.manager.service.impl;

import com.qzn.crowdfunding.bean.Cert;
import com.qzn.crowdfunding.bean.MemberCert;
import com.qzn.crowdfunding.manager.dao.CertMapper;
import com.qzn.crowdfunding.manager.service.CertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qzn
 * @create 2019/11/15 14:59
 */
@Service
public class CertServiceImpl implements CertService {

    @Autowired
    private CertMapper certMapper;

    @Override
    public List<Cert> queryAll() {
        return certMapper.selectAll();
    }

    @Override
    public List<Cert> queryCertByAccttype(String accttype) {
        return certMapper.queryCertByAccttype(accttype);
    }

    @Override
    public void saveMemberCert(List<MemberCert> certimgs) {
        for (MemberCert certimg : certimgs) {
            certMapper.insertMemberCert(certimg);
        }
    }
}
