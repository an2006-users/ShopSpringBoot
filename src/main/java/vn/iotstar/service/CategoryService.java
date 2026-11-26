package vn.iotstar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;

public interface CategoryService {

    Page<Category> findByNameContaining(String keyword, Pageable page);

    Page<Category> findAll(Pageable page);

    /** @throws java.util.NoSuchElementException không tồn tại */
    Category findById(Long id);

    /** Thêm (id null) hoặc cập nhật. @throws DuplicateFieldException tên đã tồn tại */
    Category save(Category category);

    /** @throws IllegalStateException danh mục còn sản phẩm */
    void delete(Long id);

    List<Category> findActive();

    long count();

    long countActive();
}
