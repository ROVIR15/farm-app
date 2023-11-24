package com.vt.vt.core.data.source.base

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal open class DebouncingQueryTextListener(
    lifecycle: Lifecycle,
    private val onDebouncingQueryTextListener: (String?) -> Unit
) : SearchView.OnQueryTextListener {
    private val coroutineScope = lifecycle.coroutineScope
    private var searchJob: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(500)
                onDebouncingQueryTextListener(newText)
            }
        }
        return false
    }

    fun destroy() {
        searchJob?.cancel()
    }

}