package vn.iotstar.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    long countByRole(Role role);

    /** Tìm theo username / họ tên / email (không phân biệt hoa thường), có phân trang. */
    @Query("""
            select u from User u
            where lower(u.username) like lower(concat('%', :kw, '%'))
               or lower(u.fullname) like lower(concat('%', :kw, '%'))
               or lower(u.email)    like lower(concat('%', :kw, '%'))
            """)
    Page<User> findByNameContaining(@Param("kw") String keyword, Pageable page);
}
