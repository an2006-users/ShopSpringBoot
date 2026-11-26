<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Quản lý sản phẩm"/>
<c:set var="baseUrl" value="/admin/products/searchpaginated"/>
<c:set var="pageData" value="${productPage}"/>
<c:set var="placeholder" value="Tìm theo tên sản phẩm..."/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
    <h1 class="h3 mb-0">Sản phẩm</h1>
    <a class="btn btn-primary" href="${ctx}/admin/products/add"><i class="bi bi-plus-lg"></i> Thêm sản phẩm</a>
</div>
<%@ include file="/WEB-INF/views/common/alerts.jsp"%>

<div class="card border-0 shadow-sm">
    <div class="card-body"><%@ include file="/WEB-INF/views/common/search-bar.jsp"%></div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead class="table-light"><tr><th>#</th><th></th><th>Tên sản phẩm</th><th>Danh mục</th><th class="text-end">Giá (₫)</th><th>Trạng thái</th><th class="text-end">Thao tác</th></tr></thead>
            <tbody>
            <c:forEach items="${pageData.content}" var="p" varStatus="st">
                <tr>
                    <td>${pageData.number * pageData.size + st.index + 1}</td>
                    <td class="fs-4 text-primary"><i class="bi bi-${empty p.icon ? 'box-seam' : p.icon}"></i></td>
                    <td class="fw-semibold"><c:out value="${p.name}"/></td>
                    <td><c:out value="${p.category.name}"/></td>
                    <td class="text-end"><fmt:formatNumber value="${p.price}" maxFractionDigits="0"/></td>
                    <td><span class="badge ${p.active ? 'text-bg-success' : 'text-bg-secondary'}">${p.active ? 'Đang bán' : 'Ngừng bán'}</span></td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/products/edit/${p.id}"><i class="bi bi-pencil"></i> Sửa</a>
                        <form class="d-inline" method="post" action="${ctx}/admin/products/delete/${p.id}"
                              onsubmit="return confirm('Xoá sản phẩm này?');">
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
                <tr><td colspan="7" class="text-center text-secondary py-4">Không tìm thấy sản phẩm nào</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
</body>
</html>
