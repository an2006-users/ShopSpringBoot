package vn.iotstar.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.DuplicateFieldException;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<Category> findByNameContaining(String keyword, Pageable page) {
        return categoryRepository.findByNameContaining(keyword, page);
    }

    @Override
    public Page<Category> findAll(Pageable page) {
        return categoryRepository.findAll(page);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy danh mục #" + id));
    }

    @Override
    @Transactional
    public Category save(Category category) {
        boolean duplicate = category.getId() == null
                ? categoryRepository.existsByNameIgnoreCase(category.getName())
                : categoryRepository.existsByNameIgnoreCaseAndIdNot(category.getName(), category.getId());
        if (duplicate) {
            throw new DuplicateFieldException("name", "Tên danh mục đã tồn tại");
        }
        if (category.getId() != null) {
            // giữ nguyên ngày tạo cũ
            Category current = findById(category.getId());
            category.setCreatedAt(current.getCreatedAt());
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = findById(id);
        long products = productRepository.countByCategoryId(id);
        if (products > 0) {
            throw new IllegalStateException("Không thể xoá \"" + category.getName() + "\": danh mục đang có "
                    + products + " sản phẩm. Hãy chuyển sang trạng thái Khoá thay vì xoá.");
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> findActive() {
        return categoryRepository.findByActiveTrueOrderByNameAsc();
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public long countActive() {
        return categoryRepository.countByActiveTrue();
    }
}
