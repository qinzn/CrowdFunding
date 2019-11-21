package com.qzn.crowdfunding.manager.service;

import com.qzn.crowdfunding.bean.Permission;

import java.util.List;

/**
 * @author qzn
 * @create 2019/11/1 14:52
 */
public interface PermissionService {

    Permission getRootPermission();

    List<Permission> getChildrenPermissionByPid(Integer id);

    List<Permission> queryAllPermission();

    int savePermission(Permission permission);

    Permission getPermissionById(Integer id);

    int updatePermission(Permission permission);

    int deletePermission(Integer id);
}
