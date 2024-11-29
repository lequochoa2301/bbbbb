package mvc.codejava.repository;

import mvc.codejava.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Booking, Long> {
}
