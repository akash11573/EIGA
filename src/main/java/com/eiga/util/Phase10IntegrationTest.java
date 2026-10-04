package com.eiga.util;

import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Phase10IntegrationTest {
    public static void main(String[] args) {
        System.out.println("Starting Phase 10: Owner Backend Integration Tests...");
        try {
            TheatreDAO tDAO = new TheatreDAO();
            ScreenDAO scDAO = new ScreenDAO();
            SeatDAO stDAO = new SeatDAO();
            ShowDAO shDAO = new ShowDAO();
            BookingDAO bDAO = new BookingDAO();
            UserDAO uDAO = new UserDAO();
            MovieDAO mDAO = new MovieDAO();
            
            OwnerAuthorizationService auth = new OwnerAuthorizationService();
            ShowService showService = new ShowService();
            
            // Setup Base
            Theatre t1 = new Theatre(); t1.setTheatreId("T10_A"); tDAO.save(t1);
            Theatre t2 = new Theatre(); t2.setTheatreId("T10_B"); tDAO.save(t2);
            
            Screen sc1 = new Screen(); sc1.setScreenId("SCR10_A"); sc1.setTheatreId("T10_A"); scDAO.save(sc1);
            Screen sc2 = new Screen(); sc2.setScreenId("SCR10_B"); sc2.setTheatreId("T10_B"); scDAO.save(sc2);
            
            Seat st1 = new Seat(); st1.setSeatId("ST10_A"); st1.setScreenId("SCR10_A"); stDAO.save(st1);
            Seat st2 = new Seat(); st2.setSeatId("ST10_B"); st2.setScreenId("SCR10_B"); stDAO.save(st2);
            
            Movie m = new Movie(); m.setMovieId("MOV10"); m.setRuntime(120); mDAO.save(m);
            
            Show sh1 = new Show(); sh1.setShowId("SH10_A"); sh1.setTheatreId("T10_A"); sh1.setScreenId("SCR10_A"); sh1.setMovieId("MOV10"); sh1.setShowDate(LocalDate.now()); sh1.setStartTime(LocalTime.of(8,0)); sh1.setEndTime(LocalTime.of(9,59)); shDAO.save(sh1);
            Show sh2 = new Show(); sh2.setShowId("SH10_B"); sh2.setTheatreId("T10_B"); sh2.setScreenId("SCR10_B"); sh2.setMovieId("MOV10"); shDAO.save(sh2);
            
            Booking b1 = new Booking(); b1.setBookingId("B10_A"); b1.setTheatreId("T10_A"); bDAO.save(b1);
            Booking b2 = new Booking(); b2.setBookingId("B10_B"); b2.setTheatreId("T10_B"); bDAO.save(b2);
            
            User u1 = new User(); u1.setUserId("U10_A"); u1.setRole("STAFF"); u1.setTheatreId("T10_A"); uDAO.save(u1);
            User u2 = new User(); u2.setUserId("U10_B"); u2.setRole("STAFF"); u2.setTheatreId("T10_B"); uDAO.save(u2);
            
            boolean p1 = auth.ownsTheatre("T10_A", "T10_A");
            boolean p2 = !auth.ownsTheatre("T10_A", "T10_B");
            boolean p3 = auth.ownsScreen("T10_A", "SCR10_A");
            boolean p4 = !auth.ownsScreen("T10_A", "SCR10_B");
            boolean p5 = auth.ownsSeat("T10_A", "ST10_A");
            boolean p6 = !auth.ownsSeat("T10_A", "ST10_B");
            
            boolean p7 = true;
            try {
                Show nShow = new Show(); nShow.setShowId("NSH1"); nShow.setTheatreId("T10_A"); nShow.setScreenId("SCR10_A"); nShow.setMovieId("MOV10");
                nShow.setShowDate(LocalDate.now()); nShow.setStartTime(LocalTime.of(10,0)); nShow.setEndTime(LocalTime.of(12,0));
                showService.addShow(nShow);
            } catch(Exception e) { System.out.println("EXCEPTION IN p7: " + e.getMessage()); p7 = false; }
            
            boolean p8 = false;
            try {
                Show nShow = new Show(); nShow.setShowId("NSH2"); nShow.setTheatreId("T10_A"); nShow.setScreenId("SCR10_B"); nShow.setMovieId("MOV10");
                nShow.setShowDate(LocalDate.now()); nShow.setStartTime(LocalTime.of(13,0)); nShow.setEndTime(LocalTime.of(15,0));
                showService.addShow(nShow);
            } catch(Exception e) { p8 = true; } // Should fail because SCR10_B doesn't belong to T10_A
            
            boolean p9 = false;
            try {
                Show nShow = new Show(); nShow.setShowId("NSH3"); nShow.setTheatreId("T10_A"); nShow.setScreenId("SCR10_A"); nShow.setMovieId("MOV10");
                nShow.setShowDate(LocalDate.now()); nShow.setStartTime(LocalTime.of(11,0)); nShow.setEndTime(LocalTime.of(13,0));
                showService.addShow(nShow);
            } catch(Exception e) { p9 = true; } // Overlap
            
            boolean p10 = auth.ownsBooking("T10_A", "B10_A");
            boolean p11 = !auth.ownsBooking("T10_A", "B10_B");
            boolean p12 = auth.ownsStaff("T10_A", "U10_A");
            boolean p13 = !auth.ownsStaff("T10_A", "U10_B");
            
            if(p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11 && p12 && p13) {
                System.out.println("\nALL PHASE 10 TESTS PASSED");
            } else {
                System.out.println("\nSOME PHASE 10 TESTS FAILED");
                System.out.println("p1:"+p1+" p2:"+p2+" p3:"+p3+" p4:"+p4+" p5:"+p5+" p6:"+p6+" p7:"+p7+" p8:"+p8+" p9:"+p9+" p10:"+p10+" p11:"+p11+" p12:"+p12+" p13:"+p13);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
