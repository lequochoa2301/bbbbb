package mvc.codejava.repository;


import mvc.codejava.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAll();
    // Tìm phòng theo tên loại phòng (roomTypeName)

    @Query("SELECT r FROM Room r WHERE r.available = true AND "
            + "(?1 IS NULL OR LOWER(r.roomType.roomTypeName) LIKE LOWER(CONCAT('%', ?1, '%')))")
    List<Room> searchAvailableRooms(String keyword);
//jpa dua vao entyti


    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
            "(SELECT bd.room.id FROM BookingDetail bd JOIN bd.booking b WHERE " +
            "(b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)) " +
            "AND r.roomType.people >= :people")
    List<Room> findAvailableRooms(@Param("checkInDate") LocalDate checkInDate,
                                  @Param("checkOutDate") LocalDate checkOutDate,
                                  @Param("people") int people);
    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
            "(SELECT bd.room.id FROM BookingDetail bd JOIN bd.booking b WHERE " +
            "(b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate))")
    List<Room> findAllAvailableRooms(@Param("checkInDate") LocalDate checkInDate,
                                     @Param("checkOutDate") LocalDate checkOutDate);
//viet query truyen thong
}