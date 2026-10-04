package com.eiga.dao;

import com.eiga.model.User;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String FILE_NAME = "users.xml";

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    User obj = new User();
                    obj.setUserId(XmlUtil.getElementValue(element, "userId"));
                    obj.setFullName(XmlUtil.getElementValue(element, "fullName"));
                    obj.setEmail(XmlUtil.getElementValue(element, "email"));
                    obj.setMobile(XmlUtil.getElementValue(element, "mobile"));
                    obj.setPasswordHash(XmlUtil.getElementValue(element, "passwordHash"));
                    obj.setRole(XmlUtil.getElementValue(element, "role"));
                    obj.setTheatreId(XmlUtil.getElementValue(element, "theatreId"));
                    obj.setStatus(XmlUtil.getElementValue(element, "status"));
                    String createdAt = XmlUtil.getElementValue(element, "createdAt");
                    if (createdAt != null && !createdAt.isEmpty()) {
                        obj.setCreatedAt(LocalDateTime.parse(createdAt));
                    }
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public User findById(String id) {
        for (User obj : findAll()) {
            if (id != null && id.equals(obj.getUserId())) return obj;
        }
        return null;
    }
    
    public User findByEmail(String email) {
        for (User obj : findAll()) {
            if (email != null && email.equalsIgnoreCase(obj.getEmail())) return obj;
        }
        return null;
    }

    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name);
        child.setTextContent(value == null ? "" : value);
        parent.appendChild(child);
    }

        public void save(User obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("user");
            for (int i = 0; i < nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if (obj.getUserId().equals(XmlUtil.getElementValue(existing, "userId"))) {
                    doc.getDocumentElement().removeChild(existing); break;
                }
            }
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("user");
            
            addChild(doc, newElement, "userId", obj.getUserId());
            addChild(doc, newElement, "fullName", obj.getFullName());
            addChild(doc, newElement, "email", obj.getEmail());
            addChild(doc, newElement, "mobile", obj.getMobile());
            addChild(doc, newElement, "passwordHash", obj.getPasswordHash());
            addChild(doc, newElement, "role", obj.getRole());
            addChild(doc, newElement, "theatreId", obj.getTheatreId());
            addChild(doc, newElement, "status", obj.getStatus());
            addChild(doc, newElement, "createdAt", obj.getCreatedAt() != null ? obj.getCreatedAt().toString() : "");
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
