package mvc.codejava.repository;


import mvc.codejava.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Tính tổng tiền cho một booking
//    @Transactional
//    @Query(value = "SELECT (DATEDIFF(b.check_out_date, b.check_in_date) * COALESCE(rt.price, 0)) " +
//            "FROM booking b " +
//            "JOIN booking_detail bd ON b.id = bd.booking_id " +
//            "JOIN room r ON bd.room_id = r.id " +
//            "JOIN room_type rt ON r.room_type_id = rt.id " +
//            "WHERE b.id = :bookingId", nativeQuery = true)
//    Double calculateTotalAmount(@Param("bookingId") Long bookingId);
}
