<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="isNew" value="${category.id == null}"/>
<c:set var="pageTitle" value="${isNew ? 'Thêm danh mục' : 'Sửa danh mục'}"/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<nav aria-label="breadcrumb"><ol class="breadcrumb small">
    <li class="breadcrumb-item"><a href="${ctx}/admin/categories/searchpaginated">Danh mục</a></li>
    <li class="breadcrumb-item active">${isNew ? 'Thêm mới' : 'Chỉnh sửa'}</li></ol></nav>

<div class="card border-0 shadow-sm" style="max-width: 720px">
    <div class="card-body p-4">
        <h1 class="h4 mb-3">${pageTitle}<c:if test="${not isNew}"> <small class="text-secondary">#${category.id}</small></c:if></h1>

        <form:form action="${ctx}/admin/categories/save" method="post" modelAttribute="category" cssClass="needs-validation" novalidate="novalidate">
            <form:hidden path="id"/>
            <div class="mb-3">
                <label class="form-label" for="name">Tên danh mục *</label>
                <form:input path="name" cssClass="form-control" cssErrorClass="form-control is-invalid"
                            required="required" minlength="2" maxlength="100" autofocus="autofocus"/>
                <div class="invalid-feedback">Tên danh mục từ 2 đến 100 ký tự</div>
                <form:errors path="name" cssClass="invalid-feedback d-block"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="description">Mô tả</label>
                <form:textarea path="description" rows="3" cssClass="form-control" cssErrorClass="form-control is-invalid" maxlength="500"/>
                <form:errors path="description" cssClass="invalid-feedback d-block"/>
            </div>
            <div class="form-check form-switch mb-4">
                <form:checkbox path="active" cssClass="form-check-input" id="active"/>
                <label class="form-check-label" for="active">Đang hoạt động</label>
            </div>
            <button class="btn btn-primary" type="submit"><i class="bi bi-save"></i> Lưu</button>
            <a class="btn btn-outline-secondary" href="${ctx}/admin/categories/searchpaginated">Huỷ</a>
        </form:form>
    </div>
</div>
<script src="${ctx}/static/js/validate.js"></script>
</body>
</html>
