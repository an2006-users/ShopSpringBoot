package vn.iotstar.controller.admin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;

/** Hàm dùng chung cho các trang tìm kiếm + phân trang (theo mẫu searchpaginated). */
public final class PageHelper {

    /** Các lựa chọn "Page size" trên giao diện. */
    public static final List<Integer> SIZES = List.of(3, 5, 10, 15, 20);

    public static final int DEFAULT_SIZE = 3;

    private PageHelper() {
    }

    /** page bắt đầu từ 1 trên URL; size không hợp lệ -> 3. */
    public static Pageable pageable(int currentPage, int pageSize, Sort sort) {
        int p = Math.max(currentPage, 1);
        int s = SIZES.contains(pageSize) ? pageSize : DEFAULT_SIZE;
        return PageRequest.of(p - 1, s, sort);
    }

    /** Danh sách số trang hiển thị: trang hiện tại ±2 (tối đa 5 số). */
    public static void addPageNumbers(ModelMap model, Page<?> resultPage, int currentPage) {
        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            // luôn cố gắng hiện đủ 5 số khi đang ở đầu / cuối
            if (end - start < 4) {
                if (start == 1) {
                    end = Math.min(totalPages, start + 4);
                } else if (end == totalPages) {
                    start = Math.max(1, end - 4);
                }
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("sizes", SIZES);
    }
}
