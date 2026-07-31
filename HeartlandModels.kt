package com.heartlandnewsfeed.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

// News Models
@Serializable
data class NewsArticle(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val author: String? = null,
    @SerialName("published_at")
    val publishedAt: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val category: String,
    val source: String = "Heartland Newsfeed",
    val url: String? = null
)

@Serializable
data class NewsResponse(
    val articles: List<NewsArticle>,
    val total: Int,
    val page: Int
)

// Radio Stream Models
data class RadioStation(
    val id: String,
    val name: String,
    val description: String,
    val streamUrl: String,
    val backupUrl: String? = null,
    val logo: String? = null,
    val genre: String,
    val isLive: Boolean = true,
    val launchDate: String? = null // For stations not yet live
)

data class CurrentlyPlaying(
    val stationId: String,
    val title: String? = null,
    val artist: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

// Category Models
@Serializable
data class NewsCategory(
    val id: String,
    val name: String,
    val icon: String? = null
)

// UI State Models
data class NewsUiState(
    val articles: List<NewsArticle> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String = "all",
    val currentPage: Int = 1
)

data class RadioUiState(
    val stations: List<RadioStation> = emptyList(),
    val selectedStation: RadioStation? = null,
    val isPlaying: Boolean = false,
    val currentlyPlaying: CurrentlyPlaying? = null,
    val error: String? = null,
    val isLoadingStations: Boolean = false
)

data class AppUiState(
    val newsState: NewsUiState = NewsUiState(),
    val radioState: RadioUiState = RadioUiState(),
    val isDarkMode: Boolean = false,
    val selectedTab: String = "news"
)
