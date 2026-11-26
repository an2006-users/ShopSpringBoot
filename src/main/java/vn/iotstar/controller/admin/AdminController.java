package vn.iotstar.controller.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.service.CategoryService;
import vn.iotstar.service.UserService;

/** Dashboard quản trị: /admin (chỉ ROLE_ADMIN, xem SecurityConfig). */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final UserService userService;

    public AdminController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping({ "", "/" })
    public String dashboard(Model model) {
        model.addAttribute("categoryCount", categoryService.count());
        model.addAttribute("activeCategoryCount", categoryService.countActive());
        model.addAttribute("userCount", userService.count());
        model.addAttribute("adminCount", userService.countAdmins());
        PageRequest newest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("recentUsers", userService.findAll(newest).getContent());
        model.addAttribute("recentCategories", categoryService.findAll(newest).getContent());
        return "admin/dashboard";
    }
}
