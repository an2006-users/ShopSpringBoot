package vn.iotstar.controller.admin;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.DuplicateFieldException;

/**
 * CRUD danh mục (role ADMIN):
 *   GET  /admin/categories/searchpaginated?name=&page=&size= danh sách + tìm kiếm + phân trang
 *   GET  /admin/categories/add                    form thêm
 *   GET  /admin/categories/edit/{id}              form sửa
 *   POST /admin/categories/save                   lưu (thêm / sửa)
 *   POST /admin/categories/delete/{id}            xoá
 */
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private static final String LIST = "admin/category/list";
    private static final String FORM = "admin/category/form";

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /** Không cho client tự gửi ngày tạo. */
    @InitBinder("category")
    void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("createdAt");
    }

    /** Tìm kiếm + phân trang: /admin/categories/searchpaginated?name=&page=&size=  (gõ /admin/categories cũng vào đây) */
    @RequestMapping({ "", "/searchpaginated" })
    public String search(ModelMap model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = Math.max(page.orElse(1), 1);
        int pageSize = size.orElse(PageHelper.DEFAULT_SIZE);
        Pageable pageable = PageHelper.pageable(currentPage, pageSize, Sort.by("name"));

        Page<Category> resultPage;
        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByNameContaining(name.trim(), pageable);
            model.addAttribute("name", name.trim());
        } else {
            resultPage = categoryService.findAll(pageable);
        }
        // trang vượt quá số trang (vd sau khi xoá) -> lấy trang cuối
        if (resultPage.getTotalPages() > 0 && currentPage > resultPage.getTotalPages()) {
            currentPage = resultPage.getTotalPages();
            pageable = PageHelper.pageable(currentPage, pageSize, Sort.by("name"));
            resultPage = StringUtils.hasText(name) ? categoryService.findByNameContaining(name.trim(), pageable)
                    : categoryService.findAll(pageable);
        }

        PageHelper.addPageNumbers(model, resultPage, currentPage);
        model.addAttribute("categoryPage", resultPage);
        return LIST;
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return FORM;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("category", categoryService.findById(id));
            return FORM;
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/categories/searchpaginated";
        }
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("category") Category category, BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return FORM;
        }
        boolean isNew = category.getId() == null;
        try {
            categoryService.save(category);
        } catch (DuplicateFieldException e) {
            result.rejectValue(e.getField(), "duplicate", e.getMessage());
            return FORM;
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/categories/searchpaginated";
        }
        redirect.addFlashAttribute("message",
                (isNew ? "Đã thêm danh mục \"" : "Đã cập nhật danh mục \"") + category.getName() + "\"");
        return "redirect:/admin/categories/searchpaginated";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            RedirectAttributes redirect) {
        try {
            categoryService.delete(id);
            redirect.addFlashAttribute("message", "Đã xoá danh mục #" + id);
        } catch (NoSuchElementException | IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        // quay lại đúng trang / từ khoá đang xem
        redirect.addAttribute("name", name);
        redirect.addAttribute("page", page == null ? 1 : page);
        redirect.addAttribute("size", size == null ? PageHelper.DEFAULT_SIZE : size);
        return "redirect:/admin/categories/searchpaginated";
    }
}
