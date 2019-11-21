package com.qzn.crowdfunding.potal.service;


import com.qzn.crowdfunding.bean.Member;
import com.qzn.crowdfunding.bean.Ticket;

/**
 * @author qzn
 * @create 2019/11/15 11:34
 */
public interface TicketService {
    Ticket getTicketByMemberId(Integer id);

    void saveTicket(Ticket ticket);

    void updatePstep(Ticket ticket);

    void updatePiidAndPstep(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}
