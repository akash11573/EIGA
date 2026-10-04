<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Digital Ticket - EIGA</title>
    <style>
        body { background-color: #121212; color: #ffffff; font-family: sans-serif; padding: 20px; }
        a { color: #00d4ff; text-decoration: none; }
        .ticket { background: #fff; color: #000; padding: 20px; margin: 20px auto; border-radius: 8px; max-width: 400px; text-align: center; border: 2px dashed #000;}
        .ticket h2 { margin: 0 0 10px 0; color: #e50914;}
    </style>
</head>
<body>
    <a href="${pageContext.request.contextPath}/my-bookings">← My Bookings</a><br><br>
    
    <div class="ticket">
        <h2>${movie.title}</h2>
        <p><strong>Theatre:</strong> ${theatre.name}</p>
        <p><strong>Screen:</strong> ${show.screenId}</p>
        <p><strong>Date & Time:</strong> ${show.showDate} @ ${show.startTime}</p>
        <hr style="border-top: 1px dashed #000;">
        <p><strong>Seats:</strong> ${booking.seatIds}</p>
        <p><strong>Total Paid:</strong> $${booking.totalAmount}</p>
        <hr style="border-top: 1px dashed #000;">
        <p>Booking Ref: ${booking.bookingId}</p>
        <p>Status: ${booking.bookingStatus}</p>
    </div>
</body>
</html>
