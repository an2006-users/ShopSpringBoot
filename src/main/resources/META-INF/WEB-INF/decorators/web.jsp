<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<%--
    DECORATOR "web" (SiteMesh 3): layout Bootstrap cho trang người dùng.
    Trang nội dung chỉ viết <title> và <body>; SiteMesh chèn vào các thẻ <sitemesh:write .../>.
--%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/> | ShopSpringBoot</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/app.css">
    <sitemesh:write property="head"/>
</head>
<body class="d-flex flex-column min-vh-100 bg-body-tertiary">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${ctx}/"><i class="bi bi-bag-heart"></i> ShopSpringBoot</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav"
                aria-controls="nav" aria-expanded="false" aria-label="Mở menu">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="nav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${ctx}/"><i class="bi bi-house"></i> Trang chủ</a></li>
                <c:if test="${isAdmin}">
                    <li class="nav-item"><a class="nav-link" href="${ctx}/admin"><i class="bi bi-speedometer2"></i> Quản trị</a></li>
                </c:if>
            </ul>
            <div class="d-flex align-items-center gap-2">
                <c:choose>
                    <c:when test="${not empty currentUser}">
                        <span class="text-white-50 small"><i class="bi bi-person-circle"></i> <c:out value="${currentUser}"/></span>
                        <form action="${ctx}/logout" method="post" class="m-0">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <button class="btn btn-sm btn-outline-light" type="submit"><i class="bi bi-box-arrow-right"></i> Đăng xuất</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-sm btn-outline-light" href="${ctx}/login">Đăng nhập</a>
                        <a class="btn btn-sm btn-primary" href="${ctx}/register">Đăng ký</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

<main class="flex-grow-1 py-4">
    <div class="container">
        <sitemesh:write property="body"/>
    </div>
</main>

<footer class="border-top bg-white py-3 mt-auto">
    <div class="container small text-secondary d-flex justify-content-between flex-wrap">
        <span>© 2026 ShopSpringBoot</span>
        <span>Spring Boot 4 • JSP/JSTL • SiteMesh 3 • Bootstrap 5</span>
    </div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
