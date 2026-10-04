<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eiga.model.*" %>
<%@ page import="com.eiga.service.tmdb.TmdbConfig" %>
<!DOCTYPE html>
<html>
<head>
    <title>Movie Details - EIGA</title>
    <style>
        body { background-color: #121212; color: #ffffff; font-family: sans-serif; padding: 20px; }
        a { color: #00d4ff; text-decoration: none; }
        .show-card { background: #1e1e1e; padding: 15px; margin-bottom: 10px; border-radius: 8px; border: 1px solid #333; }
        .btn { background: #e50914; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; }
    </style>
</head>
<body>
    <a href="${pageContext.request.contextPath}/movies">← Back to Movies</a><br><br>
    <% 
        Movie m = (Movie) request.getAttribute("movie"); 
        Boolean isPlaying = (Boolean) request.getAttribute("isPlaying");
        List<Show> activeShows = (List<Show>) request.getAttribute("activeShows");
        Map<String, Theatre> theatreMap = (Map<String, Theatre>) request.getAttribute("theatreMap");
        if (m != null) {
    %>
        <h2><%= m.getTitle() %></h2>
        <% if (m.getBackdropPath() != null && !m.getBackdropPath().isEmpty() && !m.getBackdropPath().equals("null")) { %>
            <img src="<%= TmdbConfig.IMAGE_BASE_URL + m.getBackdropPath() %>" alt="Backdrop" width="600" style="border-radius:8px;" />
        <% } %>
        <div style="display:flex; margin-top:20px;">
            <% if (m.getPosterPath() != null && !m.getPosterPath().isEmpty() && !m.getPosterPath().equals("null")) { %>
                <img src="<%= TmdbConfig.IMAGE_BASE_URL + m.getPosterPath() %>" alt="Poster" width="200" style="margin-right:20px; border-radius:8px;"/>
            <% } %>
            <div>
                <p><strong>Overview:</strong> <%= m.getOverview() %></p>
                <p><strong>Release Date:</strong> <%= m.getReleaseDate() %></p>
                <p><strong>Runtime:</strong> <%= m.getRuntime() %> mins</p>
                <p><strong>Genres:</strong> <%= m.getGenres() %></p>
                
                <h3 style="color: <%= isPlaying ? "#00ff88" : "#ff4444" %>;">
                    <%= isPlaying ? "Available for Booking in EIGA Theatres" : "Not currently available for booking" %>
                </h3>
            </div>
        </div>
        
        <% if(isPlaying && activeShows != null && !activeShows.isEmpty()) { %>
            <hr style="border-color:#333; margin: 30px 0;">
            <h3>Select a Show</h3>
            <% for(Show s : activeShows) { 
                Theatre t = theatreMap.get(s.getTheatreId());
            %>
                <div class="show-card">
                    <h4><%= t != null ? t.getName() + " - " + t.getCity() : "Unknown Theatre" %></h4>
                    <p>Date: <strong><%= s.getShowDate() %></strong> | Time: <strong><%= s.getStartTime() %> - <%= s.getEndTime() %></strong></p>
                    <a href="${pageContext.request.contextPath}/seat-selection?showId=<%= s.getShowId() %>" class="btn">Select Seats</a>
                </div>
            <% } %>
        <% } %>
    <% } %>
</body>
</html>
