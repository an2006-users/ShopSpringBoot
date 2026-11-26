<%@ page pageEncoding="UTF-8"%>
<%-- Biến dùng chung: ctx (context path), currentUser (username đang đăng nhập), isAdmin --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="currentUser" value="${pageContext.request.userPrincipal.name}"/>
<c:set var="isAdmin" value="${pageContext.request.isUserInRole('ADMIN')}"/>
