package com.qzn.crowdfunding.potal.dao;

import com.qzn.crowdfunding.bean.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    Member selectByPrimaryKey(Integer id);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

	Member queryMebmerlogin(Map<String, Object> paramMap);

	void updateAcctType(Member member);

	void updateBasicinfo(Member member);

    void updateEmail(Member member);

    void updateAuthstatus(Member loginMember);

    List<Map<String, Object>> queryCertByMemberid(Integer memberid);
}