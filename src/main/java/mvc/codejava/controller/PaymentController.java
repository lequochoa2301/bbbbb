package mvc.codejava.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mvc.codejava.entity.Booking;
import mvc.codejava.entity.BookingDetail;
import mvc.codejava.entity.Payment;
import mvc.codejava.entity.Room;
import mvc.codejava.repository.BookingDetailRepository;
import mvc.codejava.repository.PaymentRepository;
import mvc.codejava.repository.RoomRepository;
import mvc.codejava.service.BookingService;
import mvc.codejava.service.EmailService;
import mvc.codejava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class PaymentController {
    @Autowired
    PaymentRepository paymentHistoryRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    // Change to USD because Paypal using USD
    public static double convertVNDToUSD(double vndAmount, double exchangeRate) {
        double usdAmount = vndAmount / exchangeRate;
        return Math.round(usdAmount * 100.0) / 100.0;
    }
    @RequestMapping("/payment")
    public String viewPayment(@ModelAttribute("booking") Booking booking, Model model, HttpSession session) {
        session.setAttribute("paymentBooking", booking);
        double usdAmount = convertVNDToUSD(booking.getTotalAmount(), 23000);
        model.addAttribute("price", usdAmount);
        return "payment/index";
    }

    @RequestMapping("/payment-error")
    public String viewPaymentError(Model model) {
        return "payment/error";
    }

    @RequestMapping(value = "/thank-you", method = RequestMethod.GET)
    public String viewPaymentThanks(Model model, HttpSession session) {
        Booking paymentBooking = (Booking) session.getAttribute("paymentBooking");
        String roomTypeName = (String) session.getAttribute("roomTypeName");
        model.addAttribute("booking", paymentBooking);
        model.addAttribute("roomTypeName", roomTypeName);
        // Gửi email xác nhận đặt phòng
        emailService.sendBookingConfirmationEmail(paymentBooking, roomTypeName);

        return "hotel/bookingSuccessful"; // Chuyển tới trang bookingSuccessful
    }

    @RequestMapping(value = "/payment-success", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Payment> paymentSuccess(@RequestBody JsonNode fullResponse, HttpSession session) {
        try {
            Payment paymentEntity = decodeJsonToPaymentEntity(fullResponse);
            if (paymentEntity != null) {
                Booking paymentBooking = (Booking) session.getAttribute("paymentBooking");
                Booking savedBooking = bookingService.saveBooking(paymentBooking, "CARD");
                Long roomId = (Long) session.getAttribute("roomId");
                // Kiểm tra bookingId và roomId không null
                if (roomId == null || paymentBooking.getId() == null) {
                    return new ResponseEntity<>(new Payment(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

                // Tạo đối tượng BookingDetail và gán thông tin
                BookingDetail bookingDetail = new BookingDetail();

                // Gán bookingId vào BookingDetail
                paymentBooking.setStatus("Đã Thanh Toán");
                bookingDetail.setBooking(paymentBooking);  // Gán đối tượng booking vào BookingDetail

                // Tạo đối tượng Room từ roomId và gán vào BookingDetail
                Room room = new Room();
                room.setId(roomId);  // Chỉ cần gán roomId mà không cần tìm phòng từ DB
                bookingDetail.setRoom(room);  // Gán roomId vào BookingDetail

                // Lưu BookingDetail vào cơ sở dữ liệu
                bookingDetailRepository.save(bookingDetail);
                return ResponseEntity.ok(paymentEntity);
            } else {
                return new ResponseEntity<>(new Payment(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Payment(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private Payment decodeJsonToPaymentEntity(JsonNode jsonNode) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //  JsonNode jsonNode = objectMapper.readTree(json);

            Payment paymentEntity = new Payment();
//            paymentEntity.setOrderID(jsonNode.get("orderID").asText());
//            paymentEntity.setPayerID(jsonNode.get("payerID").asText());
//            paymentEntity.setPayerName(jsonNode.get("details").get("payer").get("name").get("given_name").asText());
//            paymentEntity.setPayerAddress(jsonNode.get("details").get("payer").get("address").get("country_code").asText());
//            paymentEntity.setPayerEmail(jsonNode.get("details").get("payer").get("email_address").asText());
//            paymentEntity.setPaymentAmount(jsonNode.get("details").get("purchase_units").get(0).get("amount").get("value").asText());
            //paymentEntity.setPaymentCurrency(jsonNode.get("details").get("purchase_units").get(0).get("amount").get("currency_code").asText());
            paymentEntity.setPaymentStatus(jsonNode.get("details").get("status").asText());

            return paymentEntity;
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý exception nếu có
            return null;
        }
    }
}