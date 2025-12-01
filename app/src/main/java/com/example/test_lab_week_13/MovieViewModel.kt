package com.example.test_lab_week_13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    // StateFlow untuk list movie
    private val _popularMovies = MutableStateFlow(
        emptyList<Movie>()
    )
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    // StateFlow untuk error message
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    // fetch movies from the API
    private fun fetchPopularMovies() {
        // launch a coroutine in viewModelScope
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                // catch exception dari Flow
                .catch { e ->
                    _error.value = "An exception occurred: ${e.message}"
                }
                // collect value dari Flow
                .collect { movies ->
                    // DI SINI kita balikin filter/sort:
                    // urutkan descending berdasarkan popularity
                    _popularMovies.value = movies
                        .sortedByDescending { it.popularity }
                }
        }
    }
}
