package com.eiga.service;
import com.eiga.dao.*;
import com.eiga.model.*;

public class CinemaService {
    private TheatreDAO theatreDAO = new TheatreDAO();
    private ScreenDAO screenDAO = new ScreenDAO();
    private SeatDAO seatDAO = new SeatDAO();

    public void addTheatre(Theatre t) throws Exception {
        if(theatreDAO.findById(t.getTheatreId()) != null) throw new Exception("Duplicate Theatre ID");
        theatreDAO.save(t);
    }

    public void addScreen(Screen s) throws Exception {
        if(screenDAO.findById(s.getScreenId()) != null) throw new Exception("Duplicate Screen ID");
        if(theatreDAO.findById(s.getTheatreId()) == null) throw new Exception("Theatre does not exist");
        screenDAO.save(s);
    }

    public void addSeat(Seat s) throws Exception {
        if(seatDAO.findById(s.getSeatId()) != null) throw new Exception("Duplicate Seat ID");
        if(screenDAO.findById(s.getScreenId()) == null) throw new Exception("Screen does not exist");
        seatDAO.save(s);
    }
}
