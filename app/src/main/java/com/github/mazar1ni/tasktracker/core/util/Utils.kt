package com.github.mazar1ni.tasktracker.core.util

import android.content.Context
import android.text.format.DateUtils
import com.github.mazar1ni.tasktracker.R
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object Utils {

    fun String.generateMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    fun getStringOfDay(date: Long, context: Context) =
        if (DateUtils.isToday(date))
            context.getString(R.string.today)
        else if (DateUtils.isToday(date - DateUtils.DAY_IN_MILLIS))
            context.getString(R.string.tomorrow)
        else if (DateUtils.isToday(date + DateUtils.DAY_IN_MILLIS))
            context.getString(R.string.yesterday)
        else
            getDateFormat(date)

    fun localDateTimeToEpoch(date: Long, dueHour: Int?, dueMinute: Int?) =
        getLocalDateTimeFromEpoch(date).withHour(dueHour ?: 0)
            .withMinute(dueMinute ?: 0).atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli()

    fun getLocalDateTimeFromEpoch(
        date: Long,
        zoneId: ZoneId = ZoneOffset.systemDefault()
    ): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), zoneId)

    fun getDateFormat(
        date: Long,
        zoneId: ZoneId = ZoneOffset.systemDefault(),
        format: String = Constants.DUE_DATE_FORMAT
    ): String = getLocalDateTimeFromEpoch(date, zoneId).format(DateTimeFormatter.ofPattern(format))

    fun getTimeFormat(hour: Int, minute: Int, format: String = Constants.DUE_TIME_FORMAT): String =
        LocalDateTime.now().withHour(hour).withMinute(minute)
            .format(DateTimeFormatter.ofPattern(format))

}