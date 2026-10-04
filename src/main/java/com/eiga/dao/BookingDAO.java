package com.eiga.dao;
import com.eiga.model.Booking;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.time.LocalDateTime;
import java.util.*;

public class BookingDAO {
    private static final String FILE_NAME = "bookings.xml";
    public List<Booking> findAll() {
        List<Booking> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("booking");
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    Booking b = new Booking();
                    b.setBookingId(XmlUtil.getElementValue(e, "bookingId"));
                    b.setUserId(XmlUtil.getElementValue(e, "userId"));
                    b.setShowId(XmlUtil.getElementValue(e, "showId"));
                    b.setMovieId(XmlUtil.getElementValue(e, "movieId"));
                    b.setTheatreId(XmlUtil.getElementValue(e, "theatreId"));
                    b.setScreenId(XmlUtil.getElementValue(e, "screenId"));
                    b.setSeatIds(XmlUtil.getElementValue(e, "seatIds"));
                    b.setBookingStatus(XmlUtil.getElementValue(e, "bookingStatus"));
                    b.setPaymentStatus(XmlUtil.getElementValue(e, "paymentStatus"));
                    String amt = XmlUtil.getElementValue(e, "totalAmount");
                    if (amt != null && !amt.isEmpty()) b.setTotalAmount(Double.parseDouble(amt));
                    String bt = XmlUtil.getElementValue(e, "bookingTime");
                    if (bt != null && !bt.isEmpty()) b.setBookingTime(LocalDateTime.parse(bt));
                    list.add(b);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    public Booking findById(String id) {
        for (Booking b : findAll()) if (id.equals(b.getBookingId())) return b;
        return null;
    }

    public void save(Booking b) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("booking");
            for (int i = 0; i < nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if (b.getBookingId().equals(XmlUtil.getElementValue(existing, "bookingId"))) {
                    doc.getDocumentElement().removeChild(existing); break;
                }
            }
            Element root = doc.getDocumentElement();
            Element n = doc.createElement("booking");
            addChild(doc, n, "bookingId", b.getBookingId());
            addChild(doc, n, "userId", b.getUserId());
            addChild(doc, n, "showId", b.getShowId());
            addChild(doc, n, "movieId", b.getMovieId());
            addChild(doc, n, "theatreId", b.getTheatreId());
            addChild(doc, n, "screenId", b.getScreenId());
            addChild(doc, n, "seatIds", b.getSeatIds());
            addChild(doc, n, "bookingStatus", b.getBookingStatus());
            addChild(doc, n, "paymentStatus", b.getPaymentStatus());
            addChild(doc, n, "totalAmount", String.valueOf(b.getTotalAmount()));
            addChild(doc, n, "bookingTime", b.getBookingTime() != null ? b.getBookingTime().toString() : "");
            root.appendChild(n);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
// patching
