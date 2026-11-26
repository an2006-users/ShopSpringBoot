// Nút con mắt: bấm để xem / ẩn mật khẩu ở mọi ô <input type="password">
document.querySelectorAll('input[type="password"]').forEach(function (input) {
    var box = input.parentElement;           // .mb-3 / .col-md-6 chứa label + input + thông báo lỗi
    box.classList.add("pw-wrap");
    input.classList.add("pw-input");

    var btn = document.createElement("button");
    btn.type = "button";
    btn.className = "pw-toggle";
    btn.setAttribute("aria-label", "Hiện mật khẩu");
    btn.innerHTML = '<i class="bi bi-eye"></i>';
    input.insertAdjacentElement("afterend", btn);

    // đặt nút ở giữa chiều cao ô nhập (label phía trên làm lệch nếu dùng top: 50%)
    var place = function () { btn.style.top = (input.offsetTop + input.offsetHeight / 2) + "px"; };
    place();
    window.addEventListener("resize", place);

    btn.addEventListener("click", function () {
        var show = input.type === "password";
        input.type = show ? "text" : "password";
        btn.innerHTML = show ? '<i class="bi bi-eye-slash"></i>' : '<i class="bi bi-eye"></i>';
        btn.setAttribute("aria-label", show ? "Ẩn mật khẩu" : "Hiện mật khẩu");
        input.focus();
    });
});

// Kiểm tra form phía trình duyệt theo kiểu Bootstrap 5 (server vẫn kiểm tra lại bằng @Valid)
document.querySelectorAll("form.needs-validation").forEach(function (form) {
    form.querySelectorAll("[data-match]").forEach(function (input) {
        var other = document.querySelector(input.dataset.match);
        var check = function () {
            input.setCustomValidity(other && input.value && input.value !== other.value ? "Mật khẩu nhập lại không khớp" : "");
        };
        input.addEventListener("input", check);
        if (other) other.addEventListener("input", check);
    });
    form.addEventListener("submit", function (e) {
        form.querySelectorAll("[data-match]").forEach(function (i) { i.dispatchEvent(new Event("input")); });
        if (!form.checkValidity()) {
            e.preventDefault();
            e.stopPropagation();
        }
        form.classList.add("was-validated");
    });
});
