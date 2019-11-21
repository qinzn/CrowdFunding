package com.qzn.crowdfunding.manager.controller;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.manager.service.PermissionService;
import com.qzn.crowdfunding.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/11/1 14:50
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index() {
        return "permission/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "permission/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map) {
        Permission permission = permissionService.getPermissionById(id);

        map.put("permission",permission);
        return "permission/update";
    }

    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(Permission permission) {
        AjaxResult result = new AjaxResult();
        try {
            int count = permissionService.updatePermission(permission);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("修改许可数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(Integer id) {
        AjaxResult result = new AjaxResult();
        try {
            int count = permissionService.deletePermission(id);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除许可数据失败！");
        }

        return result;//将对象序列化为JSON字符串，以流的形式返回。
    }

    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Permission permission) {
        AjaxResult result = new AjaxResult();

        try {
            int count = permissionService.savePermission(permission);

            result.setSuccess(count == 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("保存许可数据失败！");
        }
        return result;
    }

    //采用一次性加载所有permission数据，减少与数据库交互的次数
    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData() {
        AjaxResult result = new AjaxResult();

        try {

            List<Permission> root = new ArrayList<>();

            //父
            List<Permission> childrenPermission = permissionService.queryAllPermission();

            Map<Integer, Permission> map = new HashMap<>();

            for (Permission innerPermission : childrenPermission) {
                map.put(innerPermission.getId(), innerPermission);
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
            result.setSuccess(true);
            result.setData(root);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("加载许可树数据失败！");
        }
        return result;
    }

    /*//采用递归调用，来解决许可树多个层次的问题
    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData(){
        AjaxResult result = new AjaxResult();

        try {

            List<Permission> root = new ArrayList<>();

            //父
            Permission permission = permissionService.getRootPermission();

            root.add(permission);

            queryChildPermission(permission);

            result.setSuccess(true);
            result.setData(root);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("加载许可树数据失败！");
        }
        return result;
    }*/

    /*private void queryChildPermission(Permission permission) {

        List<Permission> children = permissionService.getChildrenPermissionByPid(permission.getId());

        permission.setChildren(children);

        for (Permission innerChildren : children) {
            queryChildPermission(innerChildren);
        }

    }*/


    /*@ResponseBody
    @RequestMapping("/loadData")
    public Object loadData(){
        AjaxResult result = new AjaxResult();

        try {

            List<Permission> root = new ArrayList<>();

            //父
            Permission permission = permissionService.getRootPermission();

            root.add(permission);
            //子
            List<Permission> children = permissionService.getChildrenPermissionByPid(permission.getId());

            //设置父子关系
            permission.setChildren(children);

            for (Permission child : children) {
                child.setOpen(true);

                List<Permission> innerChildren = permissionService.getChildrenPermissionByPid(child.getId());

                child.setChildren(innerChildren);
            }

            result.setSuccess(true);
            result.setData(root);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("加载许可树数据失败！");
        }
        return result;
    }*/
}
