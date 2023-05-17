package ma.ya.datepicker.core.extensions

import java.time.LocalDate
import java.time.MonthDay
import java.time.YearMonth

fun LocalDate.toYearMonth(): YearMonth {
	return YearMonth.of(year, month)
}

fun LocalDate.toMonthDay(): MonthDay {
	return MonthDay.of(month, dayOfMonth)
}

fun YearMonth.toLocalDate(dayOfMonth: Int): LocalDate = LocalDate.of(year, month, dayOfMonth)
