package com.dagachi.koapp_android.widget.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.TimeZone

/* 날짜와 관련된 함수 및 변수 파일 */
class DateUtils {
    companion object {
        // 오늘 날짜 구하기
        fun getTodayDate(): ZonedDateTime =
            Instant.now().atZone(TimeZone.getDefault().toZoneId())

        // 오늘 날짜 기준, 일주일의 시작 날짜 구하기
        fun getWeekStartDate(): LocalDateTime {
            val startDate: LocalDateTime = getTodayDate().toLocalDateTime().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            return startDate
        }
    }
}