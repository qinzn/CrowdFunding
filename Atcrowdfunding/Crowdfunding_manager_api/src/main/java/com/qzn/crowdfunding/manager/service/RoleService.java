package com.qzn.crowdfunding.manager.service;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.util.Page;
import com.qzn.crowdfunding.vo.Data;

import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/10/31 17:35
 */
public interface RoleService {
    Role getUserById(Integer id);

    int deleteRoleBatchByVo(Data data);

    int deleteRole(Integer id);

    int updateRole(Role role);

    int saveRole(Role role);

    Page queryPage(Map paramMap);

    List<Permission> queryAllPermission();

    List<Integer> queryPermissionIdsByRoleid(Integer roleid);

    int saveRolePermissionRelationship(Integer roleid, Data datas);

    void deleteRolePermissionRelationship(Integer roleid);
}
