package com.vt.vt.core.data.source.base.refresh

import android.os.Handler
import android.os.Looper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface SwipeRefreshListener {
    fun onRefreshRequested()

    fun setupSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout) {
        var isSwipeRefreshEnabled = true

        swipeRefreshLayout.setOnRefreshListener {
            if (isSwipeRefreshEnabled) {
                onRefreshRequested()
                isSwipeRefreshEnabled = false
                swipeRefreshLayout.isRefreshing = true

                // Re-enable swipe-to-refresh after 3 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    isSwipeRefreshEnabled = true
                    swipeRefreshLayout.isRefreshing = false
                }, 2000)
            } else {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}