package com.qzn.crowdfunding.potal.service.impl;

import com.qzn.crowdfunding.bean.Member;
import com.qzn.crowdfunding.potal.dao.MemberMapper;
import com.qzn.crowdfunding.potal.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/11/13 15:51
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member queryMemberLogin(Map<String, Object> paramMap) {
        return memberMapper.queryMebmerlogin(paramMap);
    }

    @Override
    public void updateAcctType(Member member) {
        memberMapper.updateAcctType(member);
    }

    @Override
    public void updateBasicinfo(Member member) {
        memberMapper.updateBasicinfo(member);
    }

    @Override
    public void updateEmail(Member member) {
        memberMapper.updateEmail(member);
    }

    @Override
    public void updateAuthstatus(Member loginMember) {
        memberMapper.updateAuthstatus(loginMember);
    }

    @Override
    public Member getMemberById(Integer memberid) {
        return memberMapper.selectByPrimaryKey(memberid);
    }

    @Override
    public List<Map<String, Object>> queryCertByMemberid(Integer memberid) {
        return memberMapper.queryCertByMemberid(memberid);
    }
}
