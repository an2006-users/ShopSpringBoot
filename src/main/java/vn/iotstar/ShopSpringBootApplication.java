package vn.iotstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import vn.iotstar.config.CustomSiteMeshFilter;

/**
 * Chạy: Run file này trong IntelliJ, hoặc "mvn spring-boot:run" -> http://localhost:8090
 */
@SpringBootApplication
@Configuration
public class ShopSpringBootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShopSpringBootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShopSpringBootApplication.class, args);
    }

    /**
     * Định nghĩa SiteMesh filter trong Spring.
     * Tên bean "sitemesh" để starter SiteMesh không đăng ký thêm filter thứ hai.
     */
    @Bean(name = "sitemesh")
    FilterRegistrationBean<CustomSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<CustomSiteMeshFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CustomSiteMeshFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /** Hiển thị Unicode (tiếng Việt) trên Spring Boot 4. */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
}
