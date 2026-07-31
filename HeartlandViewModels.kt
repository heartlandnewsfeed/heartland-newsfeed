package com.heartlandnewsfeed.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartlandnewsfeed.app.data.models.NewsUiState
import com.heartlandnewsfeed.app.data.models.RadioUiState
import com.heartlandnewsfeed.app.data.models.AppUiState
import com.heartlandnewsfeed.app.data.models.RadioStation
import com.heartlandnewsfeed.app.data.repository.NewsRepository
import com.heartlandnewsfeed.app.data.repository.RadioRepository
import com.heartlandnewsfeed.app.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling news-related operations
 */
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLatestNews()
    }

    fun loadLatestNews(page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            newsRepository.getLatestNews(page = page).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                articles = result.data,
                                isLoading = false,
                                currentPage = page,
                                selectedCategory = "all"
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun loadNewsByCategory(category: String, page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, selectedCategory = category) }
            
            newsRepository.getNewsByCategory(category, page = page).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                articles = result.data,
                                isLoading = false,
                                currentPage = page
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            newsRepository.searchNews(query).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                articles = result.data,
                                isLoading = false
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun loadTrendingNews() {
        viewModelScope.launch {
            newsRepository.getTrendingNews().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(articles = result.data)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(error = result.message)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * ViewModel for handling radio-related operations
 */
@HiltViewModel
class RadioViewModel @Inject constructor(
    private val radioRepository: RadioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RadioUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStations()
    }

    fun loadStations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingStations = true, error = null) }
            
            radioRepository.getStations().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                stations = result.data,
                                isLoadingStations = false,
                                selectedStation = result.data.firstOrNull { station -> station.isLive }
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingStations = false,
                                error = result.message
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoadingStations = true) }
                    }
                }
            }
        }
    }

    fun selectStation(station: RadioStation) {
        _uiState.update {
            it.copy(
                selectedStation = station,
                isPlaying = true
            )
        }
        
        // Load now playing info for the selected station
        if (station.isLive) {
            loadNowPlaying(station.id)
        }
    }

    fun togglePlayback() {
        _uiState.update {
            it.copy(isPlaying = !it.isPlaying)
        }
    }

    fun stopPlayback() {
        _uiState.update {
            it.copy(isPlaying = false)
        }
    }

    private fun loadNowPlaying(stationId: String) {
        viewModelScope.launch {
            radioRepository.getNowPlaying(stationId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(currentlyPlaying = result.data)
                        }
                    }
                    is Result.Error -> {
                        // Don't show error for now playing, as it's optional
                    }
                    else -> {}
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * ViewModel for managing overall app state
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    val newsViewModel: NewsViewModel,
    val radioViewModel: RadioViewModel
) : ViewModel() {

    private val _appState = MutableStateFlow(AppUiState())
    val appState = _appState.asStateFlow()

    fun selectTab(tabName: String) {
        _appState.update { it.copy(selectedTab = tabName) }
    }

    fun toggleDarkMode() {
        _appState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }
}
