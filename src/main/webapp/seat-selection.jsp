<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eiga.model.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Seat Selection - EIGA</title>
    <style>
        body { background-color: #121212; color: #ffffff; font-family: sans-serif; padding: 20px; }
        a { color: #00d4ff; text-decoration: none; }
        .screen-curve { width: 100%; height: 50px; background: #333; margin: 20px 0; border-radius: 50% / 0 0 100% 100%; text-align: center; line-height: 50px; color: #aaa; }
        .seat { display: inline-block; width: 40px; height: 40px; line-height: 40px; text-align: center; margin: 5px; border-radius: 5px; border: 1px solid #444; font-size: 12px; cursor: pointer; background: #2a2a2a; color: white;}
        .AVAILABLE { background: #1a1a1a; }
        .LOCKED { background: #e5a909; color: black; cursor: not-allowed; }
        .BOOKED { background: #e50914; color: white; cursor: not-allowed; }
        .HELD_BY_YOU { background: #00ff88; color: black; }
        .SELECTED { background: #00d4ff; color: black; }
        .btn { background: #e50914; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; margin-top:20px;}
        .error { color: #ff4444; margin-bottom: 15px; }
    </style>
    <script>
        function toggleSeat(elem, seatId) {
            if(elem.classList.contains('LOCKED') || elem.classList.contains('BOOKED')) return;
            
            let cb = document.getElementById('cb_' + seatId);
            if (elem.classList.contains('SELECTED')) {
                elem.classList.remove('SELECTED');
                elem.classList.add('AVAILABLE');
                cb.checked = false;
            } else {
                let selected = document.querySelectorAll('.SELECTED').length;
                let held = document.querySelectorAll('.HELD_BY_YOU').length;
                if (selected + held >= 6) {
                    alert('Maximum 6 seats allowed per booking.');
                    return;
                }
                elem.classList.remove('AVAILABLE');
                elem.classList.add('SELECTED');
                cb.checked = true;
            }
        }
    </script>
</head>
<body>
    <a href="${pageContext.request.contextPath}/movie?id=${show.movieId}">← Back to Movie</a><br><br>
    
    <h2>Seat Selection for ${screen.name}</h2>
    <p>Show: <strong>${show.showDate} @ ${show.startTime}</strong></p>
    
    <% if (session.getAttribute("seatError") != null) { %>
        <div class="error"><%= session.getAttribute("seatError") %></div>
        <% session.removeAttribute("seatError"); %>
    <% } %>

    <div class="screen-curve">SCREEN</div>
    
    <form action="${pageContext.request.contextPath}/seat-selection" method="post">
        <input type="hidden" name="showId" value="${show.showId}" />
        
        <div style="text-align: center; max-width: 800px; margin: 0 auto;">
            <% 
                List<Seat> seats = (List<Seat>) request.getAttribute("seats");
                Map<String, String> statusMap = (Map<String, String>) request.getAttribute("seatStatus");
                if (seats != null) {
                    for(Seat s : seats) {
                        String st = statusMap.get(s.getSeatId());
            %>
                <div class="seat <%= st %>" onclick="toggleSeat(this, '<%= s.getSeatId() %>')">
                    <%= s.getSeatId() %>
                </div>
                <!-- Hidden checkbox -->
                <input type="checkbox" id="cb_<%= s.getSeatId() %>" name="seatIds" value="<%= s.getSeatId() %>" style="display:none;" 
                       <%= "HELD_BY_YOU".equals(st) ? "checked" : "" %> />
            <% 
                    }
                } 
            %>
        </div>
        
        <div style="text-align: center;">
            <button type="submit" class="btn">Proceed to Checkout</button>
        </div>
    </form>
    
    <div style="margin-top: 30px; text-align: center;">
        <span class="seat AVAILABLE"></span> Available 
        <span class="seat SELECTED"></span> Selected 
        <span class="seat HELD_BY_YOU"></span> Held by You 
        <span class="seat LOCKED"></span> Locked 
        <span class="seat BOOKED"></span> Booked 
    </div>
</body>
</html>
