package vn.iotstar.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Đăng nhập thành công: ADMIN vào /admin, USER về trang chủ.
 * Nếu trước đó người dùng đang mở 1 trang cần đăng nhập thì quay lại đúng trang đó.
 */
public class RoleBasedSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        setDefaultTargetUrl(admin ? "/admin" : "/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
