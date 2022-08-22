package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getDate(afterDays: Int? = 0): String {
        val calender = Calendar.getInstance()
        if (afterDays != 0) {
            calender.add(Calendar.DAY_OF_YEAR, afterDays!!)
        }
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calender.time)
    }
}