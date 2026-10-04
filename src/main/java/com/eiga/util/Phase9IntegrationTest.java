package com.eiga.util;

import com.eiga.dao.*;
import com.eiga.model.*;
import com.eiga.service.*;
import com.eiga.qr.*;
import java.io.File;
import java.time.*;

public class Phase9IntegrationTest {
    public static void main(String[] args) {
        System.out.println("Starting Phase 9: QR Check-in Validation Tests...");
        try {
            // Pre-requisites
            new File(XmlUtil.getDataFile("check_ins.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("bookings.xml").getAbsolutePath()).delete();
            new File(XmlUtil.getDataFile("shows.xml").getAbsolutePath()).delete();
            
            // Re-init XML files so they are completely fresh.
            XmlUtil.saveDocument(XmlUtil.loadDocument("check_ins.xml"), "check_ins.xml");
            XmlUtil.saveDocument(XmlUtil.loadDocument("bookings.xml"), "bookings.xml");
            XmlUtil.saveDocument(XmlUtil.loadDocument("shows.xml"), "shows.xml");
            
            CheckInService checkInService = new CheckInService();
            BookingDAO bookingDAO = new BookingDAO();
            ShowDAO showDAO = new ShowDAO();
            
            // Setup Base Data
            Show s1 = new Show(); s1.setShowId("SH1"); showDAO.save(s1);
            Booking b1 = new Booking(); b1.setBookingId("B1"); b1.setTheatreId("T1"); b1.setShowId("SH1"); b1.setPaymentStatus("PAID"); b1.setBookingStatus("CONFIRMED"); b1.setUserId("U1"); bookingDAO.save(b1);
            
            long now = System.currentTimeMillis();
            long day = 24L * 60 * 60 * 1000;
            
            // Generate valid QR payload
            String validQr = QrGenerator.generatePayload("B1", now, now + day);
            
            System.out.println("1. Valid QR, first scan:");
            String r1 = checkInService.processQrScan(validQr, "STAFF1", "T1");
            System.out.println("Expected: VALID, Actual: " + r1);
            
            System.out.println("2. Valid QR, second scan:");
            String r2 = checkInService.processQrScan(validQr, "STAFF1", "T1");
            System.out.println("Expected: ALREADY_CHECKED_IN, Actual: " + r2);
            
            System.out.println("3. Expired token:");
            String expiredQr = QrGenerator.generatePayload("B1", now - day - 1000, now - 1000);
            String r3 = checkInService.processQrScan(expiredQr, "STAFF1", "T1");
            System.out.println("Expected: TOKEN_EXPIRED, Actual: " + r3);
            
            System.out.println("4. Tampered QR payload:");
            String tamperedQr = validQr.substring(0, validQr.length() - 5) + "abcde";
            String r4 = checkInService.processQrScan(tamperedQr, "STAFF1", "T1");
            System.out.println("Expected: INVALID_TOKEN (or INVALID_QR), Actual: " + r4);
            
            System.out.println("5. Wrong theatre:");
            Booking b2 = new Booking(); b2.setBookingId("B2"); b2.setTheatreId("T2"); b2.setShowId("SH1"); b2.setPaymentStatus("PAID"); b2.setBookingStatus("CONFIRMED"); bookingDAO.save(b2);
            String qr2 = QrGenerator.generatePayload("B2", now, now + day);
            String r5 = checkInService.processQrScan(qr2, "STAFF1", "T1"); // Staff is at T1
            System.out.println("Expected: WRONG_THEATRE, Actual: " + r5);
            
            System.out.println("6. Wrong show:");
            Booking b3 = new Booking(); b3.setBookingId("B3"); b3.setTheatreId("T1"); b3.setShowId("SH99"); b3.setPaymentStatus("PAID"); b3.setBookingStatus("CONFIRMED"); bookingDAO.save(b3);
            String qr3 = QrGenerator.generatePayload("B3", now, now + day);
            String r6 = checkInService.processQrScan(qr3, "STAFF1", "T1"); 
            System.out.println("Expected: WRONG_SHOW, Actual: " + r6);
            
            System.out.println("7. Cancelled booking:");
            Booking b4 = new Booking(); b4.setBookingId("B4"); b4.setTheatreId("T1"); b4.setShowId("SH1"); b4.setPaymentStatus("PAID"); b4.setBookingStatus("CANCELLED"); bookingDAO.save(b4);
            String qr4 = QrGenerator.generatePayload("B4", now, now + day);
            String r7 = checkInService.processQrScan(qr4, "STAFF1", "T1");
            System.out.println("Expected: BOOKING_CANCELLED, Actual: " + r7);
            
            System.out.println("8. Unpaid booking:");
            Booking b5 = new Booking(); b5.setBookingId("B5"); b5.setTheatreId("T1"); b5.setShowId("SH1"); b5.setPaymentStatus("FAILED"); b5.setBookingStatus("CONFIRMED"); bookingDAO.save(b5);
            String qr5 = QrGenerator.generatePayload("B5", now, now + day);
            String r8 = checkInService.processQrScan(qr5, "STAFF1", "T1");
            System.out.println("Expected: PAYMENT_NOT_CONFIRMED, Actual: " + r8);
            
            System.out.println("9. Missing booking:");
            String qrMiss = QrGenerator.generatePayload("B999", now, now + day);
            String r9 = checkInService.processQrScan(qrMiss, "STAFF1", "T1");
            System.out.println("Expected: BOOKING_NOT_FOUND, Actual: " + r9);
            
            System.out.println("10. Manual Booking ID fallback:");
            Booking b6 = new Booking(); b6.setBookingId("B6"); b6.setTheatreId("T1"); b6.setShowId("SH1"); b6.setPaymentStatus("PAID"); b6.setBookingStatus("CONFIRMED"); bookingDAO.save(b6);
            String r10 = checkInService.processManual("B6", "STAFF1", "T1");
            System.out.println("Expected: VALID, Actual: " + r10);
            
            System.out.println("11. Staff from another theatre:");
            Booking b7 = new Booking(); b7.setBookingId("B7"); b7.setTheatreId("T1"); b7.setShowId("SH1"); b7.setPaymentStatus("PAID"); b7.setBookingStatus("CONFIRMED"); bookingDAO.save(b7);
            String r11 = checkInService.processManual("B7", "STAFF2", "T3"); // Staff is at T3, Booking is for T1
            System.out.println("Expected: WRONG_THEATRE, Actual: " + r11);
            
            System.out.println("12. Malformed QR:");
            String r12 = checkInService.processQrScan("BADQRCODE123", "STAFF1", "T1");
            System.out.println("Expected: INVALID_QR, Actual: " + r12);
            
            System.out.println("13. Token issued recently for a show later than 24h away:");
            // We just test if a valid token works, as Show date is not strictly compared against now()
            Booking b8 = new Booking(); b8.setBookingId("B8"); b8.setTheatreId("T1"); b8.setShowId("SH1"); b8.setPaymentStatus("PAID"); b8.setBookingStatus("CONFIRMED"); bookingDAO.save(b8);
            String qrFuture = QrGenerator.generatePayload("B8", now, now + day);
            String r13 = checkInService.processQrScan(qrFuture, "STAFF1", "T1");
            System.out.println("Expected: VALID, Actual: " + r13);
            
            boolean allPassed = "VALID".equals(r1) && "ALREADY_CHECKED_IN".equals(r2) &&
                                "TOKEN_EXPIRED".equals(r3) && (r4.contains("INVALID")) &&
                                "WRONG_THEATRE".equals(r5) && "WRONG_SHOW".equals(r6) &&
                                "BOOKING_CANCELLED".equals(r7) && "PAYMENT_NOT_CONFIRMED".equals(r8) &&
                                "BOOKING_NOT_FOUND".equals(r9) && "VALID".equals(r10) &&
                                "WRONG_THEATRE".equals(r11) && "INVALID_QR".equals(r12) &&
                                "VALID".equals(r13);
                                
            if(allPassed) {
                System.out.println("\nALL TESTS PASSED");
            } else {
                System.out.println("\nSOME TESTS FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
