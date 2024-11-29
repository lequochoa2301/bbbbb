package mvc.codejava.controller;

import mvc.codejava.entity.*;
import mvc.codejava.repository.*;
import mvc.codejava.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/now")
    public String bookingNow() {
        // Kiểm tra xem người dùng có đăng nhập không
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            // Người dùng đã đăng nhập, chuyển hướng tới trang home
            return "redirect:/";
        } else {
            // Người dùng chưa đăng nhập, chuyển hướng tới trang đăng ký
            return "redirect:/register";
        }
    }

    @GetMapping("/check-availability")
    public String checkAvailability(@ModelAttribute Booking booking,
                                    @RequestParam(value = "checkInDate", required = false) String checkInDateStr,
                                    @RequestParam(value = "checkOutDate", required = false) String checkOutDateStr,
                                    @RequestParam(value = "people", required = false) Integer people,
                                    HttpSession session,
                                    Model model) {
        if (booking.getCheckInDate() == null || booking.getCheckOutDate() == null || people == null) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            return "hotel/availabilityResult"; // Trả về trang kết quả
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate;
        LocalDate checkOutDate;

        try {
            checkInDate = LocalDate.parse(checkInDateStr, formatter);
            checkOutDate = LocalDate.parse(checkOutDateStr, formatter);
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Ngày không hợp lệ. Vui lòng nhập lại.");
            return "hotel/availabilityResult"; // Trả về trang kết quả
        }

        List<Room> availableRooms = roomService.checkAvailability(checkInDate, checkOutDate, people);

        // Kiểm tra nếu không có phòng nào khả dụng
        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "Không có phòng nào khả dụng cho thời gian đã chọn.");
        }
/// Tạo đối tượng Booking và lưu vào session
        Booking dateBooking = new Booking();
        dateBooking.setBookingDate(LocalDate.now()); // Đặt ngày đặt phòng là ngày hiện tại
        dateBooking.setCheckInDate(checkInDate);
        dateBooking.setCheckOutDate(checkOutDate);

        // Lưu đối tượng Booking vào session
        session.setAttribute("booking", dateBooking);

        model.addAttribute("availableRooms", availableRooms);
        return "hotel/availabilityResult"; // Trả về trang kết quả
    }

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/bookingForm")
    public String bookingForm(
            @RequestParam(value = "availableRooms", required = false) List<Long> availableRooms,
            @RequestParam(value = "roomId", required = false) Long roomId,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            Model model) {

        // Xử lý danh sách các phòng đã chọn (nếu có)
        if (availableRooms != null && !availableRooms.isEmpty()) {
            String roomIds = availableRooms.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            model.addAttribute("someRoomIdsValue", roomIds);
            System.out.println("Selected room IDs: " + roomIds); // In ra danh sách phòng đã chọn
        } else {
            model.addAttribute("error", "Không có phòng nào được chọn.");
            System.out.println("No rooms selected.");
        }

        // Tìm kiếm và hiển thị danh sách phòng theo từ khóa
        if (keyword != null && !keyword.isEmpty()) {
            List<Room> rooms = roomService.searchAll(keyword);
            if (rooms != null && !rooms.isEmpty()) {
                model.addAttribute("rooms", rooms);
                System.out.println("Rooms found: " + rooms.size()); // In ra số lượng phòng tìm thấy
            } else {
                model.addAttribute("error", "Không tìm thấy phòng nào với từ khóa '" + keyword + "'.");
                System.out.println("No rooms found for keyword: " + keyword);
            }
        }

        // Lấy tên đăng nhập từ userDetails
        String username = userDetails.getUsername();
        System.out.println("username: " + userDetails.getUsername());

        // Lấy thông tin từ session về loại phòng và giá
        String roomTypeName = (String) session.getAttribute("roomTypeName");
        Double price = (Double) session.getAttribute("price");

        if (roomTypeName != null && price != null) {
            model.addAttribute("roomTypeName", roomTypeName);
            model.addAttribute("price", price);
            System.out.println("Room Type Name: " + roomTypeName);
            System.out.println("Price: " + price);
        }

        List<AdditionalService> allServices = bookingService.getAllServices();
        List<Promotion> allPromotions = bookingService.getAllPromotions();

        // Gửi thông tin chi tiết (ID, giá trị giảm giá, chi phí dịch vụ)
        model.addAttribute("services", allServices);
        model.addAttribute("promotions", allPromotions);

        // Lấy thông tin Booking từ session
        Booking dateBooking = (Booking) session.getAttribute("booking");
        if (dateBooking != null) {
            model.addAttribute("bookingDate", dateBooking.getBookingDate());
            model.addAttribute("checkInDate", dateBooking.getCheckInDate());
            model.addAttribute("checkOutDate", dateBooking.getCheckOutDate());
            System.out.println("Booking Date: " + dateBooking.getBookingDate()); // In ra ngày đặt
            System.out.println("Check-in Date: " + dateBooking.getCheckInDate()); // In ra ngày nhận phòng
            System.out.println("Check-out Date: " + dateBooking.getCheckOutDate()); // In ra ngày trả phòng
        } else {
            model.addAttribute("error", "Không tìm thấy thông tin đặt phòng.");
            System.out.println("No booking information found in session.");
        }
        return "hotel/bookingForm";
    }


    @Autowired
    private EmailService emailService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @PostMapping("/create")
    public String createBooking(
            @ModelAttribute("booking") Booking booking,
            RedirectAttributes redirectAttributes,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam(value = "services", required = false) Long serviceId,
            @RequestParam(value = "promoCode", required = false) Long promoCodeId,
//            @RequestParam("roomId") Long roomId,
            HttpSession session, // Để truy cập session
            Principal principal,
            Model model) {

        // Lấy roomTypeName từ session
        String roomTypeName = (String) session.getAttribute("roomTypeName");

        if (roomTypeName == null) {
            model.addAttribute("error", "Không tìm thấy loại phòng trong session.");
            return "hotel/bookingForm";
        }

        // Kiểm tra phương thức thanh toán

        // Lấy thông tin người dùng
        User currentUser = userService.findByUsername(principal.getName());
        booking.setUser(currentUser);

//             Gán dịch vụ thêm (nếu có)
        if (serviceId != null && serviceId != 0) {
            AdditionalService additionalService = bookingService.getServiceById(serviceId);
            if (additionalService != null) {
                booking.setAdditionalService(additionalService);
            } else {
                model.addAttribute("error", "Dịch vụ không hợp lệ.");
                return "hotel/bookingForm"; // Trả về form nếu dịch vụ không hợp lệ
            }
        }

        // Gán mã khuyến mãi (nếu có)
        if (promoCodeId != null && promoCodeId != 0) {
            System.out.println("Promo Code ID: " + promoCodeId);
            Promotion promotion = bookingService.getPromotionById(promoCodeId);
            System.out.println("Promotion found: " + promotion);
            if (promotion != null) {
                booking.setPromotion(promotion);
            } else {
                model.addAttribute("error", "Mã khuyến mãi không hợp lệ.");
                return "hotel/bookingForm"; // Trả về form nếu mã khuyến mãi không hợp lệ
            }
        }
        // Lưu đối tượng booking vào redirectAttributes, đối tượng này sẽ được lưu trong session
        redirectAttributes.addFlashAttribute("booking", booking);
        if ("COD".equals(paymentMethod)) {
            Booking savedBooking = bookingService.saveBooking(booking, paymentMethod);
            System.out.println("Booking saved: " + savedBooking);

            Long roomId = (Long) session.getAttribute("roomId");
            System.out.println("Lấy roomId từ session: " + roomId);

            // Kiểm tra bookingId và roomId không null
            if (roomId == null || booking.getId() == null) {
                model.addAttribute("error", "Phòng hoặc thông tin đặt phòng không hợp lệ.");
                return "hotel/bookingForm";  // Trả lại trang nếu không hợp lệ
            }

            // Tạo đối tượng BookingDetail và gán thông tin
            BookingDetail bookingDetail = new BookingDetail();

            // Gán bookingId vào BookingDetail
            booking.setStatus("Chưa Thanh Toán");
            bookingDetail.setBooking(booking);  // Gán đối tượng booking vào BookingDetail

            // Tạo đối tượng Room từ roomId và gán vào BookingDetail
            Room room = new Room();
            room.setId(roomId);  // Chỉ cần gán roomId mà không cần tìm phòng từ DB
            bookingDetail.setRoom(room);  // Gán roomId vào BookingDetail

            // Lưu BookingDetail vào cơ sở dữ liệu
            bookingDetailRepository.save(bookingDetail);

            // Thêm thông tin vào model và chuyển đến trang bookingSuccessful
            model.addAttribute("booking", savedBooking);
            model.addAttribute("roomTypeName", roomTypeName);  // Truyền roomTypeName từ session vào model
            // Gửi email xác nhận đặt phòng
            emailService.sendBookingConfirmationEmail(savedBooking, roomTypeName);

            // Chuyển tới trang thành công
            return "hotel/bookingSuccessful"; // Chuyển tới trang bookingSuccessful
        } else {
            return "redirect:/payment";
        }
    }




    // Phương thức initBinder để xử lý việc chuyển đổi LocalDate
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Không cho phép định dạng sai
        binder.registerCustomEditor(LocalDate.class, "checkInDate", new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(LocalDate.class, "checkOutDate", new CustomDateEditor(dateFormat, true));
    }

}
