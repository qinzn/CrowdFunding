<%--
  Created by IntelliJ IDEA.
  User: qzn
  Date: 2019/10/31
  Time: 15:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="btn-group">
    <button type="button" class="btn btn-default btn-success dropdown-toggle"
            data-toggle="dropdown">
        <i class="glyphicon glyphicon-user"></i> ${sessionScope.user.username} <span
            class="caret"></span>
    </button>
    <ul class="dropdown-menu" role="menu">
        <li><a href="#"><i class="glyphicon glyphicon-cog"></i> 个人设置</a></li>
        <li><a href="#"><i class="glyphicon glyphicon-comment"></i> 消息</a></li>
        <li class="divider"></li>
        <li><a href="${APP_PATH}/logout.do"><i class="glyphicon glyphicon-off"></i> 退出系统</a></li>
    </ul>
</div>
