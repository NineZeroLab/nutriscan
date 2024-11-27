package com.mdev.feature_analytics.presentation.analytics_page.presentation

sealed class AnalyticsPageEvent {
    data object GetAnalyticsData: AnalyticsPageEvent()

}