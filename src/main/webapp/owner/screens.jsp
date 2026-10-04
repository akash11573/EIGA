<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Manage Screens - EIGA</title></head>
<body style="background:#121212; color:#fff; font-family:sans-serif; padding:20px;">
    
    <div style="background: #333; padding: 10px; margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/owner/dashboard" style="margin-right: 15px; color: #fff;">Dashboard</a>
        <a href="${pageContext.request.contextPath}/owner/screens" style="margin-right: 15px; color: #fff;">Screens & Seats</a>
        <a href="${pageContext.request.contextPath}/owner/shows" style="margin-right: 15px; color: #fff;">Shows</a>
        <a href="${pageContext.request.contextPath}/owner/bookings" style="margin-right: 15px; color: #fff;">Bookings</a>
        <a href="${pageContext.request.contextPath}/owner/staff" style="margin-right: 15px; color: #fff;">Staff</a>
        <a href="${pageContext.request.contextPath}/login" style="float: right; color: #ff4444;">Logout</a>
    </div>

    <h2>Manage Screens</h2>
    <table border="1" cellpadding="10" style="border-collapse:collapse; width:100%;">
        <tr style="background:#333;">
            <th>ID</th><th>Name</th><th>Capacity</th><th>Status</th><th>Actions</th>
        </tr>
        <c:forEach var="s" items="${screens}">
            <tr>
                <td>${s.screenId}</td>
                <td>${s.name}</td>
                <td>${s.capacity}</td>
                <td>${s.status}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/owner/screens" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="TOGGLE_STATUS" />
                        <input type="hidden" name="screenId" value="${s.screenId}" />
                        <button type="submit">Toggle Status</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/owner/seats?screenId=${s.screenId}" style="color:#00d4ff; margin-left:10px;">Manage Seats</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
