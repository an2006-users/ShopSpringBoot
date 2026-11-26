package vn.iotstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iotstar.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /** Tìm theo tên có chứa từ khoá + phân trang. */
    Page<Category> findByNameContaining(String keyword, Pageable page);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    long countByActiveTrue();

    /** Danh mục đang hoạt động (bộ lọc ở trang chủ). */
    List<Category> findByActiveTrueOrderByNameAsc();

    Optional<Category> findByNameIgnoreCase(String name);
}
