<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Dashboard"/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<h1 class="h3 mb-4">Dashboard quản trị</h1>
<div class="row g-3 mb-4">
    <div class="col-sm-6 col-lg-3"><div class="card border-0 shadow-sm"><div class="card-body">
        <div class="text-secondary small">Danh mục</div><div class="fs-3 fw-semibold">${categoryCount}</div>
        <div class="small text-success">${activeCategoryCount} đang hoạt động</div></div></div></div>
    <div class="col-sm-6 col-lg-3"><div class="card border-0 shadow-sm"><div class="card-body">
        <div class="text-secondary small">Người dùng</div><div class="fs-3 fw-semibold">${userCount}</div>
        <div class="small text-warning">${adminCount} ADMIN</div></div></div></div>
</div>

<div class="row g-3">
    <div class="col-lg-6">
        <div class="card border-0 shadow-sm h-100">
            <div class="card-header bg-white d-flex justify-content-between"><strong>Danh mục mới</strong><a href="${ctx}/admin/categories/searchpaginated">Quản lý →</a></div>
            <ul class="list-group list-group-flush">
                <c:forEach items="${recentCategories}" var="cat">
                    <li class="list-group-item d-flex justify-content-between"><c:out value="${cat.name}"/>
                        <span class="badge ${cat.active ? 'text-bg-success' : 'text-bg-secondary'}">${cat.active ? 'Hoạt động' : 'Khoá'}</span></li>
                </c:forEach>
                <c:if test="${empty recentCategories}"><li class="list-group-item text-secondary">Chưa có danh mục</li></c:if>
            </ul>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="card border-0 shadow-sm h-100">
            <div class="card-header bg-white d-flex justify-content-between"><strong>Người dùng mới</strong><a href="${ctx}/admin/users/searchpaginated">Quản lý →</a></div>
            <ul class="list-group list-group-flush">
                <c:forEach items="${recentUsers}" var="u">
                    <li class="list-group-item d-flex justify-content-between"><span><c:out value="${u.fullname}"/> <small class="text-secondary">@<c:out value="${u.username}"/></small></span>
                        <span class="badge ${u.role == 'ADMIN' ? 'text-bg-warning' : 'text-bg-light'}">${u.role}</span></li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
