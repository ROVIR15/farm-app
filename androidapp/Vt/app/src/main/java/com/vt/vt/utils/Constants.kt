package com.vt.vt.utils

import com.vt.vt.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

const val breed_api = "https://dog.ceo/api/breeds/"
const val local_url = "http://103.189.164.95/"

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

// Notification
const val BLOCK_ID = "BLOCK_ID"
const val HABIT_TITLE = "HABIT_TITLE"
const val NOTIFICATION_CHANNEL_ID = "notify-channel"
const val NOTIF_UNIQUE_WORK: String = "NOTIF_UNIQUE_WORK"
