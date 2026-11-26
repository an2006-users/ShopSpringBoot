<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<%-- Trang đăng nhập KHÔNG dùng decorator (CustomSiteMeshFilter loại trừ /login*) nên tự viết đủ HTML --%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập | ShopSpringBoot</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/app.css">
</head>
<body class="bg-body-tertiary d-flex align-items-center min-vh-100 py-4">
<div class="container">
    <div class="text-center mb-4">
        <a class="h3 fw-bold text-decoration-none text-body" href="${ctx}/"><i class="bi bi-bag-heart text-primary"></i> ShopSpringBoot</a>
    </div>
    <div class="card shadow-sm border-0 mx-auto auth-card">
        <div class="card-body p-4">
            <h1 class="h4 text-center mb-1">Đăng nhập</h1>
            <p class="text-secondary text-center mb-4">Chào mừng bạn quay lại!</p>

            <c:if test="${not empty message}"><div class="alert alert-success"><c:out value="${message}"/></div></c:if>
            <c:if test="${param.logout != null}"><div class="alert alert-info">Bạn đã đăng xuất.</div></c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger">
                    <c:choose>
                        <c:when test="${fn:containsIgnoreCase(sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message, 'disabled')}">Tài khoản đã bị khoá.</c:when>
                        <c:otherwise>Sai tên đăng nhập hoặc mật khẩu.</c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <form action="${ctx}/login" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="mb-3">
                    <label class="form-label" for="username">Tên đăng nhập</label>
                    <input class="form-control" id="username" name="username" required autofocus autocomplete="username">
                    <div class="invalid-feedback">Vui lòng nhập tên đăng nhập</div>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input class="form-control" id="password" name="password" type="password" required autocomplete="current-password">
                    <div class="invalid-feedback">Vui lòng nhập mật khẩu</div>
                </div>
                <button class="btn btn-primary w-100" type="submit">Đăng nhập</button>
            </form>
            <p class="text-center small mt-3 mb-0">Chưa có tài khoản? <a href="${ctx}/register">Đăng ký</a></p>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/validate.js"></script>
</body>
</html>
