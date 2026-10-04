<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Manage Shows - EIGA</title></head>
<body style="background:#121212; color:#fff; font-family:sans-serif; padding:20px;">
    
    <div style="background: #333; padding: 10px; margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/owner/dashboard" style="margin-right: 15px; color: #fff;">Dashboard</a>
        <a href="${pageContext.request.contextPath}/owner/screens" style="margin-right: 15px; color: #fff;">Screens & Seats</a>
        <a href="${pageContext.request.contextPath}/owner/shows" style="margin-right: 15px; color: #fff;">Shows</a>
        <a href="${pageContext.request.contextPath}/owner/bookings" style="margin-right: 15px; color: #fff;">Bookings</a>
        <a href="${pageContext.request.contextPath}/owner/staff" style="margin-right: 15px; color: #fff;">Staff</a>
        <a href="${pageContext.request.contextPath}/login" style="float: right; color: #ff4444;">Logout</a>
    </div>

    <h2>Manage Shows</h2>
    
    <c:if test="${not empty sessionScope.msg}">
        <p style="color:#00ff88;">${sessionScope.msg}</p><c:remove var="msg" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <p style="color:#ff4444;">${sessionScope.error}</p><c:remove var="error" scope="session" />
    </c:if>

    <div style="background:#1e1e1e; padding:15px; margin-bottom:20px;">
        <h3>Schedule New Show</h3>
        <form action="${pageContext.request.contextPath}/owner/shows" method="post">
            <input type="hidden" name="action" value="CREATE" />
            <label>Movie:</label>
            <select name="movieId" required>
                <c:forEach var="m" items="${movies}"><option value="${m.movieId}">${m.title}</option></c:forEach>
            </select>
            <label>Screen:</label>
            <select name="screenId" required>
                <c:forEach var="sc" items="${screens}"><option value="${sc.screenId}">${sc.name}</option></c:forEach>
            </select>
            <label>Date:</label><input type="date" name="showDate" required />
            <label>Start Time:</label><input type="time" name="startTime" required />
            <button type="submit">Schedule Show</button>
        </form>
    </div>

    <table border="1" cellpadding="10" style="border-collapse:collapse; width:100%;">
        <tr style="background:#333;">
            <th>ID</th><th>Movie</th><th>Screen</th><th>Date/Time</th><th>Status</th><th>Actions</th>
        </tr>
        <c:forEach var="s" items="${shows}">
            <tr>
                <td>${s.showId}</td>
                <td>${s.movieId}</td>
                <td>${s.screenId}</td>
                <td>${s.showDate} @ ${s.startTime}</td>
                <td>${s.status}</td>
                <td>
                    <c:if test="${s.status != 'CANCELLED'}">
                        <form action="${pageContext.request.contextPath}/owner/shows" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="CANCEL" />
                            <input type="hidden" name="showId" value="${s.showId}" />
                            <button type="submit" style="color:red;">Cancel Show</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
