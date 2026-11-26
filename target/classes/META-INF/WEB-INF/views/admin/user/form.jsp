<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="isNew" value="${userForm.id == null}"/>
<c:set var="pageTitle" value="${isNew ? 'Thêm người dùng' : 'Sửa người dùng'}"/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<nav aria-label="breadcrumb"><ol class="breadcrumb small">
    <li class="breadcrumb-item"><a href="${ctx}/admin/users/searchpaginated">Người dùng</a></li>
    <li class="breadcrumb-item active">${isNew ? 'Thêm mới' : 'Chỉnh sửa'}</li></ol></nav>

<div class="card border-0 shadow-sm" style="max-width: 760px">
    <div class="card-body p-4">
        <h1 class="h4 mb-3">${pageTitle}<c:if test="${not isNew}"> <small class="text-secondary">#${userForm.id}</small></c:if></h1>
        <%@ include file="/WEB-INF/views/common/alerts.jsp"%>

        <form:form action="${ctx}/admin/users/save" method="post" modelAttribute="userForm" cssClass="needs-validation" novalidate="novalidate">
            <form:hidden path="id"/>
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label" for="username">Tên đăng nhập *</label>
                    <form:input path="username" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                required="required" pattern="[A-Za-z0-9_]{3,30}" maxlength="30"/>
                    <div class="invalid-feedback">3-30 ký tự: chữ không dấu, số hoặc _</div>
                    <form:errors path="username" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="fullname">Họ tên *</label>
                    <form:input path="fullname" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                required="required" minlength="2" maxlength="100"/>
                    <div class="invalid-feedback">Họ tên từ 2 đến 100 ký tự</div>
                    <form:errors path="fullname" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="email">Email *</label>
                    <form:input path="email" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                required="required" maxlength="100"/>
                    <div class="invalid-feedback">Email không đúng định dạng</div>
                    <form:errors path="email" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="phone">Số điện thoại</label>
                    <form:input path="phone" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                pattern="(0|\+84)(3|5|7|8|9)[0-9]{8}" maxlength="15" placeholder="0912345678"/>
                    <div class="invalid-feedback">Số điện thoại không hợp lệ</div>
                    <form:errors path="phone" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="password">Mật khẩu ${isNew ? '*' : ''}</label>
                    <c:choose>
                        <c:when test="${isNew}">
                            <form:password path="password" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                           required="required" minlength="6" maxlength="50" autocomplete="new-password"/>
                        </c:when>
                        <c:otherwise>
                            <form:password path="password" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                           minlength="6" maxlength="50" autocomplete="new-password" placeholder="Để trống = giữ mật khẩu cũ"/>
                        </c:otherwise>
                    </c:choose>
                    <div class="form-text">6-50 ký tự, có chữ và số</div>
                    <form:errors path="password" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-3">
                    <label class="form-label" for="role">Vai trò *</label>
                    <form:select path="role" cssClass="form-select" cssErrorClass="form-select is-invalid">
                        <form:options items="${roles}"/>
                    </form:select>
                    <form:errors path="role" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-3 d-flex align-items-end">
                    <div class="form-check form-switch mb-2">
                        <form:checkbox path="enabled" cssClass="form-check-input" id="enabled"/>
                        <label class="form-check-label" for="enabled">Cho phép đăng nhập</label>
                    </div>
                </div>
            </div>
            <div class="mt-4">
                <button class="btn btn-primary" type="submit"><i class="bi bi-save"></i> Lưu</button>
                <a class="btn btn-outline-secondary" href="${ctx}/admin/users/searchpaginated">Huỷ</a>
            </div>
        </form:form>
    </div>
</div>
<script src="${ctx}/static/js/validate.js"></script>
</body>
</html>
