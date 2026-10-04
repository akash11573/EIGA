package com.eiga.controller.owner;
import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.OwnerAuthorizationService;
import com.eiga.service.ShowService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/owner/shows")
public class OwnerShowServlet extends HttpServlet {
    private ShowDAO showDAO = new ShowDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private ScreenDAO screenDAO = new ScreenDAO();
    private ShowService showService = new ShowService();
    private OwnerAuthorizationService authService = new OwnerAuthorizationService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        
        List<Show> shows = showDAO.findAll().stream().filter(s -> s.getTheatreId().equals(ownerTheatreId)).collect(Collectors.toList());
        List<Movie> movies = movieDAO.findAll();
        List<Screen> screens = screenDAO.findAll().stream().filter(s -> s.getTheatreId().equals(ownerTheatreId) && "ACTIVE".equals(s.getStatus())).collect(Collectors.toList());
        
        request.setAttribute("shows", shows);
        request.setAttribute("movies", movies);
        request.setAttribute("screens", screens);
        request.getRequestDispatcher("/owner/shows.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ownerTheatreId = (String) request.getSession().getAttribute("theatreId");
        String action = request.getParameter("action");
        
        try {
            if ("CREATE".equals(action)) {
                String movieId = request.getParameter("movieId");
                String screenId = request.getParameter("screenId");
                String showDateStr = request.getParameter("showDate");
                String startTimeStr = request.getParameter("startTime");
                
                if (!authService.ownsScreen(ownerTheatreId, screenId)) {
                    throw new Exception("Access Denied to this Screen");
                }
                
                
                Show show = new Show();
                show.setShowId("SH" + System.currentTimeMillis());
                show.setMovieId(movieId);
                show.setTheatreId(ownerTheatreId);
                show.setScreenId(screenId);
                show.setShowDate(LocalDate.parse(showDateStr));
                show.setStartTime(LocalTime.parse(startTimeStr));
                
                Movie movie = movieDAO.findById(movieId);
                int runtime = movie.getRuntime() > 0 ? movie.getRuntime() : 120;
                show.setEndTime(show.getStartTime().plusMinutes(runtime));
                show.setStatus("SCHEDULED");
                
                showService.addShow(show);

                request.getSession().setAttribute("msg", "Show created successfully");
                
            } else if ("CANCEL".equals(action)) {
                String showId = request.getParameter("showId");
                if (!authService.ownsShow(ownerTheatreId, showId)) {
                    throw new Exception("Access Denied to this Show");
                }
                
                Show show = showDAO.findById(showId);
                show.setStatus("CANCELLED"); // Simple cancellation, no refund logic here per scope.
                showDAO.save(show);
                request.getSession().setAttribute("msg", "Show cancelled");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/owner/shows");
    }
}
