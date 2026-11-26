package vn.iotstar.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.controller.admin.PageHelper;
import vn.iotstar.entity.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;

/**
 * Trang chủ người dùng: sản phẩm đang bán
 *   /?name=&categoryId=&page=   (lọc theo danh mục, tìm theo tên, phân trang 8 sản phẩm/trang)
 */
@Controller
public class HomeController {

    private static final int PAGE_SIZE = 8;

    private final ProductService productService;
    private final CategoryService categoryService;

    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({ "/", "/home" })
    public String home(ModelMap model,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam("page") Optional<Integer> page) {
        int currentPage = Math.max(page.orElse(1), 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Page<Product> resultPage = productService.searchForShop(name, categoryId, PageRequest.of(currentPage - 1, PAGE_SIZE, sort));
        if (resultPage.getTotalPages() > 0 && currentPage > resultPage.getTotalPages()) {
            currentPage = resultPage.getTotalPages();
            resultPage = productService.searchForShop(name, categoryId, PageRequest.of(currentPage - 1, PAGE_SIZE, sort));
        }
        PageHelper.addPageNumbers(model, resultPage, currentPage);
        model.addAttribute("productPage", resultPage);
        model.addAttribute("name", name.trim());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findActive());
        return "web/home";
    }
}
