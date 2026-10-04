package com.eiga.util;

import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class Phase5Test {
    public static void main(String[] args) {
        System.out.println("Starting Phase 5: Cinema Data Hierarchy Tests...");
        try {
            // Clean slate for tests
            new File(XmlUtil.getDataFile("theatres.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("screens.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("seats.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("shows.xml").getAbsolutePath()).delete();
            
            CinemaService cs = new CinemaService();
            ShowService ss = new ShowService();
            MovieDAO movieDAO = new MovieDAO();
            
            // Dummy Movie
            Movie m = new Movie(); m.setMovieId("MOV-P5"); m.setTitle("Test Movie");
            movieDAO.save(m);
            
            // 1. Add Theatre
            Theatre t1 = new Theatre(); t1.setTheatreId("T1"); t1.setName("EIGA Downtown");
            cs.addTheatre(t1);
            System.out.println("PASS: Added Theatre");
            
            // 2. Add Screen
            Screen scr1 = new Screen(); scr1.setScreenId("SCR1"); scr1.setTheatreId("T1"); scr1.setName("Screen 1");
            cs.addScreen(scr1);
            System.out.println("PASS: Added Screen");
            
            // 3. Add Seat
            Seat s1 = new Seat(); s1.setSeatId("SEAT1"); s1.setScreenId("SCR1"); s1.setCategory("PREMIUM"); s1.setPrice(15.0);
            cs.addSeat(s1);
            System.out.println("PASS: Added Seat");
            
            // 4. Valid Show
            Show sh1 = new Show(); sh1.setShowId("SH1"); sh1.setMovieId("MOV-P5"); sh1.setTheatreId("T1"); sh1.setScreenId("SCR1");
            sh1.setShowDate(LocalDate.now()); sh1.setStartTime(LocalTime.of(10, 0)); sh1.setEndTime(LocalTime.of(12, 30)); sh1.setStatus("SCHEDULED");
            ss.addShow(sh1);
            System.out.println("PASS: Added Valid Show");
            
            // Validation Tests
            boolean v1 = false; try { cs.addTheatre(t1); } catch(Exception e) { v1 = e.getMessage().contains("Duplicate"); }
            System.out.println((v1 ? "PASS" : "FAIL") + ": Duplicate ID validation");
            
            boolean v2 = false; Screen scrInvalid = new Screen(); scrInvalid.setScreenId("SCR2"); scrInvalid.setTheatreId("T99");
            try { cs.addScreen(scrInvalid); } catch(Exception e) { v2 = e.getMessage().contains("Theatre does not exist"); }
            System.out.println((v2 ? "PASS" : "FAIL") + ": Screen Theatre dependency validation");
            
            boolean v3 = false; Seat seatInvalid = new Seat(); seatInvalid.setSeatId("SEAT2"); seatInvalid.setScreenId("SCR99");
            try { cs.addSeat(seatInvalid); } catch(Exception e) { v3 = e.getMessage().contains("Screen does not exist"); }
            System.out.println((v3 ? "PASS" : "FAIL") + ": Seat Screen dependency validation");
            
            boolean v4 = false; Show shInvalid = new Show(); shInvalid.setShowId("SH2"); shInvalid.setMovieId("MOV99");
            try { ss.addShow(shInvalid); } catch(Exception e) { v4 = e.getMessage().contains("Movie does not exist"); }
            System.out.println((v4 ? "PASS" : "FAIL") + ": Show Movie dependency validation");
            
            boolean v5 = false; Theatre t2 = new Theatre(); t2.setTheatreId("T2"); cs.addTheatre(t2);
            Show shMismatch = new Show(); shMismatch.setShowId("SH3"); shMismatch.setMovieId("MOV-P5"); shMismatch.setTheatreId("T2"); shMismatch.setScreenId("SCR1");
            try { ss.addShow(shMismatch); } catch(Exception e) { v5 = e.getMessage().contains("Screen does not belong"); }
            System.out.println((v5 ? "PASS" : "FAIL") + ": Show Screen/Theatre relationship validation");
            
            boolean v6 = false; Show shTime = new Show(); shTime.setShowId("SH4"); shTime.setMovieId("MOV-P5"); shTime.setTheatreId("T1"); shTime.setScreenId("SCR1");
            shTime.setShowDate(LocalDate.now()); shTime.setStartTime(LocalTime.of(14, 0)); shTime.setEndTime(LocalTime.of(12, 0));
            try { ss.addShow(shTime); } catch(Exception e) { v6 = e.getMessage().contains("end time must be after"); }
            System.out.println((v6 ? "PASS" : "FAIL") + ": Show Time logical validation");
            
            boolean v7 = false; Show shOverlap = new Show(); shOverlap.setShowId("SH5"); shOverlap.setMovieId("MOV-P5"); shOverlap.setTheatreId("T1"); shOverlap.setScreenId("SCR1");
            shOverlap.setShowDate(LocalDate.now()); shOverlap.setStartTime(LocalTime.of(11, 0)); shOverlap.setEndTime(LocalTime.of(13, 0));
            try { ss.addShow(shOverlap); } catch(Exception e) { v7 = e.getMessage().contains("overlaps"); }
            System.out.println((v7 ? "PASS" : "FAIL") + ": Show Overlap validation");
            
            System.out.println("\nPhase 5 Tests Completed Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
