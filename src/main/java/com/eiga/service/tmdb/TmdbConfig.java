package com.eiga.service.tmdb;

public class TmdbConfig {
    public static final String API_BASE_URL = "https://api.themoviedb.org/3";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    public static final String IMAGE_ORIGINAL_URL = "https://image.tmdb.org/t/p/original";
    
    public static String getApiKey() {
        return System.getenv("TMDB_API_KEY");
    }
}
