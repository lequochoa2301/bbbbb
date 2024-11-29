package mvc.codejava.entity;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Liên kết với người dùng thực hiện thanh toán

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;  // Liên kết với booking đã được thanh toán

    private LocalDate paymentDate;  // Ngày thanh toán
    private double amount;  // Số tiền đã thanh toán
    private String paymentMethod;  // Phương thức thanh toán, ví dụ: "Credit Card", "Thanh toán bằng tiền mặt"

    private String paymentStatus;  // Trạng thái thanh toán, ví dụ: "Đã thanh toán", "Chưa thanh toán", "Đang xử lý"

    // Optional: Chi tiết phương thức thanh toán (thêm nếu cần, ví dụ cho thanh toán qua thẻ hoặc ngân hàng)
    private String paymentDetails;  // Chi tiết phương thức thanh toán (ví dụ: tên ngân hàng, số thẻ)

    // Constructor, Getter, Setter

    public Payment() {
    }

    public Payment(User user, Booking booking, LocalDate paymentDate, double amount, String paymentMethod, String paymentStatus) {
        this.user = user;
        this.booking = booking;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
