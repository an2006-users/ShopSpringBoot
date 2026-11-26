<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Quản lý danh mục"/>
<c:set var="baseUrl" value="/admin/categories/searchpaginated"/>
<c:set var="pageData" value="${categoryPage}"/>
<c:set var="placeholder" value="Tìm theo tên danh mục..."/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
    <h1 class="h3 mb-0">Danh mục</h1>
    <a class="btn btn-primary" href="${ctx}/admin/categories/add"><i class="bi bi-plus-lg"></i> Thêm danh mục</a>
</div>
<%@ include file="/WEB-INF/views/common/alerts.jsp"%>

<div class="card border-0 shadow-sm">
    <div class="card-body"><%@ include file="/WEB-INF/views/common/search-bar.jsp"%></div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead class="table-light"><tr><th>#</th><th>ID</th><th>Tên danh mục</th><th>Mô tả</th><th>Trạng thái</th><th class="text-end">Thao tác</th></tr></thead>
            <tbody>
            <c:forEach items="${pageData.content}" var="cat" varStatus="st">
                <tr>
                    <td>${pageData.number * pageData.size + st.index + 1}</td>
                    <td>${cat.id}</td>
                    <td class="fw-semibold"><c:out value="${cat.name}"/></td>
                    <td class="text-secondary"><c:out value="${cat.description}"/></td>
                    <td><span class="badge ${cat.active ? 'text-bg-success' : 'text-bg-secondary'}">${cat.active ? 'Hoạt động' : 'Khoá'}</span></td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/categories/edit/${cat.id}"><i class="bi bi-pencil"></i> Sửa</a>
                        <form class="d-inline" method="post" action="${ctx}/admin/categories/delete/${cat.id}"
                              onsubmit="return confirm('Xoá danh mục này?');">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <input type="hidden" name="name" value="<c:out value='${name}'/>">
                            <input type="hidden" name="page" value="${pageData.number + 1}">
                            <input type="hidden" name="size" value="${pageData.size}">
                            <button class="btn btn-sm btn-outline-danger" type="submit"><i class="bi bi-trash"></i> Xoá</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty pageData.content}">
                <tr><td colspan="6" class="text-center text-secondary py-4">Không tìm thấy danh mục nào</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
</body>
</html>
