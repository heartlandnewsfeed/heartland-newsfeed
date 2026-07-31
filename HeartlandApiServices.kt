package com.heartlandnewsfeed.app.data.remote

import com.heartlandnewsfeed.app.data.models.NewsArticle
import com.heartlandnewsfeed.app.data.models.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.serialization.Serializable

// Local news source response - for parsing Heartland Newsfeed website
@Serializable
data class WebScrapedNews(
    val articles: List<NewsArticle>
)

/**
 * Retrofit service for fetching news articles from Heartland Newsfeed
 * This uses a custom parser/scraper to extract structured data
 */
interface HeartlandNewsService {
    
    /**
     * Fetch latest news articles
     */
    @GET("api/news/latest")
    suspend fun getLatestNews(
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1
    ): NewsResponse

    /**
     * Get news by category
     */
    @GET("api/news/category")
    suspend fun getNewsByCategory(
        @Query("category") category: String,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1
    ): NewsResponse

    /**
     * Search news articles
     */
    @GET("api/news/search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): NewsResponse

    /**
     * Get featured/trending articles
     */
    @GET("api/news/trending")
    suspend fun getTrendingNews(
        @Query("limit") limit: Int = 10
    ): NewsResponse
}

/**
 * Service for radio metadata and streaming information
 * This handles station information, currently playing, etc.
 */
interface HeartlandRadioService {
    
    /**
     * Get all available radio stations with their metadata
     */
    @GET("api/radio/stations")
    suspend fun getStations(): List<com.heartlandnewsfeed.app.data.models.RadioStation>

    /**
     * Get currently playing track/show info
     */
    @GET("api/radio/now-playing")
    suspend fun getNowPlaying(
        @Query("station_id") stationId: String
    ): com.heartlandnewsfeed.app.data.models.CurrentlyPlaying

    /**
     * Get station details
     */
    @GET("api/radio/station/{id}")
    suspend fun getStationDetails(
        @Query("id") stationId: String
    ): com.heartlandnewsfeed.app.data.models.RadioStation
}

/**
 * Constants for Heartland Newsfeed API
 */
object HeartlandApiConstants {
    const val BASE_URL = "https://www.heartlandnewsfeed.com/"
    const val API_TIMEOUT = 30L // seconds

    // Radio stream URLs
    const val HEARTLAND_NEWSFEED_RADIO_NETWORK_PRIMARY = 
        "https://live365.com/api/station/stream/a24952"
    const val HEARTLAND_NEWSFEED_RADIO_NETWORK_BACKUP = 
        "https://stream.abovecast.net/heartland"
    const val REVOLUTION_RADIO_PRIMARY = 
        "https://live365.com/api/station/stream/a99459"
    const val REVOLUTION_RADIO_BACKUP = 
        "https://stream.abovecast.net/revolution"
    const val HOME_FOR_HOLIDAYS_RADIO = 
        "https://live365.com/api/station/stream/" // To be updated when available
    
    // News categories available on Heartland Newsfeed
    val NEWS_CATEGORIES = listOf(
        "all",
        "illinois-news",
        "missouri",
        "national",
        "weather",
        "sports",
        "entertainment",
        "business",
        "opinion"
    )
}
