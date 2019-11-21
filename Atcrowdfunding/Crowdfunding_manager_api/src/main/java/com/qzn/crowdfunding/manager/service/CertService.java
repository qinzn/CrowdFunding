package com.qzn.crowdfunding.manager.service;

import com.qzn.crowdfunding.bean.Cert;
import com.qzn.crowdfunding.bean.MemberCert;

import java.util.List;

/**
 * @author qzn
 * @create 2019/11/15 14:59
 */
public interface CertService {
    List<Cert> queryAll();

    List<Cert> queryCertByAccttype(String accttype);

    void saveMemberCert(List<MemberCert> certimgs);
}
