package vn.iotstar.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;

/**
 * Form thêm / sửa người dùng trong trang admin.
 * Mật khẩu: bắt buộc khi thêm; khi sửa để trống = giữ mật khẩu cũ (kiểm tra ở controller).
 */
public class UserForm {

    private Long id;

    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    @Pattern(regexp = "^[A-Za-z0-9_]{3,30}$", message = "Tên đăng nhập 3-30 ký tự: chữ không dấu, số hoặc _")
    private String username;

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(min = 2, max = 100, message = "Họ tên từ 2 đến 100 ký tự")
    private String fullname;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @Pattern(regexp = "^$|^(0|\\+84)(3|5|7|8|9)\\d{8}$", message = "Số điện thoại không hợp lệ (vd: 0912345678)")
    private String phone;

    /** Rỗng hoặc 6-50 ký tự có chữ + số. */
    @Pattern(regexp = "^$|^(?=.*[A-Za-z])(?=.*\\d).{6,50}$", message = "Mật khẩu 6-50 ký tự, có ít nhất 1 chữ cái và 1 chữ số")
    private String password;

    @NotNull(message = "Vui lòng chọn vai trò")
    private Role role = Role.USER;

    private boolean enabled = true;

    public static UserForm from(User u) {
        UserForm f = new UserForm();
        f.id = u.getId();
        f.username = u.getUsername();
        f.fullname = u.getFullname();
        f.email = u.getEmail();
        f.phone = u.getPhone();
        f.role = u.getRole();
        f.enabled = u.isEnabled();
        return f;
    }

    public boolean isNew() { return id == null; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username == null ? null : username.trim(); }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname == null ? null : fullname.trim(); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email == null ? null : email.trim().toLowerCase(); }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone == null ? "" : phone.replaceAll("[\\s.\\-]", ""); }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password == null ? "" : password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
