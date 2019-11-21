package com.qzn.crowdfunding.manager.controller;

import com.qzn.crowdfunding.util.AjaxResult;
import com.qzn.crowdfunding.util.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/11/12 16:52
 */
@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }

    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(String id){//流程定义id

        AjaxResult result = new AjaxResult();

        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();

            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);//true表示级联删除

            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("部署流程定义失败！");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/deploy")
    public Object doIndex(HttpServletRequest request){

        AjaxResult result = new AjaxResult();

        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

            MultipartFile multipartFile = multipartHttpServletRequest.getFile("processDefFile");

            repositoryService.createDeployment().addInputStream(multipartFile.getOriginalFilename(), multipartFile.getInputStream()).deploy();

            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("部署流程定义失败！");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                          @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize){

        AjaxResult result = new AjaxResult();

        try {
            Page page = new Page(pageno,pagesize);

            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            //查询流程定义集合数据，可能出现了自关联，导致jackson组件无法将集合序列化为JSON串
            List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage(page.getStartIndex(), pagesize);

            List<Map<String,Object>> listPage = new ArrayList<>();

            for (ProcessDefinition processDefinition : processDefinitions) {
                Map<String,Object> pd = new HashMap<>();
                pd.put("id",processDefinition.getId());
                pd.put("name",processDefinition.getName());
                pd.put("key",processDefinition.getKey());
                pd.put("version",processDefinition.getVersion());

                listPage.add(pd);
            }

            Long totalsize = processDefinitionQuery.count();

            page.setDatas(listPage);

            page.setTotalsize(totalsize.intValue());

            result.setPage(page);
            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("查询流程定义失败！");
        }

        return result;
    }
}
