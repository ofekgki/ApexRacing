package com.example.apexracing.utilities

import com.example.apexracing.models.Race
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val JERUSALEM = ZoneId.of("Asia/Jerusalem")
private val UTC = ZoneId.of("UTC")

fun Race.toStartDateTimeUtc(): ZonedDateTime? {
    // API נותן date + time (UTC עם Z). לפעמים time יכול להיות null
    val t = time ?: return null
    return try {
        val instant = Instant.parse("${date}T$t") // למשל: 2026-03-08T04:00:00Z
        instant.atZone(UTC)
    } catch (e: Exception) {
        null
    }
}

fun Race.toStartDateTimeLocal(): ZonedDateTime? =
    toStartDateTimeUtc()?.withZoneSameInstant(JERUSALEM)

fun formatRaceDateLocal(race: Race): String {
    val zdt = race.toStartDateTimeLocal()
    return if (zdt != null) {
        zdt.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy • HH:mm"))
    } else {
        race.date // fallback
    }
}

data class Countdown(val days: Long, val hours: Long, val minutes: Long, val seconds: Long)

fun countdownTo(target: ZonedDateTime, now: ZonedDateTime = ZonedDateTime.now(JERUSALEM)): Countdown {
    val diff = target.toInstant().epochSecond - now.toInstant().epochSecond
    val s = diff.coerceAtLeast(0)

    val days = s / 86400
    val hours = (s % 86400) / 3600
    val minutes = (s % 3600) / 60
    val seconds = s % 60

    return Countdown(days, hours, minutes, seconds)
}

fun formatCountdown(c: Countdown): String {
    // 02d:05h:10m:03s
    return "%02dd : %02dh : %02dm : %02ds".format(c.days, c.hours, c.minutes, c.seconds)
}