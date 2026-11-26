package vn.iotstar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

/**
 * Spring Security:
 *  - /login, /register, /assets/** : ai cũng vào được
 *  - /admin/**                     : chỉ ROLE_ADMIN
 *  - còn lại                       : phải đăng nhập
 *  - Đăng nhập bằng form JSP tự viết (/login), đăng xuất POST /logout (có CSRF)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Controller trả về JSP bằng FORWARD -> phải cho qua, nếu không JSP bị chặn
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR, DispatcherType.INCLUDE).permitAll()
                .requestMatchers("/login", "/register", "/static/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")              // GET: trang JSP của mình
                .loginProcessingUrl("/login")     // POST: Spring Security tự kiểm tra
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new RoleBasedSuccessHandler())
                .failureUrl("/login?error")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll())
            .exceptionHandling(ex -> ex.accessDeniedPage("/403"));
        return http.build();
    }
}
