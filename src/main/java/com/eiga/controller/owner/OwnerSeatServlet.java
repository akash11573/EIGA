package com.eiga.controller.owner;
import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.OwnerAuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/owner/seats")
public class OwnerSeatServlet extends HttpServlet {
    private SeatDAO seatDAO = new SeatDAO();
    private OwnerAuthorizationService authService = new OwnerAuthorizationService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        String screenId = request.getParameter("screenId");
        
        if (screenId != null) {
            if (!authService.ownsScreen(ownerTheatreId, screenId)) {
                response.sendError(403, "Access Denied to this Screen"); return;
            }
            List<Seat> seats = seatDAO.findAll().stream().filter(s -> s.getScreenId().equals(screenId)).collect(Collectors.toList());
            request.setAttribute("seats", seats);
            request.setAttribute("screenId", screenId);
        }
        request.getRequestDispatcher("/owner/seats.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        String seatId = request.getParameter("seatId");
        String priceStr = request.getParameter("price");
        String status = request.getParameter("status");
        String screenId = request.getParameter("screenId");
        
        if (!authService.ownsSeat(ownerTheatreId, seatId)) {
            response.sendError(403, "Access Denied to this Seat"); return;
        }
        
        Seat s = seatDAO.findById(seatId);
        if (priceStr != null) s.setPrice(Double.parseDouble(priceStr));
        if (status != null) s.setStatus(status);
        seatDAO.save(s);
        
        response.sendRedirect(request.getContextPath() + "/owner/seats?screenId=" + screenId);
    }
}
