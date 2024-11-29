package mvc.codejava.controller;

import mvc.codejava.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/hotel/contact")
    public String showContactPage() {
        return "hotel/contact";  // Hiển thị trang contact
    }

    @PostMapping("/sendContact")
    public String sendContact(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("message") String message) {
        emailService.sendContactEmail(name, email, message);
        return "redirect:/hotel/contact?success";  // Điều hướng về trang contact với thông báo thành công
    }
}
