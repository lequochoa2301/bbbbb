package mvc.codejava.repository;

import mvc.codejava.entity.Room;
import mvc.codejava.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findByRoomTypeNameContaining(String keyword); // Tìm kiếm theo tên loại phòng
    List<Room> findByRoomTypeName(String roomTypeName);
}
