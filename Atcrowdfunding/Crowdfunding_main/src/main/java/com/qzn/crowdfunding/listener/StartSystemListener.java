package com.qzn.crowdfunding.listener;

import com.qzn.crowdfunding.bean.Permission;
import com.qzn.crowdfunding.manager.service.PermissionService;
import com.qzn.crowdfunding.util.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qzn
 * @create 2019/10/22 19:08
 */
public class StartSystemListener implements ServletContextListener {

    //在服务器启动，创建Application对象时，需要执行的方法
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //1.将项目的上下文路径(request.getContextPath())放置到Application域中
        ServletContext application = sce.getServletContext();
        String contextPath = application.getContextPath();
        application.setAttribute("APP_PATH",contextPath);
        System.out.println("APP_PATH......");

        //2.加载所有许可路径
        //1.查询所有许可
        ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
        PermissionService permissionService = ioc.getBean(PermissionService.class);
        List<Permission> permissions = permissionService.queryAllPermission();

        Set<String> allURIs = new HashSet<>();

        for (Permission permission : permissions) {
            allURIs.add("/"+permission.getUrl());
        }

        application.setAttribute(Const.ALL_PERMISSION_URI,allURIs);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
