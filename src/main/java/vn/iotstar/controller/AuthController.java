package vn.iotstar.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.iotstar.model.RegisterForm;
import vn.iotstar.service.DuplicateFieldException;
import vn.iotstar.service.UserService;

/**
 * Đăng nhập: chỉ có GET /login (trang JSP). POST /login và POST /logout do Spring Security xử lý.
 * Đăng ký: GET/POST /register.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private static boolean isLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/login")
    public String login(Authentication auth) {
        if (isLoggedIn(auth)) {
            return "redirect:/";
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Authentication auth, Model model) {
        if (isLoggedIn(auth)) {
            return "redirect:/";
        }
        model.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form, BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.register(form);
        } catch (DuplicateFieldException e) {
            result.rejectValue(e.getField(), "duplicate", e.getMessage());
            return "auth/register";
        }
        redirect.addFlashAttribute("message", "Đăng ký thành công! Hãy đăng nhập bằng tài khoản " + form.getUsername() + ".");
        return "redirect:/login";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "auth/403";
    }
}
