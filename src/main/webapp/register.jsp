<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - EIGA</title>
</head>
<body>
    <h2>Create an EIGA Account</h2>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="register" method="post">
        <label>Full Name:</label>
        <input type="text" name="fullName" required><br><br>
        <label>Email:</label>
        <input type="email" name="email" required><br><br>
        <label>Mobile:</label>
        <input type="text" name="mobile"><br><br>
        <label>Password:</label>
        <input type="password" name="password" required><br><br>
        <label>Confirm Password:</label>
        <input type="password" name="confirmPassword" required><br><br>
        <label><input type="checkbox" name="terms" required> I accept the Terms & Conditions</label><br><br>
        <button type="submit">Register</button>
    </form>
    <p>Already have an account? <a href="login">Login here</a></p>
</body>
</html>
