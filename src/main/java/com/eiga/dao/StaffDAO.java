package com.eiga.dao;

import com.eiga.model.Staff;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private static final String FILE_NAME = "staff.xml";

    public List<Staff> findAll() {
        List<Staff> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("staffMember");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Staff obj = new Staff();
                    obj.setStaffId(XmlUtil.getElementValue(element, "staffId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Staff findById(String id) {
        for (Staff obj : findAll()) {
            if (id != null && id.equals(obj.getStaffId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Staff obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("staffMember");
            
            Element idElement = doc.createElement("staffId");
            idElement.setTextContent(obj.getStaffId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Staff obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }


}
