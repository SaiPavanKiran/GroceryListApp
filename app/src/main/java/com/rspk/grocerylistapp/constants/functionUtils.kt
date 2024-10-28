package com.rspk.grocerylistapp.constants

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDecimal(input: String): String {
    return try {
        val numberFormat = NumberFormat.getInstance(Locale.getDefault())
        val number = numberFormat.parse(input)

        if (number != null && number.toFloat() == number.toInt().toFloat()) {
            number.toInt().toString()
        } else {
            input
        }
    } catch (e: Exception) {
        input
    }
}

fun getDateFromMillis(): String =
    SimpleDateFormat.getDateInstance().format(System.currentTimeMillis())

fun getStringFormattedYear():String =
    SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())

fun getStringFormattedMonth():String =
    SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())

fun getStringFormattedMonthPlusYear():String =
    SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(Date())

fun getStringFormattedFullDate():String =
    SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(Date())


suspend fun getInstallationId(): String = FirebaseInstallations.getInstance().id.await()