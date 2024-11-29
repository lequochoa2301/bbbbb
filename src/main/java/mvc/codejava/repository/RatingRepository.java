package mvc.codejava.repository;

import mvc.codejava.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Tìm tất cả đánh giá của một RoomType (phòng)
    List<Rating> findByRoomTypeId(Long roomTypeId);

    // Tìm tất cả đánh giá của một User
    List<Rating> findByUserId(Long userId);
}
