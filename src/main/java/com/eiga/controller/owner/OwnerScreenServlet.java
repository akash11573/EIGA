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

@WebServlet("/owner/screens")
public class OwnerScreenServlet extends HttpServlet {
    private ScreenDAO screenDAO = new ScreenDAO();
    private OwnerAuthorizationService authService = new OwnerAuthorizationService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        
        List<Screen> screens = screenDAO.findAll().stream().filter(s -> s.getTheatreId().equals(ownerTheatreId)).collect(Collectors.toList());
        request.setAttribute("screens", screens);
        request.getRequestDispatcher("/owner/screens.jsp").forward(request, response);
    }
    
    // For modifying a screen (e.g., status)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        String screenId = request.getParameter("screenId");
        String action = request.getParameter("action");
        
        if (!authService.ownsScreen(ownerTheatreId, screenId)) {
            response.sendError(403, "Access Denied to this Screen"); return;
        }
        
        if ("TOGGLE_STATUS".equals(action)) {
            Screen s = screenDAO.findById(screenId);
            s.setStatus("ACTIVE".equals(s.getStatus()) ? "INACTIVE" : "ACTIVE");
            screenDAO.save(s);
        }
        response.sendRedirect(request.getContextPath() + "/owner/screens");
    }
}
