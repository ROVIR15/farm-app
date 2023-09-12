package com.vt.vt.utils

import com.vt.vt.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

const val apiKey = "ce84baa5ea6072805228db04568c7ba9"
const val wilayah_indonesia = "https://www.emsifa.com/api-wilayah-indonesia/api/provinces.json"

val loggingInterceptor = if (BuildConfig.DEBUG) {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
} else {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
}

const val FILE_PREFERENCE_DATASTORE = "user_session"

const val EXTRA_HISTORY_TRANSACTION = "EXTRA_TRANSACTION"

// Edit Text Type Flag
const val EDIT_TEXT_NAME = 1
const val EDIT_TEXT_EMAIL = 2
const val EDIT_TEXT_SIGN_IN_PASS = 3
const val EDIT_TEXT_SIGN_UP_PASS = 4
const val EDIT_TEXT_PHONE_NUMBER = 5

// Edit Text Background Flag
const val EDIT_TEXT_BLACK = "black"
const val EDIT_TEXT_GREEN = "green"
const val EDIT_TEXT_RED = "red"
