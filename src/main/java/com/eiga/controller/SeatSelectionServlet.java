package com.eiga.controller;

import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.SeatLockService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/seat-selection")
public class SeatSelectionServlet extends HttpServlet {
    private ShowDAO showDAO = new ShowDAO();
    private ScreenDAO screenDAO = new ScreenDAO();
    private SeatDAO seatDAO = new SeatDAO();
    private SeatLockDAO seatLockDAO = new SeatLockDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private SeatLockService seatLockService = new SeatLockService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String showId = request.getParameter("showId");
        if (showId == null) {
            response.sendError(400, "Missing showId"); return;
        }
        
        Show show = showDAO.findById(showId);
        if (show == null) {
            response.sendError(404, "Show not found"); return;
        }
        
        Screen screen = screenDAO.findById(show.getScreenId());
        List<Seat> allSeats = seatDAO.findAll().stream().filter(s -> s.getScreenId().equals(screen.getScreenId())).collect(Collectors.toList());
        
        // Compute active locks and bookings
        LocalDateTime now = LocalDateTime.now();
        List<SeatLock> activeLocks = seatLockDAO.findAll().stream()
            .filter(l -> l.getShowId().equals(showId) && l.getStatus().equals("LOCKED") && l.getExpiresAt().isAfter(now))
            .collect(Collectors.toList());
            
        List<Booking> bookings = bookingDAO.findAll().stream()
            .filter(b -> b.getShowId().equals(showId) && "CONFIRMED".equals(b.getBookingStatus()))
            .collect(Collectors.toList());
            
        Set<String> bookedSeats = new HashSet<>();
        for(Booking b : bookings) {
            if(b.getSeatIds() != null) bookedSeats.addAll(Arrays.asList(b.getSeatIds().split(",")));
        }
        
        Map<String, String> seatStatus = new HashMap<>();
        String currentUserId = (String) request.getSession().getAttribute("userId");
        
        for(Seat s : allSeats) {
            if(bookedSeats.contains(s.getSeatId())) {
                seatStatus.put(s.getSeatId(), "BOOKED");
            } else {
                SeatLock l = activeLocks.stream().filter(lk -> lk.getSeatId().equals(s.getSeatId())).findFirst().orElse(null);
                if(l != null) {
                    if (currentUserId != null && currentUserId.equals(l.getUserId())) {
                        seatStatus.put(s.getSeatId(), "HELD_BY_YOU");
                    } else {
                        seatStatus.put(s.getSeatId(), "LOCKED");
                    }
                } else {
                    seatStatus.put(s.getSeatId(), "AVAILABLE");
                }
            }
        }
        
        request.setAttribute("show", show);
        request.setAttribute("screen", screen);
        request.setAttribute("seats", allSeats);
        request.setAttribute("seatStatus", seatStatus);
        
        request.getRequestDispatcher("/seat-selection.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String showId = request.getParameter("showId");
        String[] seatIds = request.getParameterValues("seatIds");
        HttpSession session = request.getSession();
        
        if (seatIds == null || seatIds.length == 0) {
            session.setAttribute("seatError", "Please select at least one seat.");
            response.sendRedirect(request.getContextPath() + "/seat-selection?showId=" + showId);
            return;
        }
        
        List<String> selectedSeats = Arrays.asList(seatIds);
        
        if (session.getAttribute("userId") == null) {
            // Preserve intent for anonymous users
            session.setAttribute("pendingShowId", showId);
            session.setAttribute("pendingSeats", selectedSeats);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userId = (String) session.getAttribute("userId");
        
        try {
            seatLockService.lockSeats(showId, selectedSeats, userId);
            response.sendRedirect(request.getContextPath() + "/checkout?showId=" + showId);
        } catch (Exception e) {
            session.setAttribute("seatError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/seat-selection?showId=" + showId);
        }
    }
}
