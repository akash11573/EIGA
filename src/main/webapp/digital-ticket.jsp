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
        <%
            String qrUrl = "";
            try {
                com.eiga.model.Booking b = (com.eiga.model.Booking) request.getAttribute("booking");
                if (b != null) {
                    long issued = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toInstant().toEpochMilli();
                    if (b.getBookingTime() != null) {
                        issued = b.getBookingTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
                    }
                    long expires = issued + (24L * 60 * 60 * 1000);
                    String payload = com.eiga.qr.QrGenerator.generatePayload(b.getBookingId(), issued, expires);
                    qrUrl = "https://chart.googleapis.com/chart?chs=150x150&cht=qr&chl=" + java.net.URLEncoder.encode(payload, "UTF-8");
                }
            } catch(Exception e) {
                out.print("<p style='color:red;'>QR Error: " + e.getMessage() + "</p>");
            }
        %>
        <img src="<%= qrUrl %>" alt="QR Code" width="150" height="150" />
        <br>
        <p>Booking Ref: ${booking.bookingId}</p>
 ${booking.bookingId}</p>
        <p>Status: ${booking.bookingStatus}</p>
    </div>
</body>
</html>
