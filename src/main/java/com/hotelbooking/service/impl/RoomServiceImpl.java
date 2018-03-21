package com.hotelbooking.service.impl;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.RoomCategory;
import com.hotelbooking.entity.request.RoomRequest;
import com.hotelbooking.repository.HotelRepository;
import com.hotelbooking.repository.RoomCategoryRepository;
import com.hotelbooking.repository.RoomRepository;
import com.hotelbooking.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService{

    private RoomRepository roomRepository;
    private HotelRepository hotelRepository;
    private RoomCategoryRepository roomCategoryRepository;

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = (List<Room>) roomRepository.findAll();
        return rooms;
    }

    @Override
    public Room getRoom(int id) {
        return roomRepository.findOne(id);
    }

    @Override
    public Room saveRoom(RoomRequest request) {
        Room room = new Room();
        room.setId(Integer.parseInt(request.getId()));
        room.setNumber(Integer.parseInt(request.getNumber()));
        Hotel hotel = hotelRepository.findOne(Integer.parseInt(request.getHotelId()));
        room.setHotel(hotel);
        RoomCategory roomCategory = roomCategoryRepository.findOne(Integer.parseInt(request.getRoomCategoryId()));
        room.setRoomCategory(roomCategory);
        return roomRepository.save(room);
    }
}
