package vn.iotstar.controller.admin;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.model.UserForm;
import vn.iotstar.service.DuplicateFieldException;
import vn.iotstar.service.UserService;

/**
 * CRUD người dùng (role ADMIN):
 *   GET  /admin/users/searchpaginated?name=&page=&size= danh sách + tìm kiếm (username / họ tên / email) + phân trang
 *   GET  /admin/users/add | /edit/{id}       form
 *   POST /admin/users/save                   lưu
 *   POST /admin/users/delete/{id}            xoá
 */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private static final String LIST = "admin/user/list";
    private static final String FORM = "admin/user/form";

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("roles")
    Role[] roles() {
        return Role.values();
    }

    /** Tìm kiếm + phân trang: /admin/users/searchpaginated?name=&page=&size=  (gõ /admin/users cũng vào đây) */
    @RequestMapping({ "", "/searchpaginated" })
    public String search(ModelMap model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = Math.max(page.orElse(1), 1);
        int pageSize = size.orElse(PageHelper.DEFAULT_SIZE);
        Pageable pageable = PageHelper.pageable(currentPage, pageSize, Sort.by("username"));

        Page<User> resultPage;
        if (StringUtils.hasText(name)) {
            resultPage = userService.findByNameContaining(name.trim(), pageable);
            model.addAttribute("name", name.trim());
        } else {
            resultPage = userService.findAll(pageable);
        }
        // trang vượt quá số trang (vd sau khi xoá) -> lấy trang cuối
        if (resultPage.getTotalPages() > 0 && currentPage > resultPage.getTotalPages()) {
            currentPage = resultPage.getTotalPages();
            pageable = PageHelper.pageable(currentPage, pageSize, Sort.by("username"));
            resultPage = StringUtils.hasText(name) ? userService.findByNameContaining(name.trim(), pageable)
                    : userService.findAll(pageable);
        }

        PageHelper.addPageNumbers(model, resultPage, currentPage);
        model.addAttribute("userPage", resultPage);
        return LIST;
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("userForm", new UserForm());
        return FORM;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("userForm", UserForm.from(userService.findById(id)));
            return FORM;
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/searchpaginated";
        }
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userForm") UserForm form, BindingResult result,
            Principal principal, Model model, RedirectAttributes redirect) {
        if (form.isNew() && (form.getPassword() == null || form.getPassword().isEmpty())) {
            result.rejectValue("password", "required", "Vui lòng nhập mật khẩu cho tài khoản mới");
        }
        if (result.hasErrors()) {
            return FORM;
        }
        try {
            userService.save(form, principal.getName());
        } catch (DuplicateFieldException e) {
            result.rejectValue(e.getField(), "duplicate", e.getMessage());
            return FORM;
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return FORM;
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/searchpaginated";
        }
        redirect.addFlashAttribute("message",
                (form.isNew() ? "Đã thêm người dùng \"" : "Đã cập nhật người dùng \"") + form.getUsername() + "\"");
        return "redirect:/admin/users/searchpaginated";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            RedirectAttributes redirect) {
        try {
            userService.delete(id, principal.getName());
            redirect.addFlashAttribute("message", "Đã xoá người dùng #" + id);
        } catch (IllegalStateException | NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        redirect.addAttribute("name", name);
        redirect.addAttribute("page", page == null ? 1 : page);
        redirect.addAttribute("size", size == null ? PageHelper.DEFAULT_SIZE : size);
        return "redirect:/admin/users/searchpaginated";
    }
}
