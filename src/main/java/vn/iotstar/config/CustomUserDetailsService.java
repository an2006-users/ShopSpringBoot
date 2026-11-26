package vn.iotstar.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.iotstar.repository.UserRepository;

/** Spring Security gọi lớp này để lấy tài khoản từ bảng users khi đăng nhập. */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.iotstar.entity.User u = userRepository.findByUsernameIgnoreCase(username == null ? "" : username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole().name())     // -> ROLE_ADMIN / ROLE_USER
                .disabled(!u.isEnabled())
                .build();
    }
}
