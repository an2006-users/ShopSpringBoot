# ShopSpringBoot – Spring Boot 4 + JSP/JSTL

Bài tập: đăng ký / đăng nhập / đăng xuất và CRUD (role ADMIN) cho bảng **Category**, **User**
có **tìm kiếm** và **phân trang**.

## Công nghệ
- Spring Boot **4.0.8** (Spring Framework 7, Spring Security 7, Hibernate 7, Jakarta EE 11, Tomcat 11 nhúng)
- **SiteMesh 3** (`spring-boot-starter-sitemesh` 3.3.0-RC1): layout `decorators/web.jsp`, `decorators/admin.jsp`
- JSP + JSTL 3.0 (`jakarta.tags.core`), Spring form tags, Bootstrap 5
- SQL Server (`mssql-jdbc`), Spring Data JPA
- Java 21, Maven, đóng gói **WAR** (JSP không chạy được trong jar thực thi)

## Chạy
1. Tạo database: chạy `sql/create-database.sql` trong SSMS (`CREATE DATABASE ShopSpringBoot`).
2. Sửa `spring.datasource.username/password` trong `src/main/resources/application.properties`.
3. Chạy `ShopSpringBootApplication` trong IntelliJ, hoặc `mvn spring-boot:run`.
4. Mở http://localhost:8090 → chuyển tới trang đăng nhập.
   Lần chạy đầu tạo sẵn tài khoản **admin / admin123** (role ADMIN).

## Câu 1 – Đăng ký, đăng nhập, đăng xuất
- **Đăng ký** `/register`: `RegisterForm` + `@Valid` (họ tên, email, username `[A-Za-z0-9_]{3,30}`,
  mật khẩu 6-50 ký tự có chữ và số, nhập lại khớp), kiểm tra trùng username/email,
  mật khẩu mã hoá **BCrypt**, vai trò mặc định **USER**.
- **Đăng nhập** `/login`: Spring Security form login với trang JSP riêng; tài khoản lấy từ bảng `users`
  qua `CustomUserDetailsService`. ADMIN → `/admin`, USER → `/`. Tài khoản bị khoá không đăng nhập được.
- **Đăng xuất**: `POST /logout` (kèm CSRF token) → huỷ session, về `/login?logout`.
- Phân quyền: `/admin/**` chỉ ROLE_ADMIN (người khác → trang 403), các trang còn lại phải đăng nhập.

## Câu 2 – CRUD trong role ADMIN (Category, User) + tìm kiếm + phân trang
Chỉ tài khoản **ADMIN** vào được `/admin/**`.

| URL | Chức năng |
|---|---|
| `/admin` | Dashboard: số danh mục, số người dùng, danh sách mới nhất |
| `/admin/categories/searchpaginated?name=&page=&size=` | Danh sách danh mục, **tìm theo tên**, **phân trang** (3/5/10/15/20 dòng) |
| `/admin/categories/add`, `/edit/{id}` | Form thêm / sửa (tên 2-100 ký tự, không trùng; mô tả ≤ 500; trạng thái) |
| `POST /admin/categories/save`, `/delete/{id}` | Lưu / xoá (POST + CSRF, xoá xong quay lại đúng trang đang xem) |
| `/admin/users/searchpaginated?name=&page=&size=` | Danh sách người dùng, **tìm theo username / họ tên / email**, **phân trang** |
| `/admin/users/add`, `/edit/{id}` | Form thêm / sửa: username, họ tên, email, SĐT, mật khẩu (để trống khi sửa = giữ cũ), vai trò, khoá/mở |
| `POST /admin/users/save`, `/delete/{id}` | Lưu / xoá. Không cho xoá / tự khoá / tự hạ quyền chính mình, luôn còn ≥ 1 ADMIN |

- Tìm kiếm + phân trang dùng **Spring Data JPA**: `Page<Category> findByNameContainingIgnoreCase(keyword, Pageable)`
  và `@Query` tìm user theo 3 cột; `PageRequest.of(page - 1, size, Sort.by(DESC, "id"))`.
- Giao diện JSP/JSTL + Spring form tags (`form:form` tự thêm CSRF, `form:errors` hiện lỗi validate).
- Dữ liệu mẫu tạo lần chạy đầu: 12 danh mục, 8 user (mật khẩu `user123`) để thử tìm kiếm / phân trang.

## Cấu trúc
```
src/main/java/vn/iotstar/
  ShopSpringBootApplication.java
  config/      SecurityConfig, CustomUserDetailsService, RoleBasedSuccessHandler, DataInitializer
  entity/      User, Role, Category
  model/       RegisterForm, UserForm            (DTO + Bean Validation)
  repository/  UserRepository, CategoryRepository (Spring Data JPA)
  service/     UserService, CategoryService (+ impl)
  controller/  AuthController, HomeController, admin/AdminController, AdminCategoryController, AdminUserController
src/main/webapp/WEB-INF/views/   JSP (common/header, footer, pagination, search-bar, alerts)
```

## Bổ sung
- **Trang chủ người dùng** `/`: danh sách **sản phẩm** (bảng `products`, 24 sản phẩm mẫu) dạng thẻ,
  lọc theo danh mục đang hoạt động, tìm theo tên, phân trang 8 sản phẩm/trang.
  Danh mục bị **Khoá** thì sản phẩm của nó không hiện. Không xoá được danh mục còn sản phẩm.
- **Nút con mắt** ở mọi ô mật khẩu (đăng nhập, đăng ký, form người dùng) để xem / ẩn mật khẩu.


## Cấu hình theo hướng dẫn (JSP trong META-INF + SiteMesh 3)
```
src/main/resources/
  META-INF/WEB-INF/views/       JSP nội dung (chỉ <title> + <body>)
  META-INF/WEB-INF/decorators/  web.jsp (người dùng), admin.jsp (/admin/*) – layout SiteMesh
  static/css, static/js         file tĩnh -> URL /static/** (spring.mvc.static-path-pattern)
  application.properties        server.port=8090, view prefix /WEB-INF/views/, suffix .jsp
```
- `JSPStaticResourceConfigurer` + `TomcatJSPConfiguration`: Tomcat nhúng lấy `classpath:/META-INF` làm gốc web
  (chạy từ IntelliJ không phụ thuộc Working directory). Spring Boot 4: `TomcatServletWebServerFactory`
  nằm ở package `org.springframework.boot.tomcat.servlet`.
- `CustomSiteMeshFilter` (`/*` → `web.jsp`, `/admin/*` → `admin.jsp`, loại trừ `/login*`, `/static/**`, `/api/**`)
  đăng ký bằng `FilterRegistrationBean` tên `sitemesh` trong `ShopSpringBootApplication`
  (`sitemesh.integration=filter` để starter không thêm filter thứ hai).
- Tomcat 11: view JSP chạy ở chế độ include (`InternalResourceViewResolver.setAlwaysInclude(true)`),
  nếu forward() thì SiteMesh nhận trang rỗng.
- `CharacterEncodingFilter` UTF-8 (`@Order(HIGHEST_PRECEDENCE)`) để hiển thị tiếng Việt.

## Tìm kiếm + phân trang (mẫu searchpaginated)
- Repository: `Page<Category> findByNameContaining(String keyword, Pageable page)` (tương tự Product, User).
- Service impl delegate → pull up lên interface service.
- Controller: `@RequestMapping("/searchpaginated")` với `name`, `Optional<Integer> page`, `Optional<Integer> size`
  (mặc định 3), `PageRequest.of(currentPage - 1, pageSize, Sort.by("name"))`, có từ khoá thì
  `findByNameContaining` ngược lại `findAll`, tính `pageNumbers` (trang hiện tại ±2), đưa `categoryPage` ra view.
- URL: `/admin/categories/searchpaginated`, `/admin/products/searchpaginated`, `/admin/users/searchpaginated`;
  Page size: 3 / 5 / 10 / 15 / 20.
