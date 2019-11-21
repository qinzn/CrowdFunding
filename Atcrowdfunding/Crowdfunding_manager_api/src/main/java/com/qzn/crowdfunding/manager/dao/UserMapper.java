package com.qzn.crowdfunding.manager.dao;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.bean.User;
import com.qzn.crowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

	User queryUserLogin(Map<String, Object> paramMap);

//    List<User> queryList(@Param("startIndex") Integer startIndex,@Param("pagesize") Integer pagesize);
//
//    Integer queryCount();

    List<User> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    int deleteUserBatchByVo(Data data);

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserid(Integer id);

    int saveUserRoleRelationship(@Param("userid") Integer userid,@Param("data") Data data);

    int deleteUserRoleRelationship(@Param("userid")Integer userid,@Param("data") Data data);

    List<Permission> queryPermissionByUserid(Integer id);
}