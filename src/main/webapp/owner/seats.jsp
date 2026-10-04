<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Manage Seats - EIGA</title></head>
<body style="background:#121212; color:#fff; font-family:sans-serif; padding:20px;">
    
    <div style="background: #333; padding: 10px; margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/owner/dashboard" style="margin-right: 15px; color: #fff;">Dashboard</a>
        <a href="${pageContext.request.contextPath}/owner/screens" style="margin-right: 15px; color: #fff;">Screens & Seats</a>
        <a href="${pageContext.request.contextPath}/owner/shows" style="margin-right: 15px; color: #fff;">Shows</a>
        <a href="${pageContext.request.contextPath}/owner/bookings" style="margin-right: 15px; color: #fff;">Bookings</a>
        <a href="${pageContext.request.contextPath}/owner/staff" style="margin-right: 15px; color: #fff;">Staff</a>
        <a href="${pageContext.request.contextPath}/login" style="float: right; color: #ff4444;">Logout</a>
    </div>

    <h2>Manage Seats for Screen: ${screenId}</h2>
    <a href="${pageContext.request.contextPath}/owner/screens" style="color:#00d4ff;">← Back to Screens</a><br><br>
    
    <table border="1" cellpadding="5" style="border-collapse:collapse; width:100%;">
        <tr style="background:#333;">
            <th>Seat ID</th><th>Row</th><th>Number</th><th>Category</th><th>Price</th><th>Status</th><th>Actions</th>
        </tr>
        <c:forEach var="s" items="${seats}">
            <tr>
                <td>${s.seatId}</td>
                <td>${s.row}</td>
                <td>${s.number}</td>
                <td>${s.category}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/owner/seats" method="post" style="display:inline;">
                        <input type="hidden" name="screenId" value="${screenId}" />
                        <input type="hidden" name="seatId" value="${s.seatId}" />
                        $<input type="number" step="0.01" name="price" value="${s.price}" style="width:60px;" />
                        <button type="submit">Save</button>
                    </form>
                </td>
                <td>${s.status}</td>
                <td>
                    <!-- basic toggle active/inactive if needed -->
                    <form action="${pageContext.request.contextPath}/owner/seats" method="post" style="display:inline;">
                        <input type="hidden" name="screenId" value="${screenId}" />
                        <input type="hidden" name="seatId" value="${s.seatId}" />
                        <input type="hidden" name="status" value="${s.status == 'ACTIVE' ? 'MAINTENANCE' : 'ACTIVE'}" />
                        <button type="submit">${s.status == 'ACTIVE' ? 'Mark Maintenance' : 'Mark Active'}</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
