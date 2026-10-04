package com.eiga.dao;
import com.eiga.model.Payment;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.time.LocalDateTime;
import java.util.*;

public class PaymentDAO {
    private static final String FILE_NAME = "payments.xml";
    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("payment");
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    Payment p = new Payment();
                    p.setPaymentId(XmlUtil.getElementValue(e, "paymentId"));
                    p.setBookingId(XmlUtil.getElementValue(e, "bookingId"));
                    p.setPaymentMethod(XmlUtil.getElementValue(e, "paymentMethod"));
                    p.setPaymentStatus(XmlUtil.getElementValue(e, "paymentStatus"));
                    p.setTransactionId(XmlUtil.getElementValue(e, "transactionId"));
                    String amt = XmlUtil.getElementValue(e, "amount");
                    if (amt != null && !amt.isEmpty()) p.setAmount(Double.parseDouble(amt));
                    String pa = XmlUtil.getElementValue(e, "paidAt");
                    if (pa != null && !pa.isEmpty()) p.setPaidAt(LocalDateTime.parse(pa));
                    list.add(p);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    public void save(Payment p) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("payment");
            for (int i = 0; i < nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if (p.getPaymentId().equals(XmlUtil.getElementValue(existing, "paymentId"))) {
                    doc.getDocumentElement().removeChild(existing); break;
                }
            }
            Element root = doc.getDocumentElement();
            Element n = doc.createElement("payment");
            addChild(doc, n, "paymentId", p.getPaymentId());
            addChild(doc, n, "bookingId", p.getBookingId());
            addChild(doc, n, "paymentMethod", p.getPaymentMethod());
            addChild(doc, n, "paymentStatus", p.getPaymentStatus());
            addChild(doc, n, "transactionId", p.getTransactionId());
            addChild(doc, n, "amount", String.valueOf(p.getAmount()));
            addChild(doc, n, "paidAt", p.getPaidAt() != null ? p.getPaidAt().toString() : "");
            root.appendChild(n);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
