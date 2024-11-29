package mvc.codejava.controller;

import mvc.codejava.entity.User;
//import mvc.codejava.service.EmailService;
import mvc.codejava.service.EmailService;
import mvc.codejava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "hotel/register"; // Hiển thị trang đăng ký
    }

    @PostMapping
    public String registerUser(User user, Model model) {
        try {
            // Kiểm tra nếu tên người dùng đã tồn tại trong hệ thống
            if (userService.isUsernameExist(user.getUsername())) {
                model.addAttribute("error", "Tên người dùng đã tồn tại.");
                return "hotel/register"; // Trở lại trang đăng ký nếu thất bại
            }

            // Thiết lập trạng thái của người dùng là enabled
            user.setEnabled(true);

            // Lưu thông tin người dùng vào cơ sở dữ liệu
            userService.save(user);

            // Gửi email xác nhận đăng ký thành công
            emailService.sendRegistrationEmail(user.getEmail(), user.getFullName());

            return "hotel/registrationSuccessful"; // Trang thành công

        } catch (Exception e) {
            // Xử lý khi có lỗi xảy ra trong quá trình đăng ký
            model.addAttribute("error", "Đăng ký thất bại. Vui lòng thử lại sau.");
            return "hotel/register"; // Quay lại trang đăng ký với thông báo lỗi
        }
    }
}
