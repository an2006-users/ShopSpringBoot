<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Quản lý người dùng"/>
<c:set var="baseUrl" value="/admin/users/searchpaginated"/>
<c:set var="pageData" value="${userPage}"/>
<c:set var="placeholder" value="Tìm theo username, họ tên, email..."/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
    <h1 class="h3 mb-0">Người dùng</h1>
    <a class="btn btn-primary" href="${ctx}/admin/users/add"><i class="bi bi-person-plus"></i> Thêm người dùng</a>
</div>
<%@ include file="/WEB-INF/views/common/alerts.jsp"%>

<div class="card border-0 shadow-sm">
    <div class="card-body"><%@ include file="/WEB-INF/views/common/search-bar.jsp"%></div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead class="table-light"><tr><th>#</th><th>Username</th><th>Họ tên</th><th>Email</th><th>SĐT</th><th>Vai trò</th><th>Trạng thái</th><th class="text-end">Thao tác</th></tr></thead>
            <tbody>
            <c:forEach items="${pageData.content}" var="u" varStatus="st">
                <tr>
                    <td>${pageData.number * pageData.size + st.index + 1}</td>
                    <td class="fw-semibold"><c:out value="${u.username}"/><c:if test="${u.username == currentUser}"> <span class="badge text-bg-info">Bạn</span></c:if></td>
                    <td><c:out value="${u.fullname}"/></td>
                    <td><c:out value="${u.email}"/></td>
                    <td><c:out value="${u.phone}"/></td>
                    <td><span class="badge ${u.role == 'ADMIN' ? 'text-bg-warning' : 'text-bg-light border'}">${u.role}</span></td>
                    <td><span class="badge ${u.enabled ? 'text-bg-success' : 'text-bg-secondary'}">${u.enabled ? 'Hoạt động' : 'Bị khoá'}</span></td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/users/edit/${u.id}"><i class="bi bi-pencil"></i> Sửa</a>
                        <c:if test="${u.username != currentUser}">
                            <form class="d-inline" method="post" action="${ctx}/admin/users/delete/${u.id}"
                                  onsubmit="return confirm('Xoá người dùng này?');">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                <input type="hidden" name="name" value="<c:out value='${name}'/>">
                                <input type="hidden" name="page" value="${pageData.number + 1}">
                                <input type="hidden" name="size" value="${pageData.size}">
                                <button class="btn btn-sm btn-outline-danger" type="submit"><i class="bi bi-trash"></i> Xoá</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty pageData.content}">
                <tr><td colspan="8" class="text-center text-secondary py-4">Không tìm thấy người dùng nào</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
</body>
</html>
