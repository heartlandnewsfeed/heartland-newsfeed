package com.heartlandnewsfeed.app.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.material3.SnackbarHostState
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extension functions and utility functions for the Heartland Newsfeed app
 */

// ==================== Date & Time Extensions ====================

/**
 * Format date string to a readable format
 * Input: "2026-07-30"
 * Output: "Jul 30, 2026"
 */
fun String.formatDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val date = inputFormat.parse(this) ?: return this
        outputFormat.format(date)
    } catch (e: Exception) {
        this
    }
}

/**
 * Format date string to "2 hours ago" format
 */
fun String.getTimeAgo(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val date = inputFormat.parse(this) ?: return this
        val now = System.currentTimeMillis()
        val ago = now - date.time

        when {
            ago < 60_000 -> "Just now"
            ago < 3_600_000 -> "${ago / 60_000} minutes ago"
            ago < 86_400_000 -> "${ago / 3_600_000} hours ago"
            ago < 604_800_000 -> "${ago / 86_400_000} days ago"
            else -> this
        }
    } catch (e: Exception) {
        this
    }
}

/**
 * Check if date string is from today
 */
fun String.isToday(): Boolean {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = inputFormat.parse(this) ?: return false
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        inputFormat.format(date) == today
    } catch (e: Exception) {
        false
    }
}

// ==================== String Extensions ====================

/**
 * Truncate string to specified length with ellipsis
 */
fun String.truncate(maxLength: Int): String {
    return if (length > maxLength) {
        substring(0, maxLength - 3) + "..."
    } else {
        this
    }
}

/**
 * Capitalize first letter of string
 */
fun String.capitalizeFirstLetter(): String {
    return if (isNotEmpty()) {
        this[0].uppercase() + substring(1)
    } else {
        this
    }
}

/**
 * Check if string is a valid URL
 */
fun String.isValidUrl(): Boolean {
    return try {
        java.net.URL(this)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Clean HTML tags from string (basic cleanup)
 */
fun String.stripHtmlTags(): String {
    return this
        .replace(Regex("<[^>]*>"), "")
        .replace("&nbsp;", " ")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .trim()
}

// ==================== Network Extensions ====================

/**
 * Check if device has internet connectivity
 */
fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

/**
 * Check if device is on WiFi
 */
fun Context.isWiFiConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}

/**
 * Check if device is on cellular network
 */
fun Context.isCellularConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}

// ==================== List Extensions ====================

/**
 * Chunk list into smaller lists of specified size
 */
fun <T> List<T>.chunk(size: Int): List<List<T>> {
    if (size <= 0) throw IllegalArgumentException("Size must be positive")
    
    return mutableListOf<List<T>>().apply {
        var index = 0
        while (index < this@chunk.size) {
            add(this@chunk.subList(index, minOf(index + size, this@chunk.size)))
            index += size
        }
    }
}

/**
 * Find distinct items by a selector
 */
fun <T, K> List<T>.distinctBy(selector: (T) -> K): List<T> {
    val set = mutableSetOf<K>()
    return filter { set.add(selector(it)) }
}

// ==================== Exception Handling ====================

/**
 * Safe try-catch wrapper
 */
inline fun <T> safeTry(block: () -> T, default: T? = null): T? {
    return try {
        block()
    } catch (e: Exception) {
        default
    }
}

/**
 * Log exception safely
 */
fun Exception.logError(tag: String = "HeartlandApp") {
    android.util.Log.e(tag, "Error: ${message}", this)
}

// ==================== Compose State Extensions ====================

/**
 * Show snackbar message
 */
suspend fun SnackbarHostState.showMessage(message: String) {
    showSnackbar(message)
}

/**
 * Show snackbar with action
 */
suspend fun SnackbarHostState.showMessageWithAction(
    message: String,
    actionLabel: String,
    duration: androidx.compose.material3.SnackbarDuration = androidx.compose.material3.SnackbarDuration.Short
): androidx.compose.material3.SnackbarResult {
    return showSnackbar(message, actionLabel, duration)
}

// ==================== Number Extensions ====================

/**
 * Format bytes to human-readable format
 */
fun Long.formatBytes(): String {
    val units = arrayOf("B", "KB", "MB", "GB")
    var size = toDouble()
    var unitIndex = 0
    
    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }
    
    return String.format(Locale.US, "%.2f %s", size, units[unitIndex])
}

/**
 * Format number with thousand separators
 */
fun Int.formatNumberWithSeparators(): String {
    return String.format(Locale.US, "%,d", this)
}

// ==================== Collection Extensions ====================

/**
 * Get random item from list
 */
fun <T> List<T>.random(): T? {
    return if (isNotEmpty()) {
        get(Random().nextInt(size))
    } else {
        null
    }
}

/**
 * Shuffle list (returns new list, doesn't modify original)
 */
fun <T> List<T>.shuffled(): List<T> {
    return this.toMutableList().apply { shuffle() }
}

/**
 * Safe get with default value
 */
fun <T> List<T>.getOrElse(index: Int, default: T): T {
    return if (index >= 0 && index < size) get(index) else default
}

// ==================== Validation Extensions ====================

/**
 * Check if email is valid (basic validation)
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    return emailRegex.matches(this)
}

/**
 * Check if phone number is valid (basic validation)
 */
fun String.isValidPhoneNumber(): Boolean {
    val phoneRegex = "^[\\d\\s\\-+()]*$".toRegex()
    return this.length >= 10 && phoneRegex.matches(this)
}

/**
 * Check if string contains only letters
 */
fun String.isLettersOnly(): Boolean {
    return this.matches("^[a-zA-Z]+$".toRegex())
}

/**
 * Check if string contains only numbers
 */
fun String.isNumbersOnly(): Boolean {
    return this.matches("^[0-9]+$".toRegex())
}

// ==================== Convenience Objects ====================

/**
 * Default shared instance for logging
 */
object AppLogger {
    private const val TAG = "HeartlandApp"
    
    fun d(message: String) {
        android.util.Log.d(TAG, message)
    }
    
    fun e(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            android.util.Log.e(TAG, message, throwable)
        } else {
            android.util.Log.e(TAG, message)
        }
    }
    
    fun w(message: String) {
        android.util.Log.w(TAG, message)
    }
    
    fun i(message: String) {
        android.util.Log.i(TAG, message)
    }
}

/**
 * Constants for the app
 */
object AppConstants {
    const val ARTICLE_PAGE_SIZE = 20
    const val NEWS_SEARCH_DEBOUNCE_MS = 500L
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val MAX_RETRY_ATTEMPTS = 3
    const val CACHE_DURATION_MINUTES = 60
    
    // Shared preferences keys
    const val PREF_LAST_NEWS_UPDATE = "last_news_update"
    const val PREF_SELECTED_STATION = "selected_station"
    const val PREF_DARK_MODE = "dark_mode"
    const val PREF_FAVORITE_ARTICLES = "favorite_articles"
}
