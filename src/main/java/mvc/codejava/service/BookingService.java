package mvc.codejava.service;

import mvc.codejava.entity.*;
import mvc.codejava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService; // Service để gửi email

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    public Booking saveBooking(Booking booking, String paymentMethod) {
        // Lưu thông tin đặt phòng vào cơ sở dữ liệu
        // Lưu Booking trước
        Booking savedBooking = bookingRepository.save(booking);


        // Tạo Payment và liên kết với Booking
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);

        // Liên kết hai chiều
        booking.setPayment(payment);
        payment.setBooking(booking);

        // Lưu cả Booking và Payment (do cascade)
        return bookingRepository.save(booking);
    }


    public List<AdditionalService> getAllServices() {
        return additionalServiceRepository.findAll();
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public AdditionalService getServiceById(Long serviceId) {
        return additionalServiceRepository.findById(serviceId).orElse(null);
    }

    public Promotion getPromotionById(Long promoCodeId) {
        return promotionRepository.findById(promoCodeId).orElse(null);
    }
}
