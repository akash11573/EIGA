package com.eiga.service.tmdb;

import com.eiga.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TmdbService {
    private TmdbClient client = new TmdbClient();

    public List<Movie> searchMovies(String query) throws Exception {
        if (query == null || query.trim().isEmpty()) {
            throw new Exception("Search query cannot be empty");
        }
        
        String encodedQuery = query.trim().replace(" ", "%20");
        JSONObject response = client.get("/search/movie?query=" + encodedQuery);
        JSONArray results = response.getJSONArray("results");
        
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);
            Movie m = new Movie();
            m.setTmdbId(String.valueOf(obj.optInt("id")));
            m.setTitle(obj.optString("title"));
            m.setOverview(obj.optString("overview"));
            m.setPosterPath(obj.optString("poster_path", null));
            m.setBackdropPath(obj.optString("backdrop_path", null));
            m.setRating(obj.optDouble("vote_average", 0.0));
            String relDate = obj.optString("release_date", null);
            if (relDate != null && !relDate.isEmpty()) {
                try { m.setReleaseDate(LocalDate.parse(relDate)); } catch (Exception ignored) {}
            }
            movies.add(m);
        }
        return movies;
    }

    public Movie getMovieDetails(String tmdbId) throws Exception {
        JSONObject obj = client.get("/movie/" + tmdbId);
        Movie m = new Movie();
        m.setTmdbId(String.valueOf(obj.optInt("id")));
        m.setTitle(obj.optString("title"));
        m.setOverview(obj.optString("overview"));
        m.setPosterPath(obj.optString("poster_path", null));
        m.setBackdropPath(obj.optString("backdrop_path", null));
        m.setRuntime(obj.optInt("runtime", 0));
        m.setRating(obj.optDouble("vote_average", 0.0));
        m.setLanguage(obj.optString("original_language", "en"));
        
        String relDate = obj.optString("release_date", null);
        if (relDate != null && !relDate.isEmpty()) {
            try { m.setReleaseDate(LocalDate.parse(relDate)); } catch (Exception ignored) {}
        }
        
        JSONArray genresArr = obj.optJSONArray("genres");
        if (genresArr != null) {
            List<String> genres = new ArrayList<>();
            for (int i = 0; i < genresArr.length(); i++) {
                genres.add(genresArr.getJSONObject(i).optString("name"));
            }
            m.setGenres(String.join(", ", genres));
        }
        
        m.setTrailerKey(fetchTrailerKey(tmdbId));
        
        return m;
    }

    private String fetchTrailerKey(String tmdbId) {
        try {
            JSONObject response = client.get("/movie/" + tmdbId + "/videos");
            JSONArray results = response.optJSONArray("results");
            if (results != null) {
                String teaser = null;
                for (int i = 0; i < results.length(); i++) {
                    JSONObject v = results.getJSONObject(i);
                    if ("YouTube".equalsIgnoreCase(v.optString("site"))) {
                        String type = v.optString("type");
                        if ("Trailer".equalsIgnoreCase(type)) {
                            return v.optString("key");
                        } else if ("Teaser".equalsIgnoreCase(type)) {
                            teaser = v.optString("key");
                        }
                    }
                }
                if (teaser != null) return teaser;
                
                for (int i = 0; i < results.length(); i++) {
                    JSONObject v = results.getJSONObject(i);
                    if ("YouTube".equalsIgnoreCase(v.optString("site"))) {
                        return v.optString("key");
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}
