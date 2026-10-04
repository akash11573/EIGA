<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Manage Staff - EIGA</title></head>
<body style="background:#121212; color:#fff; font-family:sans-serif; padding:20px;">
    
    <div style="background: #333; padding: 10px; margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/owner/dashboard" style="margin-right: 15px; color: #fff;">Dashboard</a>
        <a href="${pageContext.request.contextPath}/owner/screens" style="margin-right: 15px; color: #fff;">Screens & Seats</a>
        <a href="${pageContext.request.contextPath}/owner/shows" style="margin-right: 15px; color: #fff;">Shows</a>
        <a href="${pageContext.request.contextPath}/owner/bookings" style="margin-right: 15px; color: #fff;">Bookings</a>
        <a href="${pageContext.request.contextPath}/owner/staff" style="margin-right: 15px; color: #fff;">Staff</a>
        <a href="${pageContext.request.contextPath}/login" style="float: right; color: #ff4444;">Logout</a>
    </div>

    <h2>Manage Theatre Staff</h2>
    
    <c:if test="${not empty sessionScope.msg}">
        <p style="color:#00ff88;">${sessionScope.msg}</p><c:remove var="msg" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <p style="color:#ff4444;">${sessionScope.error}</p><c:remove var="error" scope="session" />
    </c:if>

    <div style="background:#1e1e1e; padding:15px; margin-bottom:20px;">
        <h3>Add Staff</h3>
        <form action="${pageContext.request.contextPath}/owner/staff" method="post">
            <input type="hidden" name="action" value="CREATE" />
            <input type="text" name="fullName" placeholder="Full Name" required />
            <input type="email" name="email" placeholder="Email" required />
            <input type="text" name="password" placeholder="Password" required />
            <button type="submit">Add Staff</button>
        </form>
    </div>

    <table border="1" cellpadding="10" style="border-collapse:collapse; width:100%;">
        <tr style="background:#333;">
            <th>Staff ID</th><th>Name</th><th>Email</th><th>Status</th><th>Actions</th>
        </tr>
        <c:forEach var="st" items="${staffList}">
            <tr>
                <td>${st.userId}</td>
                <td>${st.fullName}</td>
                <td>${st.email}</td>
                <td>${st.status}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/owner/staff" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="TOGGLE" />
                        <input type="hidden" name="staffId" value="${st.userId}" />
                        <button type="submit">Toggle Status</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
