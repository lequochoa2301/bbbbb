package mvc.codejava.controller;

import mvc.codejava.entity.RoomType;
import mvc.codejava.repository.RoomTypeRepository;
import mvc.codejava.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    // Hiển thị danh sách các phòng
    @GetMapping("/room-types")
    public String showRoomTypes(Model model) {
        List<RoomType> roomTypes = roomTypeService.findAll(); // Lấy danh sách RoomType từ service
        model.addAttribute("roomTypes", roomTypes); // Truyền danh sách vào model
        return "admin/homeAdmin";
    }

    // Hiển thị form tạo mới RoomType
    @GetMapping("/room-types/create")
    public String showNewRoomTypeForm(Model model) {
        RoomType roomType = new RoomType();
        model.addAttribute("roomType", roomType);
        return "admin/new_room";  // Chuyển đến trang form tạo mới
    }

    // Lưu phòng mới vào cơ sở dữ liệu
    @PostMapping("/room-types/create")
    public String saveNewRoomType(@ModelAttribute RoomType roomType) {
        roomTypeRepository.save(roomType);  // Lưu phòng mới vào cơ sở dữ liệu
        return "redirect:/room-types";  // Chuyển hướng về trang danh sách phòng
    }

    // Hiển thị form chỉnh sửa phòng
    // Phương thức hiển thị form chỉnh sửa
    @GetMapping("/room-types/edit/{id}")
    public String editRoomType(@PathVariable("id") Long id, Model model) {
        RoomType roomType = roomTypeService.findById(id); // Giả sử bạn có phương thức này trong service
        model.addAttribute("roomType", roomType);
        return "admin/editRoomType";
    }

    // Phương thức xử lý lưu thông tin chỉnh sửa
    @PostMapping("/room-types/edit/{id}")
    public String updateRoomType(@PathVariable("id") Long id, @ModelAttribute("roomType") RoomType roomType) {
        // Kiểm tra roomTypeName
        System.out.println("Updated RoomType Name: " + roomType.getRoomTypeName());
        roomTypeService.updateRoomType(roomType);
        return "redirect:/room-types";
    }


    // Xóa phòng
    @RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		roomTypeService.delete(id);
		return "redirect:/room-types";
	}
}
