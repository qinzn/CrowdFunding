package com.qzn.crowdfunding.vo;

import com.qzn.crowdfunding.bean.MemberCert;
import com.qzn.crowdfunding.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qzn
 * @create 2019/10/30 16:34
 */
public class Data{

    private List<User> userList = new ArrayList<>();
    private List datas = new ArrayList<>();

    private List<Integer> ids ;

    private List<MemberCert> certimgs = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

    public List<User> getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<MemberCert> getCertimgs() {
        return certimgs;
    }

    public void setCertimgs(List<MemberCert> certs) {
        this.certimgs = certs;
    }
}
