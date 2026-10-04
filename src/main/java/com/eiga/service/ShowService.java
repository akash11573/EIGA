package com.eiga.service;
import com.eiga.dao.*;
import com.eiga.model.*;
import java.util.List;
import java.util.stream.Collectors;

public class ShowService {
    private ShowDAO showDAO = new ShowDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private TheatreDAO theatreDAO = new TheatreDAO();
    private ScreenDAO screenDAO = new ScreenDAO();

    public void addShow(Show show) throws Exception {
        if(showDAO.findById(show.getShowId()) != null) throw new Exception("Duplicate Show ID");
        if(movieDAO.findById(show.getMovieId()) == null) throw new Exception("Movie does not exist");
        if(theatreDAO.findById(show.getTheatreId()) == null) throw new Exception("Theatre does not exist");
        
        Screen screen = screenDAO.findById(show.getScreenId());
        if(screen == null) throw new Exception("Screen does not exist");
        if(!screen.getTheatreId().equals(show.getTheatreId())) throw new Exception("Screen does not belong to the specified Theatre");
        
        if(show.getEndTime().compareTo(show.getStartTime()) <= 0) throw new Exception("Show end time must be after start time");
        
        List<Show> existingShows = showDAO.findAll().stream()
            .filter(s -> s.getScreenId().equals(show.getScreenId()) 
                      && s.getShowDate().equals(show.getShowDate())
                      && !"CANCELLED".equals(s.getStatus()))
            .collect(Collectors.toList());
            
        for(Show existing : existingShows) {
            boolean isBefore = show.getEndTime().compareTo(existing.getStartTime()) <= 0;
            boolean isAfter = show.getStartTime().compareTo(existing.getEndTime()) >= 0;
            if(!(isBefore || isAfter)) {
                throw new Exception("Show overlaps with existing show: " + existing.getShowId());
            }
        }
        
        showDAO.save(show);
    }
}
