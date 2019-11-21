package com.qzn.crowdfunding.potal.dao;

import com.qzn.crowdfunding.bean.Member;
import com.qzn.crowdfunding.bean.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketMapper {
    int deleteByPrimaryKey(Integer id);

    Member selectByPrimaryKey(Integer id);

    List<Ticket> selectAll();

    Ticket getTicketByMemberId(Integer memberid);

    void saveTicket(Ticket ticket);

    void updatePstep(Ticket ticket);

    void updatePiidAndPstep(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}