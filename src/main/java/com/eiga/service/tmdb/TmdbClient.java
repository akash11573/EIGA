package com.eiga.service.tmdb;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TmdbClient {
    private final HttpClient client;

    public TmdbClient() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public JSONObject get(String endpoint) throws Exception {
        String apiKey = TmdbConfig.getApiKey();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new Exception("TMDB_API_KEY environment variable is not configured.");
        }
        
        String url = TmdbConfig.API_BASE_URL + endpoint;
        if (url.contains("?")) {
            url += "&api_key=" + apiKey;
        } else {
            url += "?api_key=" + apiKey;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("TMDB API Error: HTTP " + response.statusCode());
        }
        
        return new JSONObject(response.body());
    }
}
