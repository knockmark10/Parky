package com.markoid.parky.core.date.formatters

import com.markoid.parky.core.date.enums.FormatType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.joda.time.Duration
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DateXTimeFormatterTest {

    private val SUT = DateTimeFormatter

    private lateinit var subjectTime: DateTime

    @Before
    fun setup() {
        subjectTime = DateTime(2021, 5, 6, 14, 23)
    }

    @Test
    fun format_monthDayYear_mustReturnCorrectValue() {
        val result = SUT.format(FormatType.MONTH_DAY_YEAR, subjectTime)
        assertEquals("Mayo 06 2021", result)
    }

    @Test
    fun format_monthDayYearHour_mustReturnCorrectValue() {
        val result = SUT.format(FormatType.MONTH_DAY_YEAR_HOUR, subjectTime)
        assertEquals("Mayo 06 2021 14:23", result)
    }

    @Test
    fun format_weekMonthDayHour_mustReturnCorrectValue() {
        val result = SUT.format(FormatType.WEEK_MONTH_DAY_HOUR, subjectTime)
        assertEquals("Jueves Mayo 06 14:23", result)
    }

    @Test
    fun format_weekMonthDayYearHour_mustReturnCorrectValue() {
        val result = SUT.format(FormatType.WEEK_MONTH_DAY_YEAR_HOUR, subjectTime)
        assertEquals("Jueves Mayo 06 2021 14:23", result)
    }
}
