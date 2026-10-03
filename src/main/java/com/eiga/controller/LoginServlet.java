package com.eiga.controller;

import com.eiga.model.User;
import com.eiga.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();

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
