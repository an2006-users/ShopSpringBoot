<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Không có quyền"/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>
<div class="text-center py-5">
    <div class="display-1 fw-bold text-danger">403</div>
    <p class="lead">Bạn không có quyền truy cập trang này.</p>
    <a class="btn btn-primary" href="${ctx}/">Về trang chủ</a>
</div>
</body>
</html>
