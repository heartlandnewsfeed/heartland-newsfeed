package com.heartlandnewsfeed.app.advertising

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Google AdMob configuration for banner ads
 * Banner ads are non-intrusive and display at bottom of screen
 */

object AdMobConstants {
    // Test Ad Unit IDs (for development/testing)
    // Replace with your real Ad Unit IDs from AdMob console when going live
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val BANNER_AD_UNIT_ID = "ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx"
    
    // Test Device ID
    const val TEST_DEVICE_ID = "33BE2250B43518CCDA7DE426D04EE232"
    
    // Banner ad heights
    const val BANNER_HEIGHT_DP = 50
    const val LARGE_BANNER_HEIGHT_DP = 90
}

/**
 * Hilt module for AdMob initialization
 */
@Module
@InstallIn(SingletonComponent::class)
object AdMobModule {
    
    @Provides
    @Singleton
    fun provideAdMobHelper(@ApplicationContext context: Context): AdMobHelper {
        return AdMobHelper(context)
    }
}

/**
 * Helper class for AdMob operations
 */
class AdMobHelper(private val context: Context) {
    
    init {
        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(context)
    }
    
    /**
     * Create an AdRequest for loading ads
     */
    fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }
    
    /**
     * Get the appropriate Ad Unit ID
     */
    fun getBannerAdUnitId(useTestAds: Boolean = false): String {
        return if (useTestAds) {
            AdMobConstants.TEST_BANNER_AD_UNIT_ID
        } else {
            AdMobConstants.BANNER_AD_UNIT_ID
        }
    }
}
