<%@ page pageEncoding="UTF-8"%>
<%--
    Ô tìm kiếm + "Page size" (theo mẫu searchpaginated).
    Cần: baseUrl (vd /admin/categories/searchpaginated), name, sizes, pageData, placeholder
--%>
<form class="row g-2 align-items-center" action="${ctx}${baseUrl}" method="get" role="search">
    <div class="col-sm-7 col-md-6">
        <div class="input-group">
            <span class="input-group-text"><i class="bi bi-search"></i></span>
            <input type="text" class="form-control" name="name" id="name" value="<c:out value='${name}'/>"
                   placeholder="<c:out value='${placeholder}'/>">
            <button class="btn btn-primary" type="submit">Search</button>
        </div>
    </div>
    <div class="col-auto d-flex align-items-center gap-2">
        <label for="size" class="small text-secondary text-nowrap">Page size:</label>
        <select class="form-select" name="size" id="size" onchange="this.form.submit()">
            <c:forEach items="${sizes}" var="s">
                <option ${pageData.size == s ? 'selected' : ''} value="${s}">${s}</option>
            </c:forEach>
        </select>
    </div>
    <c:if test="${not empty name}">
        <div class="col-auto"><a class="btn btn-link" href="${ctx}${baseUrl}">Xoá lọc</a></div>
    </c:if>
</form>
