package mvc.codejava.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    private String status; // Ví dụ: "Confirmed", "Cancelled"

    private Double totalAmount;


    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingDetail> bookingDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = true)
    private AdditionalService additionalService;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = true) // Thêm khóa ngoại để liên kết với Promotion
    private Promotion promotion;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    public void addRoom(Room room) {
        BookingDetail bookingDetail = new BookingDetail(); // Tạo đối tượng BookingDetail
        bookingDetail.setRoom(room); // Thiết lập phòng cho BookingDetail
        bookingDetail.setBooking(this); // Thiết lập booking cho BookingDetail
        this.bookingDetails.add(bookingDetail); // Thêm BookingDetail vào danh sách
    }

    public List<Long> getRoomIds() {
        return bookingDetails.stream()
                .map(bookingDetail -> bookingDetail.getRoom().getId()) // Lấy ID của từng phòng
                .collect(Collectors.toList());
    }

    public String getRoomTypeName() {
        // Kiểm tra xem có BookingDetail không
        if (!bookingDetails.isEmpty()) {
            // Lấy thông tin phòng từ BookingDetail đầu tiên
            Room room = bookingDetails.get(0).getRoom();  // Giả sử mỗi BookingDetail liên kết với một Room
            if (room != null) {
                return room.getRoomType().getRoomTypeName();  // Trả về tên loại phòng
            }
        }
        return "Không xác định";  // Nếu không có phòng, trả về giá trị mặc định
    }

    public Booking() {
        this.bookingDetails = new ArrayList<>();
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

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<BookingDetail> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(List<BookingDetail> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public AdditionalService getAdditionalService() {
        return additionalService;
    }

    public void setAdditionalService(AdditionalService additionalService) {
        this.additionalService = additionalService;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
