package com.eiga.dao;

import com.eiga.model.Payment;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    private static final String FILE_NAME = "payments.xml";

    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("payment");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Payment obj = new Payment();
                    obj.setPaymentId(XmlUtil.getElementValue(element, "paymentId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Payment findById(String id) {
        for (Payment obj : findAll()) {
            if (id != null && id.equals(obj.getPaymentId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Payment obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("payment");
            
            Element idElement = doc.createElement("paymentId");
            idElement.setTextContent(obj.getPaymentId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Payment obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }


}
