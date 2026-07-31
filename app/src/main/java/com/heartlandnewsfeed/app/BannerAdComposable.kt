package com.heartlandnewsfeed.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.heartlandnewsfeed.app.advertising.AdMobHelper

/**
 * Composable Banner Ad View
 * 
 * Usage:
 * BannerAd(
 *     adUnitId = "ca-app-pub-3940256099942544/6300978111",
 *     modifier = Modifier.fillMaxWidth()
 * )
 */
@Composable
fun BannerAd(
    adUnitId: String,
    modifier: Modifier = Modifier,
    adSize: AdSize = AdSize.BANNER
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(adSize)
                    setAdUnitId(adUnitId)
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

/**
 * Composable Large Banner Ad (90dp height)
 */
@Composable
fun LargeBannerAd(
    adUnitId: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.LARGE_BANNER)
                    setAdUnitId(adUnitId)
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        )
    }
}

/**
 * Test Banner Ad (for development)
 * Shows a test ad without needing real AdMob setup
 */
@Composable
fun TestBannerAd(modifier: Modifier = Modifier) {
    val testAdUnitId = "ca-app-pub-3940256099942544/6300978111"
    BannerAd(
        adUnitId = testAdUnitId,
        modifier = modifier
    )
}
