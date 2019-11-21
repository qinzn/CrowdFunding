package com.qzn.crowdfunding.manager.service.impl;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.Role;
import com.qzn.crowdfunding.bean.User;
import com.qzn.crowdfunding.exception.LoginFailException;
import com.qzn.crowdfunding.manager.dao.UserMapper;
import com.qzn.crowdfunding.manager.service.UserService;
import com.qzn.crowdfunding.util.Const;
import com.qzn.crowdfunding.util.MD5Util;
import com.qzn.crowdfunding.util.Page;
import com.qzn.crowdfunding.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qzn
 * @create 2019/10/23 15:47
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserLogin(Map<String, Object> paramMap) {

        User user = userMapper.queryUserLogin(paramMap);

        if (user == null) {
            throw new LoginFailException("用户账号或密码不正确！");
        }

        return user;
    }




    /*@Override
    public Page queryPage(Integer pageno, Integer pagesize) {
        Page page = new Page(pageno,pagesize);

        Integer startIndex = page.getStartIndex();

        List<User> datas = userMapper.queryList(startIndex,pagesize);

        page.setDatas(datas);

        Integer totalsize = userMapper.queryCount();

        page.setTotalsize(totalsize);

        return page;
    }*/

    @Override
    public int saveUser(User user) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();

        String createTime = sdf.format(date);

        user.setCreatetime(createTime);

        user.setUserpswd(MD5Util.digest(Const.PASSWORD));

        return userMapper.insert(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteUserBatch(Integer[] ids) {
        int totalCount = 0;
        for (Integer id : ids) {
            int count = userMapper.deleteByPrimaryKey(id);
            totalCount += count;
        }
        if (totalCount != ids.length){
            throw new RuntimeException("批量删除失败！");
        }
        return totalCount;
    }

    @Override
    public int deleteUserBatchByVo(Data data) {
        return userMapper.deleteUserBatchByVo(data);
    }

    @Override
    public List<Role> queryAllRole() {
        return userMapper.queryAllRole();
    }

    @Override
    public List<Integer> queryRoleByUserid(Integer id) {
        return userMapper.queryRoleByUserid(id);
    }

    @Override
    public int saveUserRoleRelationship(Integer userid, Data data) {
        return userMapper.saveUserRoleRelationship(userid,data);
    }

    @Override
    public int deleteUserRoleRelationship(Integer userid, Data data) {
        return userMapper.deleteUserRoleRelationship(userid,data);
    }

    @Override
    public List<Permission> queryPermissionByUserid(Integer id) {
        return userMapper.queryPermissionByUserid(id);
    }

    @Override
    public Page queryPage(Map paramMap) {
        Page page = new Page((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = page.getStartIndex();
        paramMap.put("startIndex",startIndex);

        List<User> datas = userMapper.queryList(paramMap);

        page.setDatas(datas);

        Integer totalsize = userMapper.queryCount(paramMap);

        page.setTotalsize(totalsize);

        return page;
    }
}
