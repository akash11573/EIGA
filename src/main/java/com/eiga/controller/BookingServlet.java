package com.eiga.controller;

import com.eiga.dao.*;
import com.eiga.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet({"/my-bookings", "/digital-ticket"})
public class BookingServlet extends HttpServlet {
    private BookingDAO bookingDAO = new BookingDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private TheatreDAO theatreDAO = new TheatreDAO();
    private ShowDAO showDAO = new ShowDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String userId = (String) request.getSession().getAttribute("userId");
        
        if ("/my-bookings".equals(path)) {
            List<Booking> myBookings = bookingDAO.findAll().stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
                
            request.setAttribute("bookings", myBookings);
            request.getRequestDispatcher("/my-bookings.jsp").forward(request, response);
            
        } else if ("/digital-ticket".equals(path)) {
            String bookingId = request.getParameter("bookingId");
            if(bookingId == null) { response.sendError(400, "Missing bookingId"); return; }
            
            Booking booking = bookingDAO.findById(bookingId);
            if(booking == null || !booking.getUserId().equals(userId)) {
                response.sendError(403, "Access denied to this booking"); return;
            }
            
            Show show = showDAO.findById(booking.getShowId());
            Movie movie = movieDAO.findById(booking.getMovieId());
            Theatre theatre = theatreDAO.findById(booking.getTheatreId());
            
            request.setAttribute("booking", booking);
            request.setAttribute("show", show);
            request.setAttribute("movie", movie);
            request.setAttribute("theatre", theatre);
            
            request.getRequestDispatcher("/digital-ticket.jsp").forward(request, response);
        }
    }
}
