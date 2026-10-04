package com.eiga.controller.owner;
import com.eiga.dao.*;
import com.eiga.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/owner/dashboard")
public class OwnerDashboardServlet extends HttpServlet {
    private TheatreDAO theatreDAO = new TheatreDAO();
    private ScreenDAO screenDAO = new ScreenDAO();
    private ShowDAO showDAO = new ShowDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        if (ownerTheatreId == null) { response.sendError(403, "No theatre assigned"); return; }
        
        Theatre theatre = theatreDAO.findById(ownerTheatreId);
        List<Screen> activeScreens = screenDAO.findAll().stream().filter(s -> s.getTheatreId().equals(ownerTheatreId)).collect(Collectors.toList());
        List<Show> allShows = showDAO.findAll().stream().filter(s -> s.getTheatreId().equals(ownerTheatreId)).collect(Collectors.toList());
        List<Booking> allBookings = bookingDAO.findAll().stream().filter(b -> b.getTheatreId().equals(ownerTheatreId)).collect(Collectors.toList());
        
        LocalDate today = LocalDate.now();
        long todayShowsCount = allShows.stream().filter(s -> today.equals(s.getShowDate())).count();
        long activeBookingsCount = allBookings.stream().filter(b -> "CONFIRMED".equals(b.getBookingStatus())).count();
        
        request.setAttribute("theatre", theatre);
        request.setAttribute("screensCount", activeScreens.size());
        request.setAttribute("totalShowsCount", allShows.size());
        request.setAttribute("todayShowsCount", todayShowsCount);
        request.setAttribute("activeBookingsCount", activeBookingsCount);
        
        request.getRequestDispatcher("/owner/dashboard.jsp").forward(request, response);
    }
}
