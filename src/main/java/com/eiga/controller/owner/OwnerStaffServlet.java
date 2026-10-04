package com.eiga.controller.owner;
import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.OwnerAuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/owner/staff")
public class OwnerStaffServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private OwnerAuthorizationService authService = new OwnerAuthorizationService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        
        List<User> staffList = userDAO.findAll().stream()
            .filter(u -> "STAFF".equals(u.getRole()) && ownerTheatreId.equals(u.getTheatreId()))
            .collect(Collectors.toList());
            
        request.setAttribute("staffList", staffList);
        request.getRequestDispatcher("/owner/staff.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        String action = request.getParameter("action");
        
        try {
            if ("CREATE".equals(action)) {
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                
                if (userDAO.findByEmail(email) != null) {
                    throw new Exception("Email already exists");
                }
                
                User staff = new User();
                staff.setUserId("U" + System.currentTimeMillis());
                staff.setFullName(fullName);
                staff.setEmail(email);
                staff.setPasswordHash(password); // Simple text for test
                staff.setRole("STAFF");
                staff.setTheatreId(ownerTheatreId); // Bound strictly to owner's theatre
                staff.setStatus("ACTIVE");
                staff.setCreatedAt(LocalDateTime.now());
                
                userDAO.save(staff);
                request.getSession().setAttribute("msg", "Staff created successfully");
                
            } else if ("TOGGLE".equals(action)) {
                String staffId = request.getParameter("staffId");
                if (!authService.ownsStaff(ownerTheatreId, staffId)) {
                    throw new Exception("Access Denied to this Staff member");
                }
                
                User staff = userDAO.findById(staffId);
                staff.setStatus("ACTIVE".equals(staff.getStatus()) ? "INACTIVE" : "ACTIVE");
                userDAO.save(staff);
                request.getSession().setAttribute("msg", "Staff status updated");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/owner/staff");
    }
}
