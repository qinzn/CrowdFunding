package com.qzn.crowdfunding.manager.controller;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.manager.service.RoleService;
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
 * @create 2019/10/31 17:34
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/index")
    public String index() {

        return "role/index";
    }

    @RequestMapping("/assignPermission")
    public String assignPermission() {

        return "role/assignPermission";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {

        return "role/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Map map) {

        Role role = roleService.getUserById(id);

        map.put("role", role);
        return "role/update";
    }

    @ResponseBody
    @RequestMapping("/loadDataAsync")
    public Object loadDataAsync(Integer roleid) {

        List<Permission> root = new ArrayList<>();

        //父
        List<Permission> childrenPermission = roleService.queryAllPermission();

        List<Integer> permissionIdssForRoleid = roleService.queryPermissionIdsByRoleid(roleid);

        Map<Integer, Permission> map = new HashMap<>();

        for (Permission innerPermission : childrenPermission) {
            map.put(innerPermission.getId(), innerPermission);
            if (permissionIdssForRoleid.contains(innerPermission.getId())) {
                innerPermission.setChecked(true);
            }
        }

        for (Permission permission : childrenPermission) {
            Permission child = permission;
            if (child.getPid() == null) {
                root.add(child);
            } else {
                Permission parent = map.get(child.getPid());
                parent.getChildren().add(child);
            }
        }

        return root;
    }

    //接收多条数据
    @ResponseBody
    @RequestMapping("/doAssignPermission")
    public Object doAssignPermission(Integer roleid, Data data) {
        AjaxResult result = new AjaxResult();
        try {
            roleService.deleteRolePermissionRelationship(roleid);
            int countSave = roleService.saveRolePermissionRelationship(roleid, data);
            result.setSuccess(countSave == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    //接收多条数据
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Data data) {
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.deleteRoleBatchByVo(data);
            result.setSuccess(count == data.getDatas().size());
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(Integer id) {
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.deleteRole(id);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(Role role) {
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.updateRole(role);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("修改数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Role role) {
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.saveRole(role);
            result.setSuccess(count == 1);
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
    public Object index(@RequestParam(value = "pageno", required = false, defaultValue = "1") Integer pageno,
                        @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize,
                        String queryText) {
        AjaxResult result = new AjaxResult();
        try {

            Map paramMap = new HashMap();
            paramMap.put("pageno", pageno);
            paramMap.put("pagesize", pagesize);

            if (StringUtil.isNotEmpty(queryText)) {
                paramMap.put("queryText", queryText);
            }
            Page page = roleService.queryPage(paramMap);
            result.setSuccess(true);
            result.setPage(page);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("查询数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }
}
