<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><title>Owner Dashboard - EIGA</title></head>
<body style="background:#121212; color:#fff; font-family:sans-serif; padding:20px;">
    
    <div style="background: #333; padding: 10px; margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/owner/dashboard" style="margin-right: 15px; color: #fff;">Dashboard</a>
        <a href="${pageContext.request.contextPath}/owner/screens" style="margin-right: 15px; color: #fff;">Screens & Seats</a>
        <a href="${pageContext.request.contextPath}/owner/shows" style="margin-right: 15px; color: #fff;">Shows</a>
        <a href="${pageContext.request.contextPath}/owner/bookings" style="margin-right: 15px; color: #fff;">Bookings</a>
        <a href="${pageContext.request.contextPath}/owner/staff" style="margin-right: 15px; color: #fff;">Staff</a>
        <a href="${pageContext.request.contextPath}/login" style="float: right; color: #ff4444;">Logout</a>
    </div>

    <h2>Theatre Dashboard: ${theatre.name}</h2>
    <p>Location: ${theatre.city}</p>
    
    <div style="display:flex; gap: 20px; margin-top:20px;">
        <div style="background:#1e1e1e; padding:20px; border-radius:8px;">
            <h3>Screens</h3>
            <p style="font-size:24px;">${screensCount}</p>
        </div>
        <div style="background:#1e1e1e; padding:20px; border-radius:8px;">
            <h3>Shows Today</h3>
            <p style="font-size:24px;">${todayShowsCount} / ${totalShowsCount} Total</p>
        </div>
        <div style="background:#1e1e1e; padding:20px; border-radius:8px;">
            <h3>Active Bookings</h3>
            <p style="font-size:24px;">${activeBookingsCount}</p>
        </div>
    </div>
</body>
</html>
