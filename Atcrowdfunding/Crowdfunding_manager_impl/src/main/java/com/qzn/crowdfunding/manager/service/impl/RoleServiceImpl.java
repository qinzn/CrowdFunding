package com.qzn.crowdfunding.manager.service.impl;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.manager.dao.RoleMapper;
import com.qzn.crowdfunding.manager.service.RoleService;
import com.qzn.crowdfunding.util.Page;
import com.qzn.crowdfunding.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/10/31 17:37
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role getUserById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteRoleBatchByVo(Data data) {
        return roleMapper.deleteRoleBatchByVo(data);
    }

    @Override
    public int deleteRole(Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateRole(Role role) {
        return roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public int saveRole(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    public Page queryPage(Map paramMap) {
        Page page = new Page((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = page.getStartIndex();
        paramMap.put("startIndex",startIndex);

        List<Role> datas = roleMapper.queryList(paramMap);

        page.setDatas(datas);

        Integer totalsize = roleMapper.queryCount(paramMap);

        page.setTotalsize(totalsize);

        return page;
    }

    @Override
    public List<Permission> queryAllPermission() {
        return roleMapper.queryAllPermission();
    }

    @Override
    public List<Integer> queryPermissionIdsByRoleid(Integer roleid) {
        return roleMapper.queryPermissionIdsByRoleid(roleid);
    }

    @Override
    public int saveRolePermissionRelationship(Integer roleid, Data data) {
        return roleMapper.saveRolePermissionRelationship(roleid,data);
    }

    @Override
    public void deleteRolePermissionRelationship(Integer roleid) {
        roleMapper.deleteRolePermissionRelationship(roleid);
    }
}
