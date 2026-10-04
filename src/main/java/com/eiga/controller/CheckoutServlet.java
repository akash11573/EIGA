package com.eiga.controller;

import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private SeatLockDAO seatLockDAO = new SeatLockDAO();
    private SeatDAO seatDAO = new SeatDAO();
    private ShowDAO showDAO = new ShowDAO();
    private TheatreDAO theatreDAO = new TheatreDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private BookingService bookingService = new BookingService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String showId = request.getParameter("showId");
        String userId = (String) request.getSession().getAttribute("userId");
        
        if (showId == null) {
            response.sendError(400, "Missing showId"); return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        List<SeatLock> activeLocks = seatLockDAO.findAll().stream()
            .filter(l -> l.getShowId().equals(showId) && l.getUserId().equals(userId) && "LOCKED".equals(l.getStatus()) && l.getExpiresAt().isAfter(now))
            .collect(Collectors.toList());
            
        if (activeLocks.isEmpty()) {
            request.getSession().setAttribute("seatError", "Your seat locks have expired or you have none. Please select seats again.");
            response.sendRedirect(request.getContextPath() + "/seat-selection?showId=" + showId);
            return;
        }
        
        Show show = showDAO.findById(showId);
        Theatre theatre = theatreDAO.findById(show.getTheatreId());
        Movie movie = movieDAO.findById(show.getMovieId());
        
        double total = 0.0;
        List<Seat> lockedSeats = new ArrayList<>();
        LocalDateTime earliestExp = null;
        
        for(SeatLock lock : activeLocks) {
            Seat s = seatDAO.findById(lock.getSeatId());
            lockedSeats.add(s);
            total += s.getPrice();
            if (earliestExp == null || lock.getExpiresAt().isBefore(earliestExp)) {
                earliestExp = lock.getExpiresAt();
            }
        }
        
        long secondsLeft = ChronoUnit.SECONDS.between(now, earliestExp);
        
        request.setAttribute("show", show);
        request.setAttribute("theatre", theatre);
        request.setAttribute("movie", movie);
        request.setAttribute("lockedSeats", lockedSeats);
        request.setAttribute("totalAmount", total);
        request.setAttribute("secondsLeft", secondsLeft);
        
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String showId = request.getParameter("showId");
        String paymentMethod = request.getParameter("paymentMethod");
        boolean simulateSuccess = "SUCCESS".equals(request.getParameter("demoOutcome"));
        String userId = (String) request.getSession().getAttribute("userId");
        
        LocalDateTime now = LocalDateTime.now();
        List<String> seatIds = seatLockDAO.findAll().stream()
            .filter(l -> l.getShowId().equals(showId) && l.getUserId().equals(userId) && "LOCKED".equals(l.getStatus()) && l.getExpiresAt().isAfter(now))
            .map(SeatLock::getSeatId)
            .collect(Collectors.toList());
            
        try {
            Booking booking = bookingService.bookAndPay(userId, showId, seatIds, paymentMethod, simulateSuccess);
            response.sendRedirect(request.getContextPath() + "/digital-ticket?bookingId=" + booking.getBookingId());
        } catch (Exception e) {
            request.getSession().setAttribute("checkoutError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/checkout?showId=" + showId);
        }
    }
}
