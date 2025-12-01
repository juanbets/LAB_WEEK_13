package com.example.test_lab_week_13

import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    // API key kamu
    private val apiKey = "4ebb92bb9df6dd74c5c162ab29810a81"

    // fetch movies from the API
    // this function returns a Flow of Movie objects
    // a Flow is a type of coroutine that can emit multiple values
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // emit the list of popular movies from the API
            emit(movieService.getPopularMovies(apiKey).results)
            // use Dispatchers.IO to run this coroutine on a shared pool of threads
        }.flowOn(Dispatchers.IO)
    }
}
