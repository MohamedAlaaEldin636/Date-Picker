package ma.ya.datepicker.core.extensions

import android.content.Context
import ma.ya.datepicker.core.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay
import java.time.YearMonth

fun LocalDate.toYearMonth(): YearMonth {
	return YearMonth.of(year, month)
}

fun LocalDate.toMonthDay(): MonthDay {
	return MonthDay.of(month, dayOfMonth)
}

fun YearMonth.toLocalDate(dayOfMonth: Int): LocalDate = LocalDate.of(year, month, dayOfMonth)

fun DayOfWeek.toActualString(context: Context): String {
	val stringRes = when (this) {
		DayOfWeek.MONDAY -> R.string.monday
		DayOfWeek.TUESDAY -> R.string.tuesday
		DayOfWeek.WEDNESDAY -> R.string.wednesday
		DayOfWeek.THURSDAY -> R.string.thursday
		DayOfWeek.FRIDAY -> R.string.friday
		DayOfWeek.SATURDAY -> R.string.saturday
		DayOfWeek.SUNDAY -> R.string.sunday
	}

	return context.getString(stringRes)
}

fun Month.toActualString(context: Context): String {
	val stringRes = when (this) {
		Month.JANUARY -> R.string.january
		Month.FEBRUARY -> R.string.february
		Month.MARCH -> R.string.march
		Month.APRIL -> R.string.april
		Month.MAY -> R.string.may
		Month.JUNE -> R.string.june
		Month.JULY -> R.string.july
		Month.AUGUST -> R.string.august
		Month.SEPTEMBER -> R.string.september
		Month.OCTOBER -> R.string.october
		Month.NOVEMBER -> R.string.november
		Month.DECEMBER -> R.string.december
	}

	return context.getString(stringRes)
}
