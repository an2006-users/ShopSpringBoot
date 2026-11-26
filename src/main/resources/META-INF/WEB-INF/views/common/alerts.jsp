<%@ page pageEncoding="UTF-8"%>
<%-- Hiển thông báo --%>
<c:if test="${message != null}">
    <div class="alert alert-success alert-dismissible fade show"><i class="bi bi-check-circle"></i> <c:out value="${message}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button></div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show"><i class="bi bi-exclamation-triangle"></i> <c:out value="${error}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button></div>
</c:if>
<%-- Hết thông báo --%>
