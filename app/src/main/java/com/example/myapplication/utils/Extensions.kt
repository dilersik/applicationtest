package com.example.myapplication.utils

import android.content.Context
import android.util.Base64
import android.util.TypedValue
import java.security.MessageDigest


fun String.toSHA256(): String {
    val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return Base64.encodeToString(digest, Base64.NO_WRAP)
}

fun Float.toPx(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
)