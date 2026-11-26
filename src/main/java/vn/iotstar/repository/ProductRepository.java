package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.iotstar.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Sản phẩm đang bán (sản phẩm + danh mục đều active), lọc theo danh mục (null = tất cả)
     * và từ khoá trong tên, có phân trang.
     */
    @Query("""
            select p from Product p
            where p.active = true and p.category.active = true
              and (:categoryId is null or p.category.id = :categoryId)
              and lower(p.name) like lower(concat('%', :kw, '%'))
            """)
    Page<Product> searchForShop(@Param("kw") String keyword, @Param("categoryId") Long categoryId, Pageable pageable);

    long countByCategoryId(Long categoryId);

    /** Admin: tìm theo tên sản phẩm + phân trang. */
    Page<Product> findByNameContaining(String keyword, Pageable page);
}
