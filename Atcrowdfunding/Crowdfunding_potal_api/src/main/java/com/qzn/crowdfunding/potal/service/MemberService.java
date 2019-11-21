package com.qzn.crowdfunding.potal.service;

import com.qzn.crowdfunding.bean.Member;

import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/11/13 15:51
 */
public interface MemberService {

    Member queryMemberLogin(Map<String, Object> paramMap);

    void updateAcctType(Member member);

    void updateBasicinfo(Member member);

    void updateEmail(Member member);

    void updateAuthstatus(Member loginMember);

    Member getMemberById(Integer memberid);

    List<Map<String, Object>> queryCertByMemberid(Integer memberid);
}
