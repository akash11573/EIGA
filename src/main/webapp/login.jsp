<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - EIGA</title>
</head>
<body>
    <h2>Login to EIGA</h2>
    <% if (request.getParameter("registered") != null) { %>
        <p style="color:green;">Registration successful! Please login.</p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="login" method="post">
        <label>Email:</label>
        <input type="email" name="email" required><br><br>
        <label>Password:</label>
        <input type="password" name="password" required><br><br>
        <label><input type="checkbox" name="rememberMe"> Remember Me</label><br><br>
        <button type="submit">Login</button>
    </form>
    <p>Don't have an account? <a href="register">Register here</a></p>
</body>
</html>
