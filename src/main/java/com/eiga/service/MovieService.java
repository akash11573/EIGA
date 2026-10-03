package com.eiga.service;

import com.eiga.dao.MovieDAO;
import com.eiga.model.Movie;
import com.eiga.service.tmdb.TmdbService;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {
    private MovieDAO movieDAO = new MovieDAO();
    private TmdbService tmdbService = new TmdbService();

    public List<Movie> getAllLocalMovies() {
        return movieDAO.findAll();
    }
    
    public List<Movie> getPublicCatalogue() {
        return movieDAO.findAll().stream()
            .filter(m -> !"INACTIVE".equals(m.getStatus()))
            .collect(Collectors.toList());
    }

    public Movie getMovieById(String movieId) {
        return movieDAO.findById(movieId);
    }
    
    public boolean isMovieCurrentlyPlaying(String movieId) {
        return false;
    }

    public List<Movie> searchTmdb(String query) throws Exception {
        return tmdbService.searchMovies(query);
    }

    public Movie importFromTmdb(String tmdbId) throws Exception {
        Movie externalMovie = tmdbService.getMovieDetails(tmdbId);
        Movie localMovie = movieDAO.findByTmdbId(tmdbId);
        
        if (localMovie != null) {
            externalMovie.setMovieId(localMovie.getMovieId());
            externalMovie.setStatus(localMovie.getStatus());
        } else {
            externalMovie.setMovieId("M" + System.currentTimeMillis());
            externalMovie.setStatus("NOT_IN_EIGA_THEATRES");
        }
        
        movieDAO.save(externalMovie);
        return externalMovie;
    }
}
