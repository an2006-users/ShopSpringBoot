<%@ page pageEncoding="UTF-8"%>
<%--
    Phân trang dùng chung (theo mẫu searchpaginated).
    Cần: pageData (Page), pageNumbers (List<Integer> do controller tính), baseUrl, name,
         categoryId (tuỳ chọn – trang chủ)
--%>
<c:set var="cur" value="${pageData.number + 1}"/>
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mt-3">
    <div class="small text-secondary">
        <c:if test="${pageData.totalElements > 0}">
            Hiển thị ${pageData.number * pageData.size + 1}–${pageData.number * pageData.size + pageData.numberOfElements}
            / ${pageData.totalElements} bản ghi
        </c:if>
    </div>
    <c:if test="${pageData.totalPages > 1}">
        <nav aria-label="Phân trang">
            <ul class="pagination pagination-sm mb-0">
                <c:url var="prevUrl" value="${baseUrl}"><c:param name="name" value="${name}"/><c:param name="size" value="${pageData.size}"/><c:if test="${not empty categoryId}"><c:param name="categoryId" value="${categoryId}"/></c:if><c:param name="page" value="${cur - 1}"/></c:url>
                <li class="page-item ${cur == 1 ? 'disabled' : ''}"><a class="page-link" href="${prevUrl}" aria-label="Trang trước">‹</a></li>
                <c:forEach items="${pageNumbers}" var="pageNumber">
                    <c:url var="pUrl" value="${baseUrl}"><c:param name="name" value="${name}"/><c:param name="size" value="${pageData.size}"/><c:if test="${not empty categoryId}"><c:param name="categoryId" value="${categoryId}"/></c:if><c:param name="page" value="${pageNumber}"/></c:url>
                    <li class="${pageNumber == pageData.number + 1 ? 'page-item active' : 'page-item'}">
                        <a class="page-link" href="${pUrl}">${pageNumber}</a>
                    </li>
                </c:forEach>
                <c:url var="nextUrl" value="${baseUrl}"><c:param name="name" value="${name}"/><c:param name="size" value="${pageData.size}"/><c:if test="${not empty categoryId}"><c:param name="categoryId" value="${categoryId}"/></c:if><c:param name="page" value="${cur + 1}"/></c:url>
                <li class="page-item ${cur == pageData.totalPages ? 'disabled' : ''}"><a class="page-link" href="${nextUrl}" aria-label="Trang sau">›</a></li>
            </ul>
        </nav>
    </c:if>
</div>
