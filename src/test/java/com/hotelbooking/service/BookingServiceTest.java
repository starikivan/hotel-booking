package com.hotelbooking.service;

import com.hotelbooking.entity.Authority;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.City;
import com.hotelbooking.entity.Country;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.HotelCategory;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.RoomCategory;
import com.hotelbooking.entity.User;
import com.hotelbooking.entity.request.BookingRequest;
import com.hotelbooking.exceptions.DataNotFoundException;
import com.hotelbooking.repository.*;
import com.hotelbooking.service.impl.BookingServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BookingServiceTest {

    private final int BOOKING_ONE_ID = 1;
    private final int COUNTRY_ID = 2;
    private final int CITY_ID = 3;
    private final int HOTEL_ID = 4;
    private final int ROOM_ID = 5;
    private final int ROOM_CATEGORY_ID = 6;
    private final int ROLE_ID = 7;
    private final int ROOM_ONE_NUMBER = 11;
    private final int ROOM_ONE_PRICE = 100;
    private final int ROOM_ONE_NUMBER_OF_PERSONS = 2;
    private final boolean USER_ENABLED = true;
    private final int BOOKING_ONE_TOTAL_SUM = 100;
    private final int BOOKING_ONE_NUMBER_OF_PERSONS = 2;

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private BookingService bookingService;

    @Before
    public void setUp() {
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        roomRepository = mock(RoomRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, roomRepository);
    }

    @Test
    public void getAllBooking() {

        // given
        Country country = new Country(COUNTRY_ID, "Country name");
        City city = new City(CITY_ID, "City name", country);
        Hotel hotel = new Hotel(HOTEL_ID, "Hotel name", city, HotelCategory.FIVE_STARS);
        RoomCategory roomCategory = new RoomCategory(ROOM_CATEGORY_ID, "Room category name",
                "Room category description" );

        Set<Authority> rolesUserOne = new HashSet<>();
        Authority roleOne = new Authority(ROLE_ID, "username", "ROLE_ADMIN");
        rolesUserOne.add(roleOne);
        User userOne = new User("username", "password", USER_ENABLED,
                "First name","Last name", rolesUserOne);
        Room roomOne = new Room(ROOM_ID, ROOM_ONE_NUMBER, hotel, roomCategory, ROOM_ONE_PRICE,
                ROOM_ONE_NUMBER_OF_PERSONS);

        List<Booking> expectedBooking = new ArrayList<>();
        Booking bookingOne = new Booking(BOOKING_ONE_ID, roomOne, userOne, BOOKING_ONE_TOTAL_SUM,
                BOOKING_ONE_NUMBER_OF_PERSONS, new Date(), new Date());
        expectedBooking.add(bookingOne);
        given(bookingRepository.findAll()).willReturn(expectedBooking);

        //when
        List<Booking> actualBooking = bookingService.getAllBooking();

        //then
        assertEquals(expectedBooking, actualBooking);
        verify(bookingRepository).findAll();
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    public void getBooking() {
        // given
        Country country = new Country(COUNTRY_ID, "Country name");
        City city = new City(CITY_ID, "City name", country);
        Hotel hotel = new Hotel(HOTEL_ID, "Hotel name", city, HotelCategory.FIVE_STARS);
        RoomCategory roomCategory = new RoomCategory(ROOM_CATEGORY_ID, "Room category name",
                "Room category description" );
        Set<Authority> roles = new HashSet<>();
        Authority roleOne = new Authority(ROLE_ID, "username", "ROLE_ADMIN");
        roles.add(roleOne);
        User user = new User("username", "password", USER_ENABLED,
                "First name","Last name", roles);
        Room roomOne = new Room(ROOM_ID, ROOM_ONE_NUMBER, hotel, roomCategory, ROOM_ONE_PRICE,
                ROOM_ONE_NUMBER_OF_PERSONS);
        Booking expectedBooking = new Booking(BOOKING_ONE_ID, roomOne, user, BOOKING_ONE_TOTAL_SUM,
                BOOKING_ONE_NUMBER_OF_PERSONS, new Date(), new Date());
        given(bookingRepository.findOne(BOOKING_ONE_ID)).willReturn(expectedBooking);

        //when
        Booking actualBooking = bookingService.getBooking(BOOKING_ONE_ID);

        //then
        assertEquals(expectedBooking, actualBooking);
        verify(bookingRepository).findOne(BOOKING_ONE_ID);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test(expected = DataNotFoundException.class)
    public void getBookingNotFound() {

        // given
        given(bookingRepository.findOne(BOOKING_ONE_ID)).willReturn(null);

        //when
        Booking booking = bookingService.getBooking(BOOKING_ONE_ID);

        fail();
    }

    @Test
    public void saveBooking() {
        // given
        //TODO Set dateBegin and DateEnd
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        BookingRequest request = new BookingRequest(BOOKING_ONE_ID, ROOM_ID, "username", BOOKING_ONE_TOTAL_SUM,
                BOOKING_ONE_NUMBER_OF_PERSONS, dateBegin, dateEnd);
        Country country = new Country(COUNTRY_ID, "Country name");
        City city = new City(CITY_ID, "City name", country);
        Hotel hotel = new Hotel(HOTEL_ID, "Hotel name", city , HotelCategory.THREE_STARS);
        RoomCategory roomCategory = new RoomCategory(ROOM_CATEGORY_ID, "Room category name",
                "Room category description" );
        Room room = new Room(ROOM_ID, ROOM_ONE_NUMBER, hotel, roomCategory, ROOM_ONE_PRICE,
                ROOM_ONE_NUMBER_OF_PERSONS);
        Set<Authority> roles = new HashSet<>();
        Authority roleOne = new Authority(ROLE_ID, "username", "ROLE_ADMIN");
        roles.add(roleOne);
        User user = new User("username", "password", USER_ENABLED,
                "First name","Last name", roles);
        Booking expectedBooking = new Booking(BOOKING_ONE_ID, room, user, BOOKING_ONE_TOTAL_SUM,
                BOOKING_ONE_NUMBER_OF_PERSONS, dateBegin, dateEnd);

        given(userRepository.findOne("username")).willReturn(user);
        given(roomRepository.findOne(ROOM_ID)).willReturn(room);
        given(bookingRepository.save(expectedBooking)).willReturn(expectedBooking);

        //when
        Booking actualBooking = bookingService.saveBooking(request);

        //then
        assertEquals(expectedBooking, actualBooking);
        verify(userRepository).findOne("username");
        verifyNoMoreInteractions(userRepository);
        verify(roomRepository).findOne(ROOM_ID);
        verifyNoMoreInteractions(roomRepository);
        verify(bookingRepository).save(expectedBooking);
        verifyNoMoreInteractions(bookingRepository);
    }
}