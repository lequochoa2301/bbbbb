package mvc.codejava.service;

import mvc.codejava.entity.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void processPayment(User user, double amount) {
        // Xử lý logic thanh toán trực tuyến
        // Ví dụ: Tích hợp API của bên thứ ba như Stripe, PayPal, hoặc cổng thanh toán ngân hàng
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount.");
        }

        // Giả lập việc thanh toán thành công
        System.out.println("Payment processed for user: " + user.getUsername() + ", Amount: " + amount);
    }
}
