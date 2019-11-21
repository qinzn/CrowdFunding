package com.qzn.crowdfunding.manager.service;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.bean.User;
import com.qzn.crowdfunding.util.Page;
import com.qzn.crowdfunding.vo.Data;

import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/10/23 15:47
 */
public interface UserService {
    User queryUserLogin(Map<String, Object> paramMap);

    //Page queryPage(Integer pageno, Integer pagesize);

    int saveUser(User user);

    Page queryPage(Map<String,Object> paramMap);

    User getUserById(Integer id);

    int updateUser(User user);

    int deleteUser(Integer id);

    int deleteUserBatch(Integer[] ids);

    int deleteUserBatchByVo(Data data);

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserid(Integer id);

    int saveUserRoleRelationship(Integer userid, Data data);

    int deleteUserRoleRelationship(Integer userid, Data data);

    List<Permission> queryPermissionByUserid(Integer id);
}
