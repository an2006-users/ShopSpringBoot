package vn.iotstar.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Dữ liệu form đăng ký (validate bằng Bean Validation). */
public class RegisterForm {

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(min = 2, max = 100, message = "Họ tên từ 2 đến 100 ký tự")
    private String fullname;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    @Pattern(regexp = "^[A-Za-z0-9_]{3,30}$", message = "Tên đăng nhập 3-30 ký tự: chữ không dấu, số hoặc _")
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{6,50}$", message = "Mật khẩu 6-50 ký tự, có ít nhất 1 chữ cái và 1 chữ số")
    private String password;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu")
    private String confirmPassword;

    @AssertTrue(message = "Mật khẩu nhập lại không khớp")
    public boolean isPasswordMatched() {
        return password == null || confirmPassword == null || confirmPassword.isEmpty()
                || password.equals(confirmPassword);
    }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname == null ? null : fullname.trim(); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email == null ? null : email.trim().toLowerCase(); }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username == null ? null : username.trim(); }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
