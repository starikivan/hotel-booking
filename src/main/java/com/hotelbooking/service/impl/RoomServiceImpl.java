package com.hotelbooking.service.impl;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.RoomCategory;
import com.hotelbooking.entity.dto.RoomListDTO;
import com.hotelbooking.entity.request.RoomRequest;
import com.hotelbooking.exceptions.DataNotFoundException;
import com.hotelbooking.repository.HotelRepository;
import com.hotelbooking.repository.RoomCategoryRepository;
import com.hotelbooking.repository.RoomRepository;
import com.hotelbooking.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService{

    private RoomRepository repository;
    private HotelRepository hotelRepository;
    private RoomCategoryRepository roomCategoryRepository;

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = (List<Room>) repository.findAll();
        return rooms;
    }

    @Override
    public RoomListDTO getAllHotelRooms(int hotelId, int page, int size) {
        Page<Room> resultPage = repository.findHotelRoomsPage(hotelId, new PageRequest(page, size));
        resultPage.getTotalElements();
        List<Room> rooms = resultPage.getContent();
        long totalElements = resultPage.getTotalElements();
        return new RoomListDTO(rooms, totalElements);
    }

    @Override
    public RoomListDTO getAllHotelFreeRooms(int hotelId, Date checkIn, Date checkOut, int page, int size) {
        Page<Room> resultPage = repository.findHotelFreeRoomsPage(hotelId, checkIn, checkOut,
                new PageRequest(page, size));
        resultPage.getTotalElements();
        List<Room> rooms = resultPage.getContent();
        long totalElements = resultPage.getTotalElements();
        return new RoomListDTO(rooms, totalElements);
    }

    @Override
    public Room getRoom(int id) {
        Room room = repository.findOne(id);
        return Optional.ofNullable(room).orElseThrow(() ->
                new DataNotFoundException(String.format("Room id= %s not found", id)));
    }

    @Override
    public boolean checkRoomIsFree(int id, Date checkIn, Date checkOut) {
        return repository.checkRoomIsFree(id, checkIn, checkOut);
    }

    @Override
    public Room saveRoom(RoomRequest request) {
        Hotel hotel = hotelRepository.findOne(request.getHotelId());
        RoomCategory roomCategory = roomCategoryRepository.findOne(request.getRoomCategoryId());
        Room room = new Room(request.getId(), request.getNumber(), hotel, roomCategory, request.getPrice(),
                request.getPersons());
        return repository.save(room);
    }
}
