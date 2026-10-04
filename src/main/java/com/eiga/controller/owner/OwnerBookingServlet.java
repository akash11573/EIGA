package com.eiga.controller.owner;
import com.eiga.dao.*;
import com.eiga.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/owner/bookings")
public class OwnerBookingServlet extends HttpServlet {
    private BookingDAO bookingDAO = new BookingDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        
        // Strict filtering server-side
        List<Booking> bookings = bookingDAO.findAll().stream()
            .filter(b -> b.getTheatreId().equals(ownerTheatreId))
            .collect(Collectors.toList());
            
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/owner/bookings.jsp").forward(request, response);
    }
}
