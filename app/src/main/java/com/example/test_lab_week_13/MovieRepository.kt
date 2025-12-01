package com.example.test_lab_week_13

import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDao
import com.example.test_lab_week_13.database.MovieDatabase
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {

    // API key kamu
    private val apiKey = "4ebb92bb9df6dd74c5c162ab29810a81"

    // Sekarang: cek dulu DB, kalau kosong baru call API dan simpan ke DB
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // Check if there are movies saved in the database
            val movieDao: MovieDao = movieDatabase.movieDao()
            val savedMovies = movieDao.getMovies()

            if (savedMovies.isEmpty()) {
                // Tidak ada di DB → fetch dari API
                val movies = movieService.getPopularMovies(apiKey).results

                // Simpan hasil API ke DB
                movieDao.addMovies(movies)

                // Emit data dari API
                emit(movies)
            } else {
                // Ada di DB → langsung emit dari DB
                emit(savedMovies)
            }
        }.flowOn(Dispatchers.IO)
    }
}
