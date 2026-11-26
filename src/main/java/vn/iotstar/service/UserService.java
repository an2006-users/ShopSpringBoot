package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.User;
import vn.iotstar.model.RegisterForm;
import vn.iotstar.model.UserForm;

public interface UserService {

    /**
     * Đăng ký tài khoản mới với vai trò USER.
     *
     * @throws DuplicateFieldException username / email đã tồn tại
     */
    User register(RegisterForm form);

    // ----------------------------------------------------- quản trị (admin)

    /** Tìm theo username / họ tên / email. */
    Page<User> findByNameContaining(String keyword, Pageable page);

    Page<User> findAll(Pageable page);

    /** @throws java.util.NoSuchElementException không tồn tại */
    User findById(Long id);

    /**
     * Thêm (form.id null) hoặc sửa. Mật khẩu trống khi sửa = giữ mật khẩu cũ.
     *
     * @param currentUsername admin đang đăng nhập (không tự hạ quyền / tự khoá mình)
     * @throws DuplicateFieldException username / email trùng
     * @throws IllegalStateException   vi phạm quy tắc (tự khoá mình, bỏ admin cuối cùng...)
     */
    User save(UserForm form, String currentUsername);

    /** @throws IllegalStateException không cho xoá chính mình / admin cuối cùng */
    void delete(Long id, String currentUsername);

    long count();

    long countAdmins();
}
