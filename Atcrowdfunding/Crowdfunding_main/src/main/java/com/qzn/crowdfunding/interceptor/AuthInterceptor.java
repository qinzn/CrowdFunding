package com.qzn.crowdfunding.interceptor;

import com.qzn.crowdfunding.util.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author qzn
 * @create 2019/11/4 15:42
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Set<String> allURIs = (Set<String>)request.getSession().getServletContext().getAttribute(Const.ALL_PERMISSION_URI);

        //服务器启动时加载所有许可路径，并存放在application域中
        //2.判断请求路径是否在所有许可的范围内
        String servletPath = request.getServletPath();
        if (allURIs.contains(servletPath)) {

            //3.判断请求路径是否在用户所拥有的权限内
            Set<String> myURIs = (Set<String>) request.getSession().getAttribute(Const.MY_URIS);
            if (myURIs.contains(servletPath)) {
                return true;
            } else {
                response.sendRedirect(request.getContextPath() + "/login.htm");
                return false;
            }

        } else {//不再拦截范围内
            return true;
        }
    }
}
