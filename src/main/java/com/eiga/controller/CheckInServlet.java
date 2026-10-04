package com.eiga.controller;

import com.eiga.service.CheckInService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/checkin")
public class CheckInServlet extends HttpServlet {
    private CheckInService checkInService = new CheckInService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/staff/checkin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String staffId = (String) request.getSession().getAttribute("userId");
        String staffTheatreId = (String) request.getSession().getAttribute("theatreId");
        
        String qrPayload = request.getParameter("qrPayload");
        String manualBookingId = request.getParameter("manualBookingId");
        String result = "";
        
        if (qrPayload != null && !qrPayload.trim().isEmpty()) {
            result = checkInService.processQrScan(qrPayload.trim(), staffId, staffTheatreId);
        } else if (manualBookingId != null && !manualBookingId.trim().isEmpty()) {
            result = checkInService.processManual(manualBookingId.trim(), staffId, staffTheatreId);
        } else {
            result = "INVALID_INPUT";
        }
        
        request.setAttribute("resultCode", result);
        request.getRequestDispatcher("/staff/checkin.jsp").forward(request, response);
    }
}
