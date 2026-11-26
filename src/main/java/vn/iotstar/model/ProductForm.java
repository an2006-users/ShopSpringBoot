package vn.iotstar.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.iotstar.entity.Product;

/** Form thêm / sửa sản phẩm (admin). */
public class ProductForm {

    private Long id;

    @NotBlank(message = "Vui lòng nhập tên sản phẩm")
    @Size(min = 2, max = 150, message = "Tên sản phẩm từ 2 đến 150 ký tự")
    private String name;

    @NotNull(message = "Vui lòng nhập giá")
    @DecimalMin(value = "0", message = "Giá không được âm")
    @DecimalMax(value = "1000000000", message = "Giá tối đa 1.000.000.000")
    private BigDecimal price;

    @NotNull(message = "Vui lòng chọn danh mục")
    private Long categoryId;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    @Pattern(regexp = "^$|^[a-z0-9-]{1,50}$", message = "Tên icon chỉ gồm chữ thường, số, dấu - (vd: phone, laptop)")
    private String icon;

    private boolean active = true;

    public static ProductForm from(Product p) {
        ProductForm f = new ProductForm();
        f.id = p.getId();
        f.name = p.getName();
        f.price = p.getPrice();
        f.categoryId = p.getCategory().getId();
        f.description = p.getDescription();
        f.icon = p.getIcon();
        f.active = p.isActive();
        return f;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name == null ? null : name.trim(); }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description == null || description.isBlank() ? null : description.trim(); }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon == null ? "" : icon.trim(); }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
