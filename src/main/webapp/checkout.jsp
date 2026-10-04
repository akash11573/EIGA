<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eiga.model.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Checkout - EIGA</title>
    <style>
        body { background-color: #121212; color: #ffffff; font-family: sans-serif; padding: 20px; }
        .card { background: #1e1e1e; padding: 20px; margin-bottom: 20px; border-radius: 8px; border: 1px solid #333; max-width: 600px; margin: 0 auto; }
        .btn { background: #e50914; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; width: 100%; margin-top:10px; }
        .btn-secondary { background: #333; }
        .error { color: #ff4444; margin-bottom: 15px; }
        .timer { font-size: 24px; color: #e5a909; text-align: center; margin-bottom: 20px; }
    </style>
    <script>
        let timeLeft = <%= request.getAttribute("secondsLeft") %>;
        function updateTimer() {
            if (timeLeft <= 0) {
                document.getElementById("timer").innerHTML = "Locks Expired!";
                document.getElementById("checkoutBtn").disabled = true;
                return;
            }
            let m = Math.floor(timeLeft / 60);
            let s = timeLeft % 60;
            document.getElementById("timer").innerHTML = m + ":" + (s < 10 ? "0" : "") + s;
            timeLeft--;
            setTimeout(updateTimer, 1000);
        }
        window.onload = updateTimer;
    </script>
</head>
<body>
    <div class="card">
        <h2 style="text-align:center;">Secure Checkout</h2>
        
        <div class="timer" id="timer">--:--</div>
        
        <% if (session.getAttribute("checkoutError") != null) { %>
            <div class="error"><%= session.getAttribute("checkoutError") %></div>
            <% session.removeAttribute("checkoutError"); %>
        <% } %>
        
        <p><strong>Movie:</strong> ${movie.title}</p>
        <p><strong>Theatre:</strong> ${theatre.name}, ${theatre.city}</p>
        <p><strong>Show:</strong> ${show.showDate} @ ${show.startTime}</p>
        <p><strong>Seats:</strong> 
            <% 
                List<Seat> lockedSeats = (List<Seat>) request.getAttribute("lockedSeats");
                for(Seat s : lockedSeats) out.print(s.getSeatId() + " ");
            %>
        </p>
        <h3>Total: $${totalAmount}</h3>
        
        <hr style="border-color:#333;">
        
        <form action="${pageContext.request.contextPath}/checkout" method="post">
            <input type="hidden" name="showId" value="${show.showId}" />
            <div style="margin-bottom: 15px;">
                <label>Payment Method (Demo):</label><br>
                <select name="paymentMethod" style="width: 100%; padding: 8px; margin-top:5px;">
                    <option value="CREDIT_CARD">Credit Card</option>
                    <option value="PAYPAL">PayPal</option>
                </select>
            </div>
            
            <button type="submit" name="demoOutcome" value="SUCCESS" class="btn" id="checkoutBtn">Simulate Payment Success</button>
            <button type="submit" name="demoOutcome" value="FAIL" class="btn btn-secondary">Simulate Payment Failure</button>
        </form>
    </div>
</body>
</html>
