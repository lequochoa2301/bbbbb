package mvc.codejava.service;

import mvc.codejava.entity.Promotion;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class PromotionService {
    public boolean isPromotionValid(Promotion promotion) {
        LocalDate currentDate = LocalDate.now(); // Lấy ngày hiện tại

        // Kiểm tra xem ngày bắt đầu trước ngày hiện tại và ngày kết thúc sau ngày hiện tại
        return promotion.getStartDay().isBefore(currentDate) &&
                promotion.getEndDate().isAfter(currentDate) &&
                promotion.getQuantity() > 0; // Kiểm tra số lượng mã khuyến mãi
    }
}
