<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eiga.model.Movie" %>
<!DOCTYPE html>
<html>
<head><title>Admin - TMDB Search</title></head>
<body>
    <h2>TMDB Search & Import</h2>
    <a href="${pageContext.request.contextPath}/admin/dashboard">Back to Admin Dashboard</a><br><br>
    
    <% if (request.getParameter("msg") != null) { %>
        <p style="color:green;"><%= request.getParameter("msg") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="search-tmdb" method="get">
        <input type="text" name="q" placeholder="Search TMDB..." value="<%= request.getAttribute("query") != null ? request.getAttribute("query") : "" %>" required>
        <button type="submit">Search</button>
    </form>
    
    <hr>
    <% 
        List<Movie> results = (List<Movie>) request.getAttribute("results");
        if (results != null && !results.isEmpty()) {
            for (Movie m : results) {
    %>
        <div style="border:1px solid #ccc; padding:10px; margin-bottom:10px;">
            <h4><%= m.getTitle() %> (<%= m.getReleaseDate() %>)</h4>
            <p><%= m.getOverview() %></p>
            <form action="import" method="post">
                <input type="hidden" name="tmdbId" value="<%= m.getTmdbId() %>">
                <button type="submit">Import to EIGA</button>
            </form>
        </div>
    <% 
            }
        } 
    %>
</body>
</html>
