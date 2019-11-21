package com.qzn.crowdfunding.manager.controller;

import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.bean.User;
import com.qzn.crowdfunding.manager.service.UserService;
import com.qzn.crowdfunding.util.AjaxResult;
import com.qzn.crowdfunding.util.Page;
import com.qzn.crowdfunding.util.StringUtil;
import com.qzn.crowdfunding.vo.Data;
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
 * @create 2019/10/28 11:06
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(){

        return "user/index";
    }


    @RequestMapping("/toAdd")
    public String toAdd(){

        return "user/add";
    }

    //分配角色
    @ResponseBody
    @RequestMapping("/doAssignRole")
    public Object doAssignRole(Integer userid,Data data) {
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.saveUserRoleRelationship(userid,data);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("分配角色失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    //取消角色
    @ResponseBody
    @RequestMapping("/doUnAssignRole")
    public Object doUnAssignRole(Integer userid,Data data) {
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.deleteUserRoleRelationship(userid,data);
            result.setSuccess(count==data.getIds().size());
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除角色失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }



    //显示分配角色数据
    @RequestMapping("/assignRole")
    public String assignRole(Integer id,Map map) {

        List<Role> allListRole = userService.queryAllRole();

        List<Integer> roleIds = userService.queryRoleByUserid(id);

        List<Role> leftRoleList = new ArrayList<>();//未分配角色
        List<Role> rightRoleList = new ArrayList<>();//已分配角色

        for (Role role : allListRole) {

            if (roleIds.contains(role.getId())){
                rightRoleList.add(role);
            }else {
                leftRoleList.add(role);
            }
        }

        map.put("leftRoleList", leftRoleList);
        map.put("rightRoleList", rightRoleList);

        return "user/assignRole";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map) {

        User user = userService.getUserById(id);

        map.put("user",user);
        return "user/update";
    }

    //接收多条数据
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Data data) {
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.deleteUserBatchByVo(data);
            result.setSuccess(count==data.getDatas().size());
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    //接收一个参数名带多个值。
    /*@ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Integer[] id) {
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.deleteUserBatch(id);
            result.setSuccess(count==id.length);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }*/


    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(Integer id) {
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.deleteUser(id);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(User user) {
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.updateUser(user);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("修改数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(User user) {
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.saveUser(user);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("保存数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }


    //条件查询
    @ResponseBody
    @RequestMapping("/doIndex")
    public Object index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                        @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize,
                        String queryText) {
        AjaxResult result = new AjaxResult();
        try {

            Map paramMap = new HashMap();
            paramMap.put("pageno",pageno);
            paramMap.put("pagesize",pagesize);

            if (StringUtil.isNotEmpty(queryText)){
                paramMap.put("queryText",queryText);
            }
            Page page = userService.queryPage(paramMap);
            result.setSuccess(true);
            result.setPage(page);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("查询数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }





    //异步请求
    /*@ResponseBody
    @RequestMapping("/index")
    public Object index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                        @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize) {
        AjaxResult result = new AjaxResult();
        try {
            Page page = userService.queryPage(pageno, pagesize);

            result.setSuccess(true);
            result.setPage(page);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("查询数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }*/

    //同步请求
    /*@RequestMapping("/index")
    public String index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                        @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize, Map map){

        Page page = userService.queryPage(pageno, pagesize);

        map.put("page",page);

        return "user/index";
    }*/
}
