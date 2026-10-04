package com.eiga.controller;

import com.eiga.model.User;
import com.eiga.service.UserService;
import com.eiga.service.SeatLockService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();
    private SeatLockService seatLockService = new SeatLockService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            User user = userService.authenticate(email, password);
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("fullName", user.getFullName());
            session.setAttribute("role", user.getRole());
            if (user.getTheatreId() != null && !user.getTheatreId().isEmpty()) {
                session.setAttribute("theatreId", user.getTheatreId());
            }
            
            // Check for pending seat selection intent
            if (session.getAttribute("pendingShowId") != null && session.getAttribute("pendingSeats") != null) {
                String showId = (String) session.getAttribute("pendingShowId");
                List<String> seats = (List<String>) session.getAttribute("pendingSeats");
                session.removeAttribute("pendingShowId");
                session.removeAttribute("pendingSeats");
                
                try {
                    seatLockService.lockSeats(showId, seats, user.getUserId());
                    response.sendRedirect(request.getContextPath() + "/checkout?showId=" + showId);
                    return;
                } catch (Exception ex) {
                    session.setAttribute("seatError", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/seat-selection?showId=" + showId);
                    return;
                }
            }
            
            String role = user.getRole();
            if ("ADMIN".equals(role)) response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            else if ("OWNER".equals(role)) response.sendRedirect(request.getContextPath() + "/owner/dashboard");
            else if ("STAFF".equals(role)) response.sendRedirect(request.getContextPath() + "/staff/dashboard");
            else response.sendRedirect(request.getContextPath() + "/");
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
