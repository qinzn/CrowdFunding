package com.qzn.crowdfunding.controller;

import com.qzn.crowdfunding.bean.Member;
import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.bean.User;
import com.qzn.crowdfunding.manager.service.UserService;
import com.qzn.crowdfunding.potal.service.MemberService;
import com.qzn.crowdfunding.util.AjaxResult;
import com.qzn.crowdfunding.util.Const;
import com.qzn.crowdfunding.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author qzn
 * @create 2019/10/23 13:44
 */
@Controller
public class DispatcherController {

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String index(HttpServletRequest request, HttpSession session) {
        //判断是否需要自动登录
        //如果之前登录过，cookie中存放了用户信息，需要获取cookie中的信息，并进行数据库的验证
        boolean needLogin = true;
        String logintype = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) { //如果客户端禁用了Cookie，那么无法获取Cookie信息
            for (Cookie cookie : cookies) {

                if ("logincode".equals(cookie.getName())) {
                    String logincode = cookie.getValue();
                    System.out.println("获取到Cookie中的键值对" + cookie.getName() + "===== " + logincode);
                    //loginacct=admin&userpswd=21232f297a57a5a743894a0e4a801fc3&logintype=1

                    String[] split = logincode.split("&");

                    if (split.length == 3) {

                        String loginacct = split[0].split("=")[1];
                        String userpswd = split[1].split("=")[1];
                        logintype = split[2].split("=")[1];

                        Map<String, Object> paramMap = new HashMap<>();
                        paramMap.put("loginacct", loginacct);
                        paramMap.put("userpswd", userpswd);
                        paramMap.put("type", logintype);

                        if ("user".equals(logintype)) {
                            User dbLogin = userService.queryUserLogin(paramMap);
                            if (dbLogin != null) {
                                session.setAttribute(Const.LOGIN_USER, dbLogin);
                                needLogin = false;
                            }
                            //加载当前用户所拥有的许可权限
                            List<Permission> myPermissions = userService.queryPermissionByUserid(dbLogin.getId());//当前用户所拥有的许可权限

                            Permission permissionRoot = null;

                            Map<Integer, Permission> map = new HashMap<>();

                            Set<String> myUris = new HashSet<>();

                            for (Permission innerPermission : myPermissions) {
                                map.put(innerPermission.getId(), innerPermission);

                                myUris.add("/" + innerPermission.getUrl());
                            }

                            session.setAttribute(Const.MY_URIS, myUris);

                            for (Permission permission : myPermissions) {
                                Permission child = permission;
                                if (child.getPid() == null) {
                                    permissionRoot = permission;
                                } else {
                                    Permission parent = map.get(child.getPid());
                                    parent.getChildren().add(child);
                                }
                            }

                            session.setAttribute("permissionRoot", permissionRoot);
                        } else if ("member".equals(logintype)) {
                            Member dbLogin = memberService.queryMemberLogin(paramMap);
                            if (dbLogin != null) {
                                session.setAttribute(Const.LOGIN_MEMBER, dbLogin);
                                needLogin = false;
                            }
                        }
                    }
                }
            }
        }
        if (needLogin) {
            return "login";
        } else {
            if ("user".equals(logintype)) {
                return "redirect:/main.htm";
            } else if ("member".equals(logintype)) {
                return "redirect:/member.htm";
            }
        }
        return "login";
    }

    @RequestMapping("/reg")
    public String reg() {
        return "reg";
    }

    @RequestMapping("/member")
    public String member() {
        return "member/member";
    }

    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();//销毁session对象，或清理session域

        return "redirect:/index.jsp";
    }

    //异步请求：
    //ResponseBody 结合Jackson组件，将返回结果转换为字符串，将JSON串以流的形式返回给客户端。
    @ResponseBody
    @RequestMapping("/doLogin")
    public Object doLogin(String loginacct, String userpswd, String type,
                          String rememberme, HttpSession session, HttpServletResponse response) {

        AjaxResult result = new AjaxResult();

        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("loginacct", loginacct);
            paramMap.put("userpswd", MD5Util.digest(userpswd));
            paramMap.put("type", type);

            if ("member".equals(type)) {
                Member member = memberService.queryMemberLogin(paramMap);
                session.setAttribute(Const.LOGIN_MEMBER, member);

                if ("1".equals(rememberme)) {
                    String logincode = "\"loginacct=" + member.getLoginacct() + "&userpswd=" + member.getUserpswd() + "&logintype=member\"";

                    //loginacct=zhangsan&userpswd=21232f297a57a5a743894a0e4a801fc3&logintype=member

                    System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::" + logincode);

                    Cookie c = new Cookie("logincode", logincode);

                    c.setMaxAge(60 * 60 * 24 * 14); //2周时间Cookie过期     单位秒

                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);


                }

            } else if ("user".equals(type)) {
                User user = userService.queryUserLogin(paramMap);
                session.setAttribute(Const.LOGIN_USER, user);

                if ("1".equals(rememberme)) {
                    String logincode = "\"loginacct=" + user.getLoginacct() + "&userpswd=" + user.getUserpswd() + "&logintype=user\"";

                    //loginacct=zhangsan&userpswd=21232f297a57a5a743894a0e4a801fc3&logintype=user

                    System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::" + logincode);

                    Cookie c = new Cookie("logincode", logincode);

                    c.setMaxAge(60 * 60 * 24 * 14); //2周时间Cookie过期     单位秒

                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);


                }

                //加载当前用户所拥有的许可权限
                List<Permission> myPermissions = userService.queryPermissionByUserid(user.getId());//当前用户所拥有的许可权限

                Permission permissionRoot = null;

                Map<Integer, Permission> map = new HashMap<>();

                Set<String> myUris = new HashSet<>();

                for (Permission innerPermission : myPermissions) {
                    map.put(innerPermission.getId(), innerPermission);

                    myUris.add("/" + innerPermission.getUrl());
                }

                session.setAttribute(Const.MY_URIS, myUris);

                for (Permission permission : myPermissions) {
                    Permission child = permission;
                    if (child.getPid() == null) {
                        permissionRoot = permission;
                    } else {
                        Permission parent = map.get(child.getPid());
                        parent.getChildren().add(child);
                    }
                }

                session.setAttribute("permissionRoot", permissionRoot);

            } else {

            }

            result.setData(type);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("登录失败！");
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }


    //同步请求：
    /*@RequestMapping("/doLogin")
    public String doLogin(String loginacct, String userpswd, String type, HttpSession session){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loginacct",loginacct);
        paramMap.put("userpswd",userpswd);
        paramMap.put("type",type);

        User user = userService.queryUserLogin(paramMap);
        session.setAttribute(Const.LOGIN_USER,user);

        return "redirect:/main.htm";
    }*/
}
