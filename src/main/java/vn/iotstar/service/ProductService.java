package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Product;
import vn.iotstar.model.ProductForm;

public interface ProductService {

    /** Trang chủ: sản phẩm đang bán, lọc theo danh mục (null = tất cả) và từ khoá. */
    Page<Product> searchForShop(String keyword, Long categoryId, Pageable pageable);

    // ----------------------------------------------------- quản trị (admin)

    Page<Product> findByNameContaining(String keyword, Pageable page);

    Page<Product> findAll(Pageable page);

    /** @throws java.util.NoSuchElementException không tồn tại */
    Product findById(Long id);

    /** Thêm (form.id null) hoặc sửa. @throws DuplicateFieldException danh mục không tồn tại */
    Product save(ProductForm form);

    void delete(Long id);

    long count();
}
