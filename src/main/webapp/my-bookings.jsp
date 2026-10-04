<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eiga.model.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Bookings - EIGA</title>
    <style>
        body { background-color: #121212; color: #ffffff; font-family: sans-serif; padding: 20px; }
        a { color: #00d4ff; text-decoration: none; }
        .booking-card { background: #1e1e1e; padding: 15px; margin-bottom: 10px; border-radius: 8px; border: 1px solid #333; }
        .status-CONFIRMED { color: #00ff88; font-weight: bold; }
        .status-CANCELLED { color: #ff4444; font-weight: bold; }
    </style>
</head>
<body>
    <a href="${pageContext.request.contextPath}/">← Home</a><br><br>
    <h2>My Bookings</h2>
    
    <% 
        List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
        if(bookings == null || bookings.isEmpty()) {
    %>
        <p>You have no bookings yet.</p>
    <% } else {
        for(Booking b : bookings) {
    %>
        <div class="booking-card">
            <h4>Booking ID: <%= b.getBookingId() %></h4>
            <p>Status: <span class="status-<%= b.getBookingStatus() %>"><%= b.getBookingStatus() %></span></p>
            <p>Amount: $<%= b.getTotalAmount() %></p>
            <p>Time: <%= b.getBookingTime() %></p>
            <a href="${pageContext.request.contextPath}/digital-ticket?bookingId=<%= b.getBookingId() %>">View Ticket</a>
        </div>
    <% 
        }
    } %>
</body>
</html>
