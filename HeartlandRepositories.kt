package com.heartlandnewsfeed.app.data.repository

import com.heartlandnewsfeed.app.data.models.NewsArticle
import com.heartlandnewsfeed.app.data.models.RadioStation
import com.heartlandnewsfeed.app.data.models.CurrentlyPlaying
import com.heartlandnewsfeed.app.data.remote.HeartlandNewsService
import com.heartlandnewsfeed.app.data.remote.HeartlandRadioService
import com.heartlandnewsfeed.app.data.remote.HeartlandApiConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for news operations with error handling and caching
 */
@Singleton
class NewsRepository @Inject constructor(
    private val newsService: HeartlandNewsService
) {
    private val _articleCache = MutableStateFlow<List<NewsArticle>>(emptyList())
    val articleCache = _articleCache.asStateFlow()

    fun getLatestNews(page: Int = 1, limit: Int = 20): Flow<Result<List<NewsArticle>>> = flow {
        try {
            emit(Result.loading())
            val response = newsService.getLatestNews(limit, page)
            _articleCache.value = response.articles
            emit(Result.success(response.articles))
        } catch (e: Exception) {
            emit(Result.error("Failed to fetch news: ${e.message}"))
        }
    }

    fun getNewsByCategory(
        category: String,
        page: Int = 1,
        limit: Int = 20
    ): Flow<Result<List<NewsArticle>>> = flow {
        try {
            emit(Result.loading())
            val response = newsService.getNewsByCategory(category, limit, page)
            emit(Result.success(response.articles))
        } catch (e: Exception) {
            emit(Result.error("Failed to fetch category news: ${e.message}"))
        }
    }

    fun searchNews(query: String, limit: Int = 20): Flow<Result<List<NewsArticle>>> = flow {
        try {
            emit(Result.loading())
            val response = newsService.searchNews(query, limit)
            emit(Result.success(response.articles))
        } catch (e: Exception) {
            emit(Result.error("Failed to search news: ${e.message}"))
        }
    }

    fun getTrendingNews(limit: Int = 10): Flow<Result<List<NewsArticle>>> = flow {
        try {
            emit(Result.loading())
            val response = newsService.getTrendingNews(limit)
            emit(Result.success(response.articles))
        } catch (e: Exception) {
            emit(Result.error("Failed to fetch trending news: ${e.message}"))
        }
    }

    fun getCachedArticles(): List<NewsArticle> = _articleCache.value
}

/**
 * Repository for radio station operations
 */
@Singleton
class RadioRepository @Inject constructor(
    private val radioService: HeartlandRadioService
) {
    private val _stationsCache = MutableStateFlow<List<RadioStation>>(emptyList())
    val stationsCache = _stationsCache.asStateFlow()

    init {
        // Initialize with hardcoded stations (fallback)
        _stationsCache.value = getDefaultStations()
    }

    fun getStations(): Flow<Result<List<RadioStation>>> = flow {
        try {
            emit(Result.loading())
            val stations = try {
                radioService.getStations()
            } catch (e: Exception) {
                // Fallback to default stations if API fails
                getDefaultStations()
            }
            _stationsCache.value = stations
            emit(Result.success(stations))
        } catch (e: Exception) {
            // Use cached or default stations
            emit(Result.success(_stationsCache.value))
        }
    }

    fun getNowPlaying(stationId: String): Flow<Result<CurrentlyPlaying>> = flow {
        try {
            emit(Result.loading())
            val nowPlaying = radioService.getNowPlaying(stationId)
            emit(Result.success(nowPlaying))
        } catch (e: Exception) {
            emit(Result.error("Failed to fetch now playing: ${e.message}"))
        }
    }

    private fun getDefaultStations(): List<RadioStation> {
        return listOf(
            RadioStation(
                id = "heartland-newsfeed",
                name = "Heartland Newsfeed Radio Network",
                description = "News and sports talk, featuring Liberty Radio Network and Sports Byline USA",
                streamUrl = HeartlandApiConstants.HEARTLAND_NEWSFEED_RADIO_NETWORK_PRIMARY,
                backupUrl = HeartlandApiConstants.HEARTLAND_NEWSFEED_RADIO_NETWORK_BACKUP,
                genre = "News/Sports Talk",
                isLive = true
            ),
            RadioStation(
                id = "revolution-radio",
                name = "Revolution Radio",
                description = "The best variety of Christian music - CCM, Christian Rock, Metal, Rap & Hip-Hop",
                streamUrl = HeartlandApiConstants.REVOLUTION_RADIO_PRIMARY,
                backupUrl = HeartlandApiConstants.REVOLUTION_RADIO_BACKUP,
                genre = "Christian Music",
                isLive = true
            ),
            RadioStation(
                id = "home-for-holidays",
                name = "Home for the Holidays Radio",
                description = "The best in holiday music - seasonal station",
                streamUrl = HeartlandApiConstants.HOME_FOR_HOLIDAYS_RADIO,
                genre = "Holiday Music",
                isLive = false,
                launchDate = "November 1"
            )
        )
    }

    fun getCachedStations(): List<RadioStation> = _stationsCache.value
}

/**
 * Generic Result wrapper for handling API responses
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val message: String) : Result<T>()
    data class Loading<T>(val data: T? = null) : Result<T>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String) = Error<T>(message)
        fun <T> loading(data: T? = null) = Loading(data)
    }

    fun getDataOrNull(): T? = when (this) {
        is Success -> data
        is Loading -> data
        is Error -> null
    }

    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun getErrorMessage(): String? = (this as? Error)?.message
}
