package vn.iotstar.config;

import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Nạp bean cấu hình JSP cho Tomcat nhúng.
 * Lưu ý Spring Boot 4: TomcatServletWebServerFactory đã chuyển sang package
 * org.springframework.boot.tomcat.servlet (Boot 3 là org.springframework.boot.web.embedded.tomcat).
 */
@Configuration
public class TomcatJSPConfiguration {

    @Bean
    public WebServerFactoryCustomizer<WebServerFactory> staticResourceCustomizer() {
        return factory -> {
            if (factory instanceof TomcatServletWebServerFactory tomcatFactory) {
                tomcatFactory.addContextCustomizers(
                        context -> context.addLifecycleListener(new JSPStaticResourceConfigurer(context)));
            }
        };
    }

    /**
     * View resolver JSP (thay bean mặc định của Spring Boot, vẫn đọc prefix/suffix như application.properties).
     * alwaysInclude = true: bắt buộc khi dùng SiteMesh filter trên Tomcat 11 (Spring Boot 4) –
     * nếu JSP được forward(), Tomcat 11 bỏ qua response wrapper của SiteMesh và trang bị trắng.
     */
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        resolver.setAlwaysInclude(true);
        resolver.setContentType("text/html;charset=UTF-8");
        return resolver;
    }
}
