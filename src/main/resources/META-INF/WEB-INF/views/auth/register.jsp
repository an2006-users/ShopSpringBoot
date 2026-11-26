<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="pageTitle" value="Đăng ký"/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<div class="card shadow-sm border-0 mx-auto auth-card">
    <div class="card-body p-4">
        <h1 class="h4 text-center mb-1">Tạo tài khoản</h1>
        <p class="text-secondary text-center mb-4">Đăng ký để sử dụng hệ thống.</p>

        <%-- form:form tự thêm CSRF token; form:errors hiện lỗi validate từ server --%>
        <form:form action="${ctx}/register" method="post" modelAttribute="form" cssClass="needs-validation" novalidate="novalidate">
            <div class="mb-3">
                <label class="form-label" for="fullname">Họ và tên</label>
                <form:input path="fullname" cssClass="form-control" cssErrorClass="form-control is-invalid"
                            required="required" minlength="2" maxlength="100" autofocus="autofocus"/>
                <form:errors path="fullname" cssClass="invalid-feedback d-block"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="email">Email</label>
                <form:input path="email" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid"
                            required="required" maxlength="100"/>
                <form:errors path="email" cssClass="invalid-feedback d-block"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="username">Tên đăng nhập</label>
                <form:input path="username" cssClass="form-control" cssErrorClass="form-control is-invalid"
                            required="required" pattern="[A-Za-z0-9_]{3,30}" maxlength="30"/>
                <div class="form-text">3-30 ký tự: chữ không dấu, số hoặc _</div>
                <form:errors path="username" cssClass="invalid-feedback d-block"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="password">Mật khẩu</label>
                <form:password path="password" cssClass="form-control" cssErrorClass="form-control is-invalid"
                               required="required" minlength="6" maxlength="50"/>
                <div class="form-text">6-50 ký tự, có ít nhất 1 chữ cái và 1 chữ số</div>
                <form:errors path="password" cssClass="invalid-feedback d-block"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="confirmPassword">Nhập lại mật khẩu</label>
                <form:password path="confirmPassword" cssClass="form-control" cssErrorClass="form-control is-invalid"
                               required="required" data-match="#password"/>
                <form:errors path="confirmPassword" cssClass="invalid-feedback d-block"/>
                <form:errors path="passwordMatched" cssClass="invalid-feedback d-block"/>
            </div>
            <button class="btn btn-primary w-100" type="submit">Đăng ký</button>
        </form:form>
        <p class="text-center small mt-3 mb-0">Đã có tài khoản? <a href="${ctx}/login">Đăng nhập</a></p>
    </div>
</div>

<script src="${ctx}/static/js/validate.js"></script>
</body>
</html>
