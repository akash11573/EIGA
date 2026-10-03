package com.eiga.dao;

import com.eiga.model.Review;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private static final String FILE_NAME = "reviews.xml";

    public List<Review> findAll() {
        List<Review> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("review");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Review obj = new Review();
                    obj.setReviewId(XmlUtil.getElementValue(element, "reviewId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Review findById(String id) {
        for (Review obj : findAll()) {
            if (id != null && id.equals(obj.getReviewId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Review obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("review");
            
            Element idElement = doc.createElement("reviewId");
            idElement.setTextContent(obj.getReviewId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Review obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }


}
