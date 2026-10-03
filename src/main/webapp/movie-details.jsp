<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.eiga.model.Movie" %>
<%@ page import="com.eiga.service.tmdb.TmdbConfig" %>
<!DOCTYPE html>
<html>
<head><title>Movie Details - EIGA</title></head>
<body>
    <a href="${pageContext.request.contextPath}/movies">Back to Movies</a><br><br>
    <% 
        Movie m = (Movie) request.getAttribute("movie"); 
        Boolean isPlaying = (Boolean) request.getAttribute("isPlaying");
        if (m != null) {
    %>
        <h2><%= m.getTitle() %></h2>
        <% if (m.getBackdropPath() != null && !m.getBackdropPath().isEmpty() && !m.getBackdropPath().equals("null")) { %>
            <img src="<%= TmdbConfig.IMAGE_BASE_URL + m.getBackdropPath() %>" alt="Backdrop" width="600" />
        <% } %>
        <div style="display:flex; margin-top:20px;">
            <% if (m.getPosterPath() != null && !m.getPosterPath().isEmpty() && !m.getPosterPath().equals("null")) { %>
                <img src="<%= TmdbConfig.IMAGE_BASE_URL + m.getPosterPath() %>" alt="Poster" width="200" style="margin-right:20px;"/>
            <% } %>
            <div>
                <p><strong>Overview:</strong> <%= m.getOverview() %></p>
                <p><strong>Release Date:</strong> <%= m.getReleaseDate() %></p>
                <p><strong>Runtime:</strong> <%= m.getRuntime() %> mins</p>
                <p><strong>Genres:</strong> <%= m.getGenres() %></p>
                <p><strong>Rating:</strong> <%= m.getRating() %></p>
                
                <% if (m.getTrailerKey() != null && !m.getTrailerKey().isEmpty() && !m.getTrailerKey().equals("null")) { %>
                    <p><a href="https://www.youtube.com/watch?v=<%= m.getTrailerKey() %>" target="_blank">Watch Trailer</a></p>
                <% } else { %>
                    <p>Trailer not available</p>
                <% } %>
                
                <h3 style="color: <%= isPlaying ? "green" : "red" %>;">
                    <%= isPlaying ? "Available for Booking in EIGA Theatres" : "Not currently available for booking in EIGA theatres" %>
                </h3>
            </div>
        </div>
    <% } %>
</body>
</html>
