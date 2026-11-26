package vn.iotstar.service;

/** Giá trị bị trùng (username, email, tên danh mục...) gắn với 1 ô nhập trên form. */
public class DuplicateFieldException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String field;

    public DuplicateFieldException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
