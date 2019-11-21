package com.qzn.crowdfunding.manager.dao;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Role selectByPrimaryKey(Integer id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    int deleteRoleBatchByVo(Data data);

    List<Role> queryList(Map paramMap);

    Integer queryCount(Map paramMap);

    List<Permission> queryAllPermission();

    List<Integer> queryPermissionIdsByRoleid(Integer roleid);

    int saveRolePermissionRelationship(@Param("roleid") Integer roleid,@Param("data") Data data);

    void deleteRolePermissionRelationship(Integer roleid);
}