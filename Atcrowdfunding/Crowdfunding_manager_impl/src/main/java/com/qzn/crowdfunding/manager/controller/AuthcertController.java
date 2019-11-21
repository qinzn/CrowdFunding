package com.qzn.crowdfunding.manager.controller;

import com.qzn.crowdfunding.bean.Member;
import com.qzn.crowdfunding.potal.service.MemberService;
import com.qzn.crowdfunding.potal.service.TicketService;
import com.qzn.crowdfunding.util.AjaxResult;
import com.qzn.crowdfunding.util.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/11/20 11:08
 */
@Controller
@RequestMapping("/authcert")
public class AuthcertController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index() {
        return "authcert/index";
    }

    @RequestMapping("/show")
    public String show(Integer memberid, Map map) {
        Member member = memberService.getMemberById(memberid);

        List<Map<String, Object>> list = memberService.queryCertByMemberid(memberid);

        map.put("member", member);
        map.put("certimgs", list);

        return "authcert/show";
    }

    @ResponseBody
    @RequestMapping("/pass")
    public Object pass(String taskid, Integer memberid) {
        AjaxResult result = new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", true);
            taskService.setVariable(taskid, "memberid", memberid);

            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/refuse")
    public Object refuse(String taskid, Integer memberid) {
        AjaxResult result = new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", false);
            taskService.setVariable(taskid, "memberid", memberid);

            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("/pageQuery")
    @ResponseBody
    public Object pageQuery(@RequestParam(value = "pageno", required = false, defaultValue = "1") Integer pageno,
                            @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize) {

        AjaxResult result = new AjaxResult();

        try {
            Page page = new Page(pageno, pagesize);

            TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey("auth").
                    taskCandidateGroup("backuser");

            // 查询后台backuser委托组的任务
            List<Task> listPage = taskQuery.listPage(page.getStartIndex(), pagesize);

            List<Map<String, Object>> datas = new ArrayList<>();

            for (Task task : listPage) {
                Map<String, Object> map = new HashMap<>();
                map.put("taskid", task.getId());
                map.put("taskName", task.getName());

                // 根据任务查询流程定义（流程定义的名称和版本）
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                        processDefinitionId(task.getProcessDefinitionId()).
                        singleResult();

                map.put("procDefName", processDefinition.getName());
                map.put("procDefVersion", processDefinition.getVersion());

                // 根据任务查询流程实例（根据流程实例的id查询流程单，查询用户信息）
                Member member = ticketService.getMemberByPiid(task.getProcessInstanceId());

                map.put("member", member);

                datas.add(map);
            }
            page.setDatas(datas);
            Long count = taskQuery.count();
            page.setTotalsize(count.intValue());

            result.setPage(page);

            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("任务查询列表失败！");
        }
        return result;
    }
}
