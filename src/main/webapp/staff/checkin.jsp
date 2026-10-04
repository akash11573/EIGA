<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Staff Check-in - EIGA</title>
    <style>
        body { background-color: #121212; color: #ffffff; font-family: sans-serif; padding: 20px; }
        .container { max-width: 600px; margin: 0 auto; background: #1e1e1e; padding: 20px; border-radius: 8px; }
        input[type="text"] { width: 100%; padding: 10px; margin: 10px 0; box-sizing: border-box; }
        .btn { background: #e50914; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; width: 100%; }
        .result { padding: 15px; margin-top: 20px; text-align: center; font-size: 20px; font-weight: bold; border-radius: 4px;}
        .VALID { background: #00ff88; color: black; }
        .ALREADY_CHECKED_IN { background: #e5a909; color: black; }
        .ERROR { background: #ff4444; color: white; }
    </style>
</head>
<body>
    <div class="container">
        <h2 style="text-align:center;">MAIN ENTRANCE SCANNER</h2>
        <a href="${pageContext.request.contextPath}/staff/dashboard" style="color:#00d4ff;">← Back to Staff Dashboard</a><br><br>
        
        <% 
            String result = (String) request.getAttribute("resultCode");
            if (result != null) {
                String cssClass = "ERROR";
                if ("VALID".equals(result)) cssClass = "VALID";
                else if ("ALREADY_CHECKED_IN".equals(result)) cssClass = "ALREADY_CHECKED_IN";
        %>
            <div class="result <%= cssClass %>"><%= result %></div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/staff/checkin" method="post" style="margin-top: 30px;">
            <label>Scan QR Code:</label>
            <input type="text" name="qrPayload" placeholder="EIGA|..." autofocus />
            <button type="submit" class="btn">Process Scan</button>
        </form>
        
        <hr style="border-color:#333; margin: 30px 0;">
        
        <form action="${pageContext.request.contextPath}/staff/checkin" method="post">
            <label>Manual Booking ID (Fallback):</label>
            <input type="text" name="manualBookingId" placeholder="Booking ID" />
            <button type="submit" class="btn" style="background:#333;">Manual Entry</button>
        </form>
    </div>
</body>
</html>
