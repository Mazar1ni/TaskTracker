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
import java.util.*

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

    fun getStringOfDayFoTaskList(date: Long, context: Context): String {
        if (date < System.currentTimeMillis())
            return context.getString(R.string.overdue)

        if (DateUtils.isToday(date))
            return context.getString(R.string.today)

        if (DateUtils.isToday(date - DateUtils.DAY_IN_MILLIS))
            return context.getString(R.string.tomorrow)

        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        calendar.set(Calendar.DAY_OF_WEEK, 7)

        if (date < calendar.time.time)
            return context.getString(R.string.this_week)

        calendar.set(Calendar.WEEK_OF_YEAR, currentWeek + 1)
        calendar.set(Calendar.DAY_OF_WEEK, 7)

        if (date < calendar.time.time)
            return context.getString(R.string.next_week)

        val monthCalendar = Calendar.getInstance()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        monthCalendar.add(Calendar.MONTH, 1)
        monthCalendar.add(Calendar.DAY_OF_MONTH, -1)

        if (date < monthCalendar.time.time)
            return context.getString(R.string.this_month)

        monthCalendar.add(Calendar.MONTH, 1)

        if (date < monthCalendar.time.time)
            return context.getString(R.string.next_month)

        return context.getString(R.string.later)
    }

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