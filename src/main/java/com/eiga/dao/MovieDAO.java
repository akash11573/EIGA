package com.eiga.dao;

import com.eiga.model.Movie;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    private static final String FILE_NAME = "movies.xml";

    public List<Movie> findAll() {
        List<Movie> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("movie");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Movie obj = new Movie();
                    obj.setMovieId(XmlUtil.getElementValue(element, "movieId"));
                    obj.setTmdbId(XmlUtil.getElementValue(element, "tmdbId"));
                    obj.setTitle(XmlUtil.getElementValue(element, "title"));
                    obj.setOverview(XmlUtil.getElementValue(element, "overview"));
                    obj.setPosterPath(XmlUtil.getElementValue(element, "posterPath"));
                    obj.setBackdropPath(XmlUtil.getElementValue(element, "backdropPath"));
                    
                    String relDate = XmlUtil.getElementValue(element, "releaseDate");
                    if (relDate != null && !relDate.trim().isEmpty() && !relDate.equals("null")) {
                        try { obj.setReleaseDate(LocalDate.parse(relDate)); } catch (Exception ignored) {}
                    }
                    
                    String runtime = XmlUtil.getElementValue(element, "runtime");
                    if (runtime != null && !runtime.trim().isEmpty() && !runtime.equals("null")) {
                        try { obj.setRuntime(Integer.parseInt(runtime)); } catch (Exception ignored) {}
                    }
                    
                    obj.setGenres(XmlUtil.getElementValue(element, "genres"));
                    obj.setLanguage(XmlUtil.getElementValue(element, "language"));
                    obj.setStatus(XmlUtil.getElementValue(element, "status"));
                    obj.setTrailerKey(XmlUtil.getElementValue(element, "trailerKey"));
                    
                    String rating = XmlUtil.getElementValue(element, "rating");
                    if (rating != null && !rating.trim().isEmpty() && !rating.equals("null")) {
                        try { obj.setRating(Double.parseDouble(rating)); } catch (Exception ignored) {}
                    }
                    
                    obj.setCast(XmlUtil.getElementValue(element, "cast"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Movie findById(String id) {
        for (Movie obj : findAll()) {
            if (id != null && id.equals(obj.getMovieId())) return obj;
        }
        return null;
    }

    public Movie findByTmdbId(String tmdbId) {
        for (Movie obj : findAll()) {
            if (tmdbId != null && tmdbId.equals(obj.getTmdbId())) return obj;
        }
        return null;
    }

    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name);
        child.setTextContent(value == null ? "" : value);
        parent.appendChild(child);
    }

    public void save(Movie obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            
            NodeList nodeList = doc.getElementsByTagName("movie");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element existing = (Element) nodeList.item(i);
                if (obj.getMovieId().equals(XmlUtil.getElementValue(existing, "movieId"))) {
                    doc.getDocumentElement().removeChild(existing);
                    break;
                }
            }
            
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("movie");
            
            addChild(doc, newElement, "movieId", obj.getMovieId());
            addChild(doc, newElement, "tmdbId", obj.getTmdbId());
            addChild(doc, newElement, "title", obj.getTitle());
            addChild(doc, newElement, "overview", obj.getOverview());
            addChild(doc, newElement, "posterPath", obj.getPosterPath());
            addChild(doc, newElement, "backdropPath", obj.getBackdropPath());
            addChild(doc, newElement, "releaseDate", obj.getReleaseDate() != null ? obj.getReleaseDate().toString() : "");
            addChild(doc, newElement, "runtime", String.valueOf(obj.getRuntime()));
            addChild(doc, newElement, "genres", obj.getGenres());
            addChild(doc, newElement, "language", obj.getLanguage());
            addChild(doc, newElement, "status", obj.getStatus());
            addChild(doc, newElement, "trailerKey", obj.getTrailerKey());
            addChild(doc, newElement, "rating", String.valueOf(obj.getRating()));
            addChild(doc, newElement, "cast", obj.getCast());
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
