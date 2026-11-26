package vn.iotstar.service.impl;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.model.RegisterForm;
import vn.iotstar.model.UserForm;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.DuplicateFieldException;
import vn.iotstar.service.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(RegisterForm form) {
        if (userRepository.existsByUsernameIgnoreCase(form.getUsername())) {
            throw new DuplicateFieldException("username", "Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmailIgnoreCase(form.getEmail())) {
            throw new DuplicateFieldException("email", "Email đã được sử dụng");
        }
        User user = new User();
        user.setUsername(form.getUsername());
        user.setFullname(form.getFullname());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public Page<User> findByNameContaining(String keyword, Pageable page) {
        return userRepository.findByNameContaining(keyword, page);
    }

    @Override
    public Page<User> findAll(Pageable page) {
        return userRepository.findAll(page);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng #" + id));
    }

    @Override
    @Transactional
    public User save(UserForm form, String currentUsername) {
        boolean isNew = form.getId() == null;

        boolean dupUsername = isNew ? userRepository.existsByUsernameIgnoreCase(form.getUsername())
                : userRepository.existsByUsernameIgnoreCaseAndIdNot(form.getUsername(), form.getId());
        if (dupUsername) {
            throw new DuplicateFieldException("username", "Tên đăng nhập đã tồn tại");
        }
        boolean dupEmail = isNew ? userRepository.existsByEmailIgnoreCase(form.getEmail())
                : userRepository.existsByEmailIgnoreCaseAndIdNot(form.getEmail(), form.getId());
        if (dupEmail) {
            throw new DuplicateFieldException("email", "Email đã được sử dụng");
        }

        User user;
        if (isNew) {
            if (form.getPassword() == null || form.getPassword().isEmpty()) {
                throw new DuplicateFieldException("password", "Vui lòng nhập mật khẩu cho tài khoản mới");
            }
            user = new User();
        } else {
            user = findById(form.getId());
            boolean self = user.getUsername().equalsIgnoreCase(currentUsername);
            if (self && (form.getRole() != Role.ADMIN || !form.isEnabled())) {
                throw new IllegalStateException("Bạn không thể tự hạ quyền hoặc tự khoá tài khoản đang đăng nhập");
            }
            if (user.getRole() == Role.ADMIN && form.getRole() != Role.ADMIN
                    && userRepository.countByRole(Role.ADMIN) <= 1) {
                throw new IllegalStateException("Phải còn ít nhất 1 tài khoản ADMIN");
            }
        }

        user.setUsername(form.getUsername());
        user.setFullname(form.getFullname());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone() == null || form.getPhone().isBlank() ? null : form.getPhone());
        user.setRole(form.getRole());
        user.setEnabled(form.isEnabled());
        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id, String currentUsername) {
        User user = findById(id);
        if (user.getUsername().equalsIgnoreCase(currentUsername)) {
            throw new IllegalStateException("Không thể xoá tài khoản đang đăng nhập");
        }
        if (user.getRole() == Role.ADMIN && userRepository.countByRole(Role.ADMIN) <= 1) {
            throw new IllegalStateException("Không thể xoá ADMIN cuối cùng");
        }
        userRepository.delete(user);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public long countAdmins() {
        return userRepository.countByRole(Role.ADMIN);
    }
}
