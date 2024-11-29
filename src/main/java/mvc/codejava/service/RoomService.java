package mvc.codejava.service;

import mvc.codejava.entity.BookingDetail;
import mvc.codejava.entity.Room;
import mvc.codejava.entity.RoomType;
import mvc.codejava.repository.RoomRepository;
import mvc.codejava.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> searchAll(String keyword) {
        return roomRepository.searchAvailableRooms(keyword);
    }

    public void bookRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Phòng không tồn tại."));
        if (room.isAvailable()) {
            room.setAvailable(false);
            roomRepository.save(room);
        } else {
            throw new RuntimeException("Phòng đã được đặt.");
        }
    }

    public List<Room> checkAvailability(LocalDate checkInDate, LocalDate checkOutDate, int people) {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Ngày nhận phòng và ngày trả phòng không được để trống.");
        }

        if (people > 8) {
            return roomRepository.findAllAvailableRooms(checkInDate, checkOutDate);
        } else {
            return roomRepository.findAvailableRooms(checkInDate, checkOutDate, people);
        }
    }
}
