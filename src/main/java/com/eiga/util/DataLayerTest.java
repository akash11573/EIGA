package com.eiga.util;

import com.eiga.dao.*;
import com.eiga.model.*;
import java.util.List;

public class DataLayerTest {
    public static void main(String[] args) {
        System.out.println("Starting Data Layer Test...");
        
        try {
            // Test 1: Users
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.findAll();
            System.out.println("Users found: " + users.size() + (users.size() > 0 ? " (e.g. " + users.get(0).getUserId() + ")" : ""));
            
            // Test 2: Movies
            MovieDAO movieDAO = new MovieDAO();
            List<Movie> movies = movieDAO.findAll();
            System.out.println("Movies found: " + movies.size());
            
            // Test 3: Theatres
            TheatreDAO theatreDAO = new TheatreDAO();
            List<Theatre> theatres = theatreDAO.findAll();
            System.out.println("Theatres found: " + theatres.size());
            
            // Test 4: Screens
            ScreenDAO screenDAO = new ScreenDAO();
            List<Screen> screens = screenDAO.findAll();
            System.out.println("Screens found: " + screens.size());
            
            // Test 5: Seats
            SeatDAO seatDAO = new SeatDAO();
            List<Seat> seats = seatDAO.findAll();
            System.out.println("Seats found: " + seats.size());
            
            // Test 6: Shows
            ShowDAO showDAO = new ShowDAO();
            List<Show> shows = showDAO.findAll();
            System.out.println("Shows found: " + shows.size());
            
            // Test 7: Bookings
            BookingDAO bookingDAO = new BookingDAO();
            List<Booking> bookings = bookingDAO.findAll();
            System.out.println("Bookings found: " + bookings.size());
            
            // Test 8: Payments
            PaymentDAO paymentDAO = new PaymentDAO();
            List<Payment> payments = paymentDAO.findAll();
            System.out.println("Payments found: " + payments.size());
            
            // Test 9: Reviews
            ReviewDAO reviewDAO = new ReviewDAO();
            List<Review> reviews = reviewDAO.findAll();
            System.out.println("Reviews found: " + reviews.size());
            
            // Test 10: Staff
            StaffDAO staffDAO = new StaffDAO();
            List<Staff> staffMembers = staffDAO.findAll();
            System.out.println("Staff found: " + staffMembers.size());
            
            // Test Write Operation
            System.out.println("Testing save operation...");
            User newUser = new User();
            newUser.setUserId("U9999");
            userDAO.save(newUser);
            
            List<User> newUsers = userDAO.findAll();
            System.out.println("Users after save: " + newUsers.size());
            
            System.out.println("All data layer tests completed successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data layer test failed.");
        }
    }
}
