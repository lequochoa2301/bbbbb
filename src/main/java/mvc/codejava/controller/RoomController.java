package mvc.codejava.controller;

import mvc.codejava.entity.Rating;
import mvc.codejava.entity.Room;
import mvc.codejava.entity.RoomType;
import mvc.codejava.entity.User;
import mvc.codejava.service.RatingService;
import mvc.codejava.service.RoomService;
import mvc.codejava.service.RoomTypeService;
import mvc.codejava.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/rooms")
    public String listRooms(@RequestParam(required = false) String keyword, Model model) {
        List<Room> rooms = roomService.searchAll(keyword);
        model.addAttribute("rooms", rooms);
        System.out.println(rooms); // Kiểm tra xem rooms có null hay không
        return "hotel/room";
    }

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;
    @GetMapping("/roomDetails/{id}")
    public String showRoomDetails(@PathVariable("id") Long id,
                                  HttpSession session,
                                  Model model) {
        RoomType roomType = roomTypeService.findById(id); // Tìm RoomType theo id
        List<Rating> ratings = ratingService.getRatingsByRoomTypeId(id); // Lấy danh sách đánh giá cho RoomType
        // Lưu thông tin vào session
        session.setAttribute("roomId", roomType.getId());
        session.setAttribute("roomTypeName", roomType.getRoomTypeName());
        session.setAttribute("price", roomType.getPrice());
        model.addAttribute("roomType", roomType);
        model.addAttribute("ratings", ratings); // Gửi danh sách đánh giá đến trang view
        return "hotel/roomDetails"; // Trả về trang roomDetails.html
    }

    @PostMapping("/roomDetails/{roomTypeId}/addRating")
    public String addRating(@PathVariable("roomTypeId") Long roomTypeId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam int score,
                            @RequestParam String comment,
                            Model model) {
        // Lấy tên đăng nhập từ userDetails
        String username = userDetails.getUsername();

        // Lấy RoomType từ dịch vụ
        RoomType roomType = roomTypeService.findById(roomTypeId);

        // Tìm User từ username
        User user = userService.findByUsername(username);

        // Tạo đối tượng Rating mới và thiết lập các giá trị
        Rating rating = new Rating();
        rating.setRoomType(roomType);
        rating.setUser(user);  // Gán người dùng vào đánh giá
        rating.setScore(score);
        rating.setComment(comment);
        rating.setCreatedDate(LocalDateTime.now());

        // Lưu đánh giá vào cơ sở dữ liệu
        ratingService.saveRating(rating);

        // Sau khi thêm đánh giá, chuyển lại về trang chi tiết RoomType và hiển thị lại các đánh giá
        return "redirect:/roomDetails/" + roomTypeId;
    }

    @PostMapping("/roomDetails/{roomTypeId}/deleteRating")
    public String deleteRating(@RequestParam("ratingId") Long ratingId, @PathVariable Long roomTypeId) {
        ratingService.deleteRating(ratingId); // Gọi service để xóa đánh giá
        return "redirect:/roomDetails/" + roomTypeId; // Quay lại trang chi tiết phòng
    }


}
