package com.eiga.controller;

import com.eiga.model.Movie;
import com.eiga.model.Show;
import com.eiga.model.Theatre;
import com.eiga.service.MovieService;
import com.eiga.dao.ShowDAO;
import com.eiga.dao.TheatreDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet({"/movies", "/movie", "/admin/movies/search-tmdb", "/admin/movies/import"})
public class MovieServlet extends HttpServlet {
    private MovieService movieService = new MovieService();
    private ShowDAO showDAO = new ShowDAO();
    private TheatreDAO theatreDAO = new TheatreDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        try {
            if ("/movies".equals(path)) {
                request.setAttribute("movies", movieService.getPublicCatalogue());
                request.getRequestDispatcher("/movies.jsp").forward(request, response);
            } else if ("/movie".equals(path)) {
                String id = request.getParameter("id");
                Movie movie = movieService.getMovieById(id);
                if (movie == null) {
                    response.sendError(404, "Movie not found");
                    return;
                }
                request.setAttribute("movie", movie);
                request.setAttribute("isPlaying", movieService.isMovieCurrentlyPlaying(id));
                
                // Fetch valid active shows
                List<Show> activeShows = showDAO.findAll().stream()
                    .filter(s -> s.getMovieId().equals(id) && "SCHEDULED".equals(s.getStatus()))
                    .collect(Collectors.toList());
                
                // Fetch related theatres
                Map<String, Theatre> theatreMap = new HashMap<>();
                for(Show s : activeShows) {
                    if(!theatreMap.containsKey(s.getTheatreId())) {
                        theatreMap.put(s.getTheatreId(), theatreDAO.findById(s.getTheatreId()));
                    }
                }
                
                request.setAttribute("activeShows", activeShows);
                request.setAttribute("theatreMap", theatreMap);
                
                request.getRequestDispatcher("/movie-details.jsp").forward(request, response);
            } else if ("/admin/movies/search-tmdb".equals(path)) {
                String query = request.getParameter("q");
                if (query != null && !query.trim().isEmpty()) {
                    List<Movie> results = movieService.searchTmdb(query);
                    request.setAttribute("results", results);
                    request.setAttribute("query", query);
                }
                request.getRequestDispatcher("/admin/movies.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            if (path.startsWith("/admin")) {
                request.getRequestDispatcher("/admin/movies.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/movies.jsp").forward(request, response);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        try {
            if ("/admin/movies/import".equals(path)) {
                String tmdbId = request.getParameter("tmdbId");
                movieService.importFromTmdb(tmdbId);
                response.sendRedirect(request.getContextPath() + "/admin/movies/search-tmdb?msg=Imported+successfully");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/admin/movies.jsp").forward(request, response);
        }
    }
}
