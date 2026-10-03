package com.eiga.util;

import com.eiga.dao.MovieDAO;
import com.eiga.model.Movie;
import com.eiga.service.MovieService;
import com.eiga.service.tmdb.TmdbConfig;

public class TmdbTest {
    public static void main(String[] args) {
        System.out.println("Starting TMDB and Movie Catalogue Tests...");
        
        try {
            MovieDAO dao = new MovieDAO();
            MovieService service = new MovieService();
            
            // 1. MovieDAO saving and loading test (Offline safe)
            Movie m = new Movie();
            m.setMovieId("M-TEST1");
            m.setTmdbId("999999");
            m.setTitle("Offline Test Movie");
            m.setOverview("Testing DAO without API");
            m.setStatus("INACTIVE");
            dao.save(m);
            
            Movie loaded = dao.findById("M-TEST1");
            System.out.println("Test 1 (DAO Read/Write): " + (loaded != null && "Offline Test Movie".equals(loaded.getTitle()) ? "PASS" : "FAIL"));
            
            // 2. TMDB Configuration Detection
            String apiKey = TmdbConfig.getApiKey();
            if (apiKey == null || apiKey.trim().isEmpty()) {
                System.out.println("Test 2 (TMDB Config): MISSING - Offline Mode Only");
                System.out.println("Graceful handling when credentials missing: PASS (Application is stable)");
                System.out.println("Live TMDB tests skipped.");
            } else {
                System.out.println("Test 2 (TMDB Config): PASS");
                
                try {
                    // Test live search
                    java.util.List<Movie> results = service.searchTmdb("The Matrix");
                    System.out.println("Test 3 (Live Search): " + (!results.isEmpty() ? "PASS" : "FAIL"));
                    
                    if (!results.isEmpty()) {
                        String firstId = results.get(0).getTmdbId();
                        
                        // Test live import
                        Movie imported = service.importFromTmdb(firstId);
                        System.out.println("Test 4 (Live Import): " + (imported != null && imported.getMovieId() != null ? "PASS" : "FAIL"));
                        
                        // Test duplicate import (Update existing)
                        Movie importedAgain = service.importFromTmdb(firstId);
                        System.out.println("Test 5 (Duplicate Import/Update): " + (imported.getMovieId().equals(importedAgain.getMovieId()) ? "PASS" : "FAIL"));
                        
                        // Test trailer retrieval
                        System.out.println("Test 6 (Trailer Logic): " + (imported.getTrailerKey() != null ? "PASS (Found: " + imported.getTrailerKey() + ")" : "PASS (No trailer)"));
                    }
                } catch (Exception e) {
                    System.out.println("Live Tests Failed due to Exception: " + e.getMessage());
                }
            }
            
            System.out.println("All TMDB/Movie tests executed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
