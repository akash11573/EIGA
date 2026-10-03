<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eiga.model.Movie" %>
<%@ page import="com.eiga.service.tmdb.TmdbConfig" %>
<!DOCTYPE html>
<html>
<head><title>Movies - EIGA</title></head>
<body>
    <h2>Movie Catalogue</h2>
    <a href="${pageContext.request.contextPath}/">Back Home</a><br><br>
    
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <div>
        <% 
            List<Movie> movies = (List<Movie>) request.getAttribute("movies");
            if (movies != null && !movies.isEmpty()) {
                for (Movie m : movies) {
        %>
            <div style="border:1px solid #ccc; padding:10px; margin-bottom:10px; display:flex;">
                <% if (m.getPosterPath() != null && !m.getPosterPath().isEmpty() && !m.getPosterPath().equals("null")) { %>
                    <img src="<%= TmdbConfig.IMAGE_BASE_URL + m.getPosterPath() %>" alt="Poster" width="100" style="margin-right:20px;"/>
                <% } %>
                <div>
                    <h3><%= m.getTitle() %></h3>
                    <p>Status: <%= m.getStatus() %></p>
                    <a href="movie?id=<%= m.getMovieId() %>">View Details</a>
                </div>
            </div>
        <% 
                }
            } else { 
        %>
            <p>No movies in the catalogue.</p>
        <% } %>
    </div>
</body>
</html>
