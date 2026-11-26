<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Trang chủ"/>
<c:set var="baseUrl" value="/"/>
<c:set var="pageData" value="${productPage}"/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<div class="d-flex flex-wrap justify-content-between align-items-end gap-2 mb-3">
    <div>
        <h1 class="h3 mb-1">Xin chào, <c:out value="${currentUser}"/>!</h1>
        <p class="text-secondary mb-0">Khám phá các sản phẩm đang bán.</p>
    </div>
    <c:if test="${isAdmin}">
        <a class="btn btn-outline-primary" href="${ctx}/admin"><i class="bi bi-speedometer2"></i> Vào trang quản trị</a>
    </c:if>
</div>

<%-- Tìm kiếm theo tên sản phẩm (giữ danh mục đang lọc) --%>
<form class="row g-2 mb-3" action="${ctx}/" method="get" role="search">
    <c:if test="${not empty categoryId}"><input type="hidden" name="categoryId" value="${categoryId}"></c:if>
    <div class="col-md-6">
        <div class="input-group">
            <span class="input-group-text"><i class="bi bi-search"></i></span>
            <input type="text" class="form-control" name="name" value="<c:out value='${name}'/>" placeholder="Tìm sản phẩm...">
            <button class="btn btn-primary" type="submit">Tìm</button>
        </div>
    </div>
</form>

<%-- Lọc theo danh mục --%>
<div class="d-flex flex-wrap gap-2 mb-4">
    <c:url var="allUrl" value="/"><c:param name="name" value="${name}"/></c:url>
    <a class="btn btn-sm ${empty categoryId ? 'btn-dark' : 'btn-outline-secondary'}" href="${allUrl}">Tất cả</a>
    <c:forEach items="${categories}" var="cat">
        <c:url var="catUrl" value="/"><c:param name="name" value="${name}"/><c:param name="categoryId" value="${cat.id}"/></c:url>
        <a class="btn btn-sm ${cat.id == categoryId ? 'btn-dark' : 'btn-outline-secondary'}" href="${catUrl}"><c:out value="${cat.name}"/></a>
    </c:forEach>
</div>

<c:choose>
    <c:when test="${empty pageData.content}">
        <div class="alert alert-light border text-center py-5">
            <i class="bi bi-box-seam fs-1 d-block mb-2 text-secondary"></i>
            Không tìm thấy sản phẩm nào.
        </div>
    </c:when>
    <c:otherwise>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-4 g-3">
            <c:forEach items="${pageData.content}" var="p">
                <div class="col">
                    <div class="card h-100 border-0 shadow-sm product-card">
                        <div class="product-thumb"><i class="bi bi-${empty p.icon ? 'box-seam' : p.icon}"></i></div>
                        <div class="card-body d-flex flex-column">
                            <span class="badge text-bg-light border align-self-start mb-2"><c:out value="${p.category.name}"/></span>
                            <h2 class="h6 card-title"><c:out value="${p.name}"/></h2>
                            <p class="small text-secondary flex-grow-1 mb-2"><c:out value="${p.description}"/></p>
                            <div class="fs-5 fw-bold text-danger">
                                <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> ₫
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
</body>
</html>
