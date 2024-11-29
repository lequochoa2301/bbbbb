package mvc.codejava.service;

import mvc.codejava.entity.Product;
import mvc.codejava.entity.Room;
import mvc.codejava.entity.RoomType;
import mvc.codejava.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public RoomType findById(Long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room type Id:" + id));
    }

    public List<RoomType> searchAll(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return roomTypeRepository.findAll(); // Nếu không có từ khóa, lấy tất cả
        }
        return roomTypeRepository.findByRoomTypeNameContaining(keyword); // Tìm kiếm theo tên loại phòng
    }

    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }


    public void save(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }

    public RoomType get(Long id) {
        return roomTypeRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        roomTypeRepository.deleteById(id);
    }

    public void updateRoomType(RoomType roomType) {
        roomTypeRepository.save(roomType); // Lưu RoomType đã cập nhật
    }

}
