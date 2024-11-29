package mvc.codejava.service;

import mvc.codejava.entity.Rating;
import mvc.codejava.entity.User;
import mvc.codejava.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    // Phương thức để thêm đánh giá mới

    public void saveRating(Rating rating) {
        ratingRepository.save(rating); // Lưu đánh giá vào DB
    }

    public List<Rating> getRatingsByRoomTypeId(Long roomTypeId) {
        return ratingRepository.findByRoomTypeId(roomTypeId); // Truy vấn đánh giá theo `roomTypeId`
    }


    // Lấy tất cả đánh giá của một User
    public List<Rating> getRatingsByUserId(Long userId) {
        return ratingRepository.findByUserId(userId);
    }

    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId); // Xóa đánh giá theo ID
    }
}
