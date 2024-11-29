package mvc.codejava.repository;

import mvc.codejava.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    @Query("SELECT p FROM Promotion p WHERE p.promoCode = :promoCode")
    Promotion findByPromoCode(@Param("promoCode") String promoCode);
}
