package mvc.codejava.service;

import mvc.codejava.entity.Booking;
import mvc.codejava.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Đăng Ký Thành Công");
        message.setText("Chào " + name + ",\n\nBạn đã đăng ký thành công!");
        mailSender.send(message);
    }

    // Phương thức để gửi email xác nhận đặt phòng
    public void sendBookingConfirmationEmail(String to, String name, String bookingDetails) {
        // Tạo một đối tượng SimpleMailMessage
        SimpleMailMessage message = new SimpleMailMessage();

        // Thiết lập thông tin email
        message.setTo(to); // Địa chỉ email của người nhận
        message.setSubject("Xác Nhận Đặt Phòng"); // Tiêu đề email
        message.setText("Xin chào " + name + ",\n\n" +
                "Cảm ơn bạn đã đặt phòng. Dưới đây là thông tin đặt phòng của bạn:\n" +
                bookingDetails + "\n\nTrân trọng,\nĐội ngũ hỗ trợ khách hàng"); // Nội dung email

        // Gửi email
        mailSender.send(message);
    }

    public void sendContactEmail(String name, String email, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("bachthibichthuy1622@gmail.com");
        mailMessage.setSubject("New Contact Message from " + name);
        mailMessage.setText("Sender: " + name + "\nEmail: " + email + "\n\nMessage:\n" + message);

        mailSender.send(mailMessage);
    }

    public void sendAccountUpdateEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Thông báo cập nhật tài khoản thành công");
        message.setText("Chào bạn, \n\nThông tin tài khoản của bạn đã được cập nhật thành công.");

        mailSender.send(message);
    }


    public void sendBookingConfirmationEmail(Booking booking, String roomTypeName) {
        // Tạo nội dung email
        String subject = "Xác nhận Đặt phòng thành công!";
        String body = "Chào " + booking.getUser().getFullName() + ",\n\n" +
                "Cảm ơn bạn đã đặt phòng với chúng tôi. Đây là thông tin đơn hàng của bạn:\n" +
                "Loại phòng: " + roomTypeName + "\n" +
                "Ngày nhận phòng: " + booking.getCheckInDate() + "\n" +
                "Ngày trả phòng: " + booking.getCheckOutDate() + "\n";

        // Định dạng số tiền
        DecimalFormat df = new DecimalFormat("#.###");  // Định dạng tiền với dấu phẩy
        String formattedAmount = df.format(booking.getTotalAmount());  // Định dạng số tiền

        body += "Tổng tiền: " + formattedAmount + " VND\n";  // Thêm thông tin số tiền vào nội dung email

        body += "Hình thức thanh toán: " + booking.getPayment().getPaymentMethod() + "\n" +
                "Mọi thắc mắc xin liên hệ trực tiếp đến bộ phận chăm sóc khách hàng thông qua số 0866045453 \n" + "\n\n";

        // Cấu hình email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@example.com");
        message.setTo(booking.getUser().getEmail());
        message.setSubject(subject);
        message.setText(body);

        // Gửi email
        mailSender.send(message);
    }


}
