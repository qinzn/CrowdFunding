package com.qzn.crowdfunding.potal.listener;

import com.qzn.crowdfunding.bean.Member;
import com.qzn.crowdfunding.potal.service.MemberService;
import com.qzn.crowdfunding.potal.service.TicketService;
import com.qzn.crowdfunding.util.ApplicationContextUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

/**
 * 实名认证审核通过执行的操作
 * @author qzn
 * @create 2019/11/14 11:25
 */
public class PassListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {

        Integer memberid = (Integer) execution.getVariable("memberid");

        // 获取IOC容器，通过自定义的工具类，实现Spring接口，以接口注入的方式获取IOC容器对象
        ApplicationContext applicationContext = ApplicationContextUtils.applicationContext;

        TicketService ticketService = applicationContext.getBean(TicketService.class);
        MemberService memberService = applicationContext.getBean(MemberService.class);

        // 更新t_member表的authstatus字段： 1 -> 2 - 已实名认证
        Member member = memberService.getMemberById(memberid);
        member.setAuthstatus("2");
        memberService.updateAuthstatus(member);

        // 更新t_ticket表的status字段: 0 -> 1 表示流程结束
        ticketService.updateStatus(member);
    }
}
