//package mvc.codejava.service;
//
//
//import mvc.codejava.entity.Room;
//import mvc.codejava.repository.RoomRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
////@Service
////public class RoomService {
////    @Autowired
////    private RoomRepository roomRepository;
////
////    public List<Room> getAllRooms() {
////        return roomRepository.findAll();
////    }
//
////    public Room getRoomById(Long id) {
////        return roomRepository.findById(id).orElse(null);
////    }
////
////    public void bookRoom(Long roomId) {
////        Room room = roomRepository.findById(roomId).orElse(null);
////        if (room != null) {
////            if (room.isAvailable()) { // Kiểm tra xem phòng có còn khả dụng hay không
////                room.setAvailable(false); // Đánh dấu phòng là không còn trống
////                roomRepository.save(room); // Lưu thay đổi vào cơ sở dữ liệu
////            } else {
////                // Xử lý trường hợp phòng đã được đặt
////                throw new RuntimeException("Phòng đã được đặt.");
////            }
////        }
////    }
////
////    public List<Room> searchRoomsByKeyword(String keyword) {
////        return roomRepository.findByRoomNumberContainingIgnoreCase(keyword); // Sửa lại theo đúng thuộc tính trong Room
////    }
//}
