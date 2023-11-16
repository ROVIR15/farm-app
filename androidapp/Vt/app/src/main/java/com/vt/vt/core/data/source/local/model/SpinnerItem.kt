package com.vt.vt.core.data.source.local.model


data class SpinnerCategoriesItem(val id: Int, val name: String) {
    override fun toString(): String {
        return name
    }
}
