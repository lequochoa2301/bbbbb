package mvc.codejava.controller;

import mvc.codejava.entity.Room;
import mvc.codejava.repository.RoomRepository;
import mvc.codejava.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @RequestMapping("/")
    public String index(){
        return "hotel/home";
    }

    @PostMapping("/searchhome")
    public String searchRooms(@RequestParam String email, @RequestParam String fullname, Model model) {
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "hotel/home";
    }
//    @PostMapping("/search")
//    public String searchRooms(@RequestParam String keyword, Model model) {
//        List<Room> rooms = roomService.searchRoomsByKeyword(keyword);
//        model.addAttribute("rooms", rooms);
//        return "hotel/list";
//    }
    @GetMapping("/list")
    public String showRoomList(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "hotel/list";
    }

    @RequestMapping("/rooms")
    public String rooms(){
        return "hotel/rooms";
    }

    @RequestMapping("/index")
    public String indexx(){
        return "tempadmin/index"; //trang cua admin
    }

    @RequestMapping("/service")
    public String showservice(){
        return "hotel/service";
    }


    @RequestMapping("/contact")
    public String showcontact(){
        return "hotel/contact";
    }

}
