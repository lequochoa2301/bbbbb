package mvc.codejava.controller;

import mvc.codejava.configuration.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("message", "Đăng nhập thất bại");
        }
        return "login";
    }
    @RequestMapping("/admin/home")
    public String viewHome(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.toString();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        model.addAttribute("message", "Hello Admin: " + username);
        return "admin/homeAdmin";
    }

    @RequestMapping("/user/home")
    public String viewHome(Model model, HttpSession session) {

        // Lấy thông tin người dùng hiện tại từ SecurityContextHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra xem principal có phải là đối tượng UserDetails hay không
        if (principal instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) principal;

            // Lấy username từ UserDetails
            String username = userDetails.getUsername();

            // Lấy fullname từ UserDetails
            String fullname = userDetails.getFullname();

            // Lưu username và fullname vào session
            session.setAttribute("username", username);
            session.setAttribute("fullname", fullname);
        }

        return "hotel/home";
    }


}
