package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * SiteMesh 3: chọn decorator (layout) theo đường dẫn.
 * Decorator nằm ở /WEB-INF/decorators/ (tiền tố mặc định của SiteMesh),
 * tức src/main/resources/META-INF/WEB-INF/decorators/web.jsp, admin.jsp.
 */
public class CustomSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        // Decorator mặc định nếu không có decorator riêng cho đường dẫn
        builder.addDecoratorPath("/*", "web.jsp")
                // Decorator riêng cho trang quản trị
                .addDecoratorPath("/admin/*", "admin.jsp")
                // Các đường dẫn không trang trí
                .addExcludedPath("/login*").addExcludedPath("/login/*")
                .addExcludedPath("/alogin*").addExcludedPath("/alogin/*")
                .addExcludedPath("/static/**")
                .addExcludedPath("/api/**");
    }
}
