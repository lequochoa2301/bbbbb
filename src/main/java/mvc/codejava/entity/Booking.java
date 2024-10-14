package mvc.codejava.entity;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private Date bookingDate;
    private Date checkInDate;
    private Date checkOutDate;
    private String status; // Ví dụ: "Confirmed", "Cancelled"

    @ManyToMany
    @JoinTable(
            name = "booking_service", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "booking_id"), // Khóa chính của bảng Booking
            inverseJoinColumns = @JoinColumn(name = "service_id") // Khóa chính của bảng AdditionalService
    )
    private List<AdditionalService> services;

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AdditionalService> getServices() {
        return services;
    }

    public void setServices(List<AdditionalService> services) {
        this.services = services;
    }
}
