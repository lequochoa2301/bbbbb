package mvc.codejava.controller;

import mvc.codejava.entity.User;
import mvc.codejava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accountuser")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewAccount(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Lấy username từ UserDetails
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);

        // Thêm thông tin người dùng vào model
        model.addAttribute("user", user);
        return "hotel/account"; // Trỏ tới file account.html
    }

    @PostMapping
    public String updateAccount(@ModelAttribute("user") User updatedUser,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        // Lấy tên đăng nhập của người dùng hiện tại
        String username = userDetails.getUsername();

        // Lấy thông tin người dùng hiện tại từ cơ sở dữ liệu
        User currentUser = userService.findByUsername(username);

        // Cập nhật thông tin người dùng trong cơ sở dữ liệu
        userService.updateUserDetails(updatedUser, currentUser);

        // Thêm thông báo vào RedirectAttributes
        redirectAttributes.addFlashAttribute("successMessage", "Thông báo cập nhật thành công đã được gửi đến email của bạn.");

        // Chuyển hướng về trang tài khoản của người dùng sau khi lưu
        return "redirect:/accountuser";
    }

}

