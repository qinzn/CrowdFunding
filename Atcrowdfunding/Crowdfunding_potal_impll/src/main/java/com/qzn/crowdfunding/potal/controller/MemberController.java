package com.qzn.crowdfunding.potal.controller;

import com.qzn.crowdfunding.bean.Cert;
import com.qzn.crowdfunding.bean.Member;
import com.qzn.crowdfunding.bean.MemberCert;
import com.qzn.crowdfunding.bean.Ticket;
import com.qzn.crowdfunding.manager.service.CertService;
import com.qzn.crowdfunding.potal.listener.PassListener;
import com.qzn.crowdfunding.potal.listener.RefuseListener;
import com.qzn.crowdfunding.potal.service.MemberService;
import com.qzn.crowdfunding.potal.service.TicketService;
import com.qzn.crowdfunding.util.AjaxResult;
import com.qzn.crowdfunding.util.Const;
import com.qzn.crowdfunding.vo.Data;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * @author qzn
 * @create 2019/11/14 17:38
 */
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CertService certService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/accttype")
    public String accttype() {
        return "member/accttype";
    }

    @RequestMapping("/checkauthcode")
    public String checkauthcode() {
        return "member/checkauthcode";
    }

    @RequestMapping("/checkemail")
    public String checkemail() {
        return "member/checkemail";
    }

    @RequestMapping("/uploadCert")
    public String uploadCert() {
        return "member/uploadCert";
    }

    @RequestMapping("/basicinfo")
    public String basicinfo() {
        return "member/basicinfo";
    }

    @RequestMapping("/apply")
    public String apply(HttpSession session) {
        Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);
        Ticket ticket = ticketService.getTicketByMemberId(member.getId());
        if (ticket == null) {
            ticket = new Ticket();
            ticket.setMemberid(member.getId());
            ticket.setPstep("apply");
            ticket.setStatus("0");

            ticketService.saveTicket(ticket);
        } else {
            String pstep = ticket.getPstep();
            if ("accttype".equals(pstep)) {
                return "redirect:/member/basicinfo.htm";
            } else if ("basicinfo".equals(pstep)) {

                //根据当前账户查询账户类型，然后根据账户类型查找需要上传的资质
                List<Cert> queryCertByAccttype = certService.queryCertByAccttype(member.getAccttype());

                session.setAttribute("queryCertByAccttype", queryCertByAccttype);

                return "redirect:/member/uploadCert.htm";
            } else if ("uploadcert".equals(pstep)) {

                return "redirect:/member/checkemail.htm";
            }else if ("checkemail".equals(pstep)) {

                return "redirect:/member/checkauthcode.htm";
            }
        }
        return "member/accttype";
    }

    @ResponseBody
    @RequestMapping("/finishApply")
    public Object finishApply(HttpSession session, String authcode) {
        AjaxResult result = new AjaxResult();
        try {
            // 获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);

            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            if (ticket.getAuthcode().equals(authcode)){

                // 让当前系统用户完成：验证码审核任务
                Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(loginMember.getLoginacct()).singleResult();
                taskService.complete(task.getId());

                // 更新用户申请状态
                loginMember.setAuthstatus("1");
                memberService.updateAuthstatus(loginMember);

                ticket.setPstep("finishapply");
                ticketService.updatePstep(ticket);
                result.setSuccess(true);
            }else {
                result.setSuccess(false);
                result.setMessage("验证码不正确，请重新输入！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/startProcess")
    public Object startProcess(HttpSession session, String email) {
        AjaxResult result = new AjaxResult();
        try {
            // 获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);

            // 如果用户输入了新的邮箱，将旧的邮箱替换
            if (!loginMember.getEmail().equals(email)) {
                loginMember.setEmail(email);
                memberService.updateEmail(loginMember);
            }
            // 启动实名认证的流程 - 系统自动发送邮件，生成验证码，验证邮箱地址是否合法
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("auth").singleResult();

            StringBuilder authcode = new StringBuilder();
            for (int i = 1; i <= 4; i++) {
                authcode.append(new Random().nextInt(10));
            }

            //toEmail loginacct flag passListener refuseListener authcode
            Map<String, Object> map = new HashMap<>();
            map.put("toEmail", email);
            map.put("loginacct", loginMember.getLoginacct());
            map.put("passListener", new PassListener());
            map.put("refuseListener", new RefuseListener());
            map.put("authcode", authcode);

            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), map);


            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("checkemail");
            ticket.setPiid(processInstance.getId());
            ticket.setAuthcode(authcode.toString());
            ticketService.updatePiidAndPstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/doUploadCert")
    public Object doUploadCert(HttpSession session, Data ds) {
        AjaxResult result = new AjaxResult();
        try {
            // 获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);

            String realPath = session.getServletContext().getRealPath("/pics");

            List<MemberCert> certimgs = ds.getCertimgs();
            for (MemberCert certimg : certimgs) {

                MultipartFile fileImg = certimg.getFileImg();
                String extName = fileImg.getOriginalFilename().substring(fileImg.getOriginalFilename().lastIndexOf("."));

                String tempName = UUID.randomUUID().toString() + extName;

                String filename = realPath + "/cert" + "/" + tempName;

                fileImg.transferTo(new File(filename));// 资质文件上传
                certimg.setIconpath(tempName);// 封装数据，保存到数据库
                certimg.setMemberid(loginMember.getId());
            }
            // 保存会员与资质的关系数据
            certService.saveMemberCert(certimgs);


            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("uploadcert");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/updateBasicinfo")
    public Object updateBasicinfo(HttpSession session, Member member) {
        AjaxResult result = new AjaxResult();
        try {
            // 获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginMember.setRealname(member.getRealname());
            loginMember.setCardnum(member.getCardnum());
            loginMember.setTel(member.getTel());
            // 更新会员的基本信息
            memberService.updateBasicinfo(loginMember);

            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("basicinfo");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 更新账户类型
     */

    @ResponseBody
    @RequestMapping("/updateAcctType")
    public Object updateAcctType(HttpSession session, String accttype) {
        AjaxResult result = new AjaxResult();
        try {
            // 获取登录会员信息
            Member member = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            member.setAccttype(accttype);
            // 更新账户类型
            memberService.updateAcctType(member);

            Ticket ticket = ticketService.getTicketByMemberId(member.getId());
            ticket.setPstep("accttype");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

}
