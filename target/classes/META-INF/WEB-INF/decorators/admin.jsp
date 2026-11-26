<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<%-- DECORATOR "admin" (SiteMesh 3): layout trang quản trị /admin/* (navbar + sidebar) --%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/> | Shop Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/app.css">
    <sitemesh:write property="head"/>
</head>
<body class="bg-body-tertiary">
<nav class="navbar navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container-fluid">
        <button class="btn btn-outline-light d-lg-none me-2" type="button" data-bs-toggle="offcanvas"
                data-bs-target="#adminSidebar" aria-controls="adminSidebar" aria-label="Mở menu">
            <i class="bi bi-list"></i>
        </button>
        <a class="navbar-brand fw-bold me-auto" href="${ctx}/admin"><i class="bi bi-bag-heart"></i> ShopSpringBoot <small class="text-secondary fw-normal">ADMIN</small></a>
        <div class="d-flex align-items-center gap-2">
            <span class="text-white-50 small d-none d-sm-inline"><i class="bi bi-person-circle"></i> <c:out value="${currentUser}"/></span>
            <form action="${ctx}/logout" method="post" class="m-0">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button class="btn btn-sm btn-outline-light" type="submit"><i class="bi bi-box-arrow-right"></i> Đăng xuất</button>
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <aside class="col-lg-2 p-0 bg-white border-end admin-sidebar">
            <div class="offcanvas-lg offcanvas-start" tabindex="-1" id="adminSidebar" aria-labelledby="adminSidebarLabel">
                <div class="offcanvas-header">
                    <h5 class="offcanvas-title" id="adminSidebarLabel">Menu</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" data-bs-target="#adminSidebar" aria-label="Đóng"></button>
                </div>
                <div class="offcanvas-body p-3">
                    <ul class="nav nav-pills flex-column gap-1 w-100">
                        <li class="nav-item"><a class="nav-link" data-nav="/admin" href="${ctx}/admin"><i class="bi bi-speedometer2 me-2"></i>Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" data-nav="/admin/categories" href="${ctx}/admin/categories/searchpaginated"><i class="bi bi-tags me-2"></i>Danh mục</a></li>
                        <li class="nav-item"><a class="nav-link" data-nav="/admin/products" href="${ctx}/admin/products/searchpaginated"><i class="bi bi-box-seam me-2"></i>Sản phẩm</a></li>
                        <li class="nav-item"><a class="nav-link" data-nav="/admin/users" href="${ctx}/admin/users/searchpaginated"><i class="bi bi-people me-2"></i>Người dùng</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/"><i class="bi bi-house me-2"></i>Về trang chủ</a></li>
                    </ul>
                </div>
            </div>
        </aside>
        <main class="col-lg-10 px-3 px-lg-4 py-4">
            <sitemesh:write property="body"/>
            <footer class="pt-4 mt-4 border-top small text-secondary">© 2026 ShopSpringBoot Admin • SiteMesh 3 + Bootstrap 5</footer>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Tô sáng menu đang mở (khớp đường dẫn dài nhất)
    (function () {
        var path = location.pathname, best = null;
        document.querySelectorAll("[data-nav]").forEach(function (a) {
            var key = a.getAttribute("data-nav");
            if (path.indexOf(key) >= 0 && (!best || key.length > best.key.length)) best = { a: a, key: key };
        });
        if (best) best.a.classList.add("active");
    })();
</script>
</body>
</html>
