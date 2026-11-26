<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="isNew" value="${productForm.id == null}"/>
<c:set var="pageTitle" value="${isNew ? 'Thêm sản phẩm' : 'Sửa sản phẩm'}"/>
<%@ include file="/WEB-INF/views/common/vars.jsp"%>
<html>
<head><title><c:out value="${pageTitle}"/></title></head>
<body>

<nav aria-label="breadcrumb"><ol class="breadcrumb small">
    <li class="breadcrumb-item"><a href="${ctx}/admin/products/searchpaginated">Sản phẩm</a></li>
    <li class="breadcrumb-item active">${isNew ? 'Thêm mới' : 'Chỉnh sửa'}</li></ol></nav>

<div class="card border-0 shadow-sm" style="max-width: 760px">
    <div class="card-body p-4">
        <h1 class="h4 mb-3">${pageTitle}<c:if test="${not isNew}"> <small class="text-secondary">#${productForm.id}</small></c:if></h1>
        <%@ include file="/WEB-INF/views/common/alerts.jsp"%>

        <form:form action="${ctx}/admin/products/save" method="post" modelAttribute="productForm" cssClass="needs-validation" novalidate="novalidate">
            <form:hidden path="id"/>
            <div class="row g-3">
                <div class="col-md-8">
                    <label class="form-label" for="name">Tên sản phẩm *</label>
                    <form:input path="name" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                required="required" minlength="2" maxlength="150" autofocus="autofocus"/>
                    <div class="invalid-feedback">Tên sản phẩm từ 2 đến 150 ký tự</div>
                    <form:errors path="name" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-4">
                    <label class="form-label" for="price">Giá (₫) *</label>
                    <form:input path="price" type="number" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                required="required" min="0" max="1000000000" step="1000"/>
                    <div class="invalid-feedback">Nhập giá từ 0 (bước 1.000)</div>
                    <form:errors path="price" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="categoryId">Danh mục *</label>
                    <form:select path="categoryId" cssClass="form-select" cssErrorClass="form-select is-invalid" required="required">
                        <form:option value="" label="-- Chọn danh mục --"/>
                        <form:options items="${allCategories}" itemValue="id" itemLabel="name"/>
                    </form:select>
                    <div class="invalid-feedback">Vui lòng chọn danh mục</div>
                    <form:errors path="categoryId" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="icon">Icon</label>
                    <div class="input-group">
                        <span class="input-group-text"><i id="iconPreview" class="bi bi-${empty productForm.icon ? 'box-seam' : productForm.icon}"></i></span>
                        <form:input path="icon" cssClass="form-control" cssErrorClass="form-control is-invalid"
                                    maxlength="50" pattern="[a-z0-9\-]{1,50}" placeholder="phone, laptop, book..."/>
                    </div>
                    <div class="form-text">Tên icon trong <a href="https://icons.getbootstrap.com" target="_blank" rel="noopener">Bootstrap Icons</a> (bỏ chữ "bi-"). Để trống = box-seam.</div>
                    <form:errors path="icon" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-12">
                    <label class="form-label" for="description">Mô tả</label>
                    <form:textarea path="description" rows="3" cssClass="form-control" cssErrorClass="form-control is-invalid" maxlength="1000"/>
                    <form:errors path="description" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="col-12">
                    <div class="form-check form-switch">
                        <form:checkbox path="active" cssClass="form-check-input" id="active"/>
                        <label class="form-check-label" for="active">Đang bán (hiện ở trang chủ)</label>
                    </div>
                </div>
            </div>
            <div class="mt-4">
                <button class="btn btn-primary" type="submit"><i class="bi bi-save"></i> Lưu</button>
                <a class="btn btn-outline-secondary" href="${ctx}/admin/products/searchpaginated">Huỷ</a>
            </div>
        </form:form>
    </div>
</div>
<script>
    // xem trước icon khi gõ tên
    document.getElementById("icon").addEventListener("input", function () {
        document.getElementById("iconPreview").className = "bi bi-" + (this.value.trim() || "box-seam");
    });
</script>
<script src="${ctx}/static/js/validate.js"></script>
</body>
</html>
