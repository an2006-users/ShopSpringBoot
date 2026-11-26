package vn.iotstar.service.impl;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.ProductForm;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.DuplicateFieldException;
import vn.iotstar.service.ProductService;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Product> searchForShop(String keyword, Long categoryId, Pageable pageable) {
        return productRepository.searchForShop(keyword == null ? "" : keyword.trim(), categoryId, pageable);
    }

    @Override
    public Page<Product> findByNameContaining(String keyword, Pageable page) {
        return productRepository.findByNameContaining(keyword, page);
    }

    @Override
    public Page<Product> findAll(Pageable page) {
        return productRepository.findAll(page);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm #" + id));
    }

    @Override
    @Transactional
    public Product save(ProductForm form) {
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new DuplicateFieldException("categoryId", "Danh mục không tồn tại"));
        Product p = form.getId() == null ? new Product() : findById(form.getId());
        p.setName(form.getName());
        p.setPrice(form.getPrice());
        p.setCategory(category);
        p.setDescription(form.getDescription());
        p.setIcon(form.getIcon() == null || form.getIcon().isBlank() ? "box-seam" : form.getIcon());
        p.setActive(form.isActive());
        return productRepository.save(p);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productRepository.delete(findById(id));
    }

    @Override
    public long count() {
        return productRepository.count();
    }
}
