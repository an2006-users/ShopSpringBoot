package vn.iotstar.controller.admin;

import java.util.List;
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
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.ProductForm;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.DuplicateFieldException;
import vn.iotstar.service.ProductService;

/**
 * CRUD sản phẩm (role ADMIN):
 *   GET  /admin/products/searchpaginated?name=&page=&size= danh sách + tìm theo tên + phân trang
 *   GET  /admin/products/add | /edit/{id}       form
 *   POST /admin/products/save                   lưu
 *   POST /admin/products/delete/{id}            xoá
 */
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private static final String LIST = "admin/product/list";
    private static final String FORM = "admin/product/form";

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /** Danh sách danh mục cho ô chọn trong form (lấy tất cả, kể cả đang khoá). */
    @ModelAttribute("allCategories")
    List<Category> allCategories() {
        return categoryService.findAll(org.springframework.data.domain.PageRequest.of(0, 500, Sort.by("name"))).getContent();
    }

    /** Tìm kiếm + phân trang: /admin/products/searchpaginated?name=&page=&size=  (gõ /admin/products cũng vào đây) */
    @RequestMapping({ "", "/searchpaginated" })
    public String search(ModelMap model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = Math.max(page.orElse(1), 1);
        int pageSize = size.orElse(PageHelper.DEFAULT_SIZE);
        Pageable pageable = PageHelper.pageable(currentPage, pageSize, Sort.by("name"));

        Page<Product> resultPage;
        if (StringUtils.hasText(name)) {
            resultPage = productService.findByNameContaining(name.trim(), pageable);
            model.addAttribute("name", name.trim());
        } else {
            resultPage = productService.findAll(pageable);
        }
        // trang vượt quá số trang (vd sau khi xoá) -> lấy trang cuối
        if (resultPage.getTotalPages() > 0 && currentPage > resultPage.getTotalPages()) {
            currentPage = resultPage.getTotalPages();
            pageable = PageHelper.pageable(currentPage, pageSize, Sort.by("name"));
            resultPage = StringUtils.hasText(name) ? productService.findByNameContaining(name.trim(), pageable)
                    : productService.findAll(pageable);
        }

        PageHelper.addPageNumbers(model, resultPage, currentPage);
        model.addAttribute("productPage", resultPage);
        return LIST;
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return FORM;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("productForm", ProductForm.from(productService.findById(id)));
            return FORM;
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/products/searchpaginated";
        }
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("productForm") ProductForm form, BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return FORM;
        }
        boolean isNew = form.getId() == null;
        try {
            productService.save(form);
        } catch (DuplicateFieldException e) {
            result.rejectValue(e.getField(), "invalid", e.getMessage());
            return FORM;
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/products/searchpaginated";
        }
        redirect.addFlashAttribute("message",
                (isNew ? "Đã thêm sản phẩm \"" : "Đã cập nhật sản phẩm \"") + form.getName() + "\"");
        return "redirect:/admin/products/searchpaginated";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            RedirectAttributes redirect) {
        try {
            productService.delete(id);
            redirect.addFlashAttribute("message", "Đã xoá sản phẩm #" + id);
        } catch (NoSuchElementException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        redirect.addAttribute("name", name);
        redirect.addAttribute("page", page == null ? 1 : page);
        redirect.addAttribute("size", size == null ? PageHelper.DEFAULT_SIZE : size);
        return "redirect:/admin/products/searchpaginated";
    }
}
