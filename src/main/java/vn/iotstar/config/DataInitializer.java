package vn.iotstar.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.UserRepository;

/**
 * Lần chạy đầu tiên:
 *  - chưa có ADMIN nào -> tạo tài khoản admin mặc định
 *  - bảng trống -> thêm dữ liệu mẫu (12 danh mục, 8 user, 24 sản phẩm) để thấy được tìm kiếm / phân trang
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository,
            ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.countByRole(Role.ADMIN) == 0 && !userRepository.existsByUsernameIgnoreCase(adminUsername)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFullname("Quản trị viên");
            admin.setEmail(adminUsername + "@shop.local");
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println(">>> Đã tạo tài khoản admin mặc định: " + adminUsername + " / " + adminPassword);
        }

        if (categoryRepository.count() == 0) {
            String[] names = { "Điện thoại", "Laptop", "Máy tính bảng", "Tai nghe", "Đồng hồ thông minh", "Phụ kiện",
                    "Thời trang nam", "Thời trang nữ", "Giày dép", "Sách", "Đồ gia dụng", "Thể thao" };
            for (int i = 0; i < names.length; i++) {
                Category c = new Category();
                c.setName(names[i]);
                c.setDescription("Danh mục " + names[i].toLowerCase());
                c.setActive(i % 5 != 4);
                categoryRepository.save(c);
            }
        }

        if (userRepository.count() <= 1) {
            String[][] samples = { { "an", "Nguyễn Văn An" }, { "binh", "Trần Thị Bình" }, { "cuong", "Lê Văn Cường" },
                    { "dung", "Phạm Thị Dung" }, { "em", "Hoàng Văn Em" }, { "giang", "Võ Thị Giang" },
                    { "hung", "Đặng Văn Hùng" }, { "lan", "Bùi Thị Lan" } };
            for (String[] s : samples) {
                if (userRepository.existsByUsernameIgnoreCase(s[0])) {
                    continue;
                }
                User u = new User();
                u.setUsername(s[0]);
                u.setFullname(s[1]);
                u.setEmail(s[0] + "@gmail.com");
                u.setPassword(passwordEncoder.encode("user123"));
                u.setRole(Role.USER);
                u.setEnabled(true);
                userRepository.save(u);
            }
        }
    
        if (productRepository.count() == 0) {
            // { danh mục, tên sản phẩm, giá, icon Bootstrap }
            String[][] samples = {
                { "Điện thoại", "iPhone 16 Pro 256GB", "28990000", "phone" },
                { "Điện thoại", "Samsung Galaxy S25", "22990000", "phone" },
                { "Điện thoại", "Xiaomi Redmi Note 14", "6490000", "phone" },
                { "Laptop", "MacBook Air M3 13 inch", "27490000", "laptop" },
                { "Laptop", "Dell Inspiron 15 3530", "15990000", "laptop" },
                { "Laptop", "ASUS TUF Gaming F15", "19990000", "laptop" },
                { "Máy tính bảng", "iPad Air M2 11 inch", "16990000", "tablet" },
                { "Máy tính bảng", "Samsung Galaxy Tab S9 FE", "8990000", "tablet" },
                { "Tai nghe", "AirPods Pro 2", "5990000", "earbuds" },
                { "Tai nghe", "Sony WH-1000XM5", "7490000", "headphones" },
                { "Đồng hồ thông minh", "Apple Watch Series 10", "10490000", "smartwatch" },
                { "Đồng hồ thông minh", "Xiaomi Smart Band 9", "890000", "smartwatch" },
                { "Phụ kiện", "Sạc nhanh Anker 65W", "790000", "lightning-charge" },
                { "Phụ kiện", "Chuột Logitech MX Master 3S", "2290000", "mouse" },
                { "Thời trang nam", "Áo sơ mi Oxford trắng", "390000", "person-standing" },
                { "Thời trang nam", "Quần jean slim fit", "490000", "person-standing" },
                { "Thời trang nữ", "Đầm suông công sở", "550000", "person-standing-dress" },
                { "Thời trang nữ", "Áo khoác cardigan len", "420000", "person-standing-dress" },
                { "Giày dép", "Giày chạy bộ Nike Pegasus", "3290000", "bag" },
                { "Giày dép", "Dép sandal quai ngang", "250000", "bag" },
                { "Sách", "Lập trình Java căn bản", "185000", "book" },
                { "Sách", "Clean Code (bản tiếng Việt)", "260000", "book" },
                { "Đồ gia dụng", "Nồi chiên không dầu 5L", "1590000", "house" },
                { "Thể thao", "Thảm tập yoga 8mm", "320000", "bicycle" } };
            for (String[] s : samples) {
                Category category = categoryRepository.findByNameIgnoreCase(s[0]).orElse(null);
                if (category == null) {
                    continue; // danh mục đã bị đổi tên / xoá -> bỏ qua
                }
                Product p = new Product();
                p.setCategory(category);
                p.setName(s[1]);
                p.setPrice(new BigDecimal(s[2]));
                p.setIcon(s[3]);
                p.setDescription(s[1] + " – hàng chính hãng, bảo hành 12 tháng.");
                p.setActive(true);
                productRepository.save(p);
            }
        }
    }
}
