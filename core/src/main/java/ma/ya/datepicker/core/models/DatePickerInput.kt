package ma.ya.datepicker.core.models

import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.MonthDay

fun DatePickerInput?.orEmpty() = this ?: DatePickerInput()

data class DatePickerInput(
	val startingDay: LocalDate = LocalDate.now(),
	val callListenerOnStartingDay: Boolean = true,
	val disabledDays: DisabledDays = DisabledDays(),
	val showYearInText: Boolean = true,
) : Serializable

/**
 * @param minAvailableDay Ex. usage is if you want calendar only available from today and to next
 * 3 May this year or only available for 10 years or 1 year etc...
 */
data class DisabledDays(
	val daysOfWeek: List<DayOfWeek> = emptyList(),
	val daysOfMonth: List<MonthDay> = emptyList(),
	val daysOfYear: List<LocalDate> = emptyList(),
	val minAvailableDay: LocalDate? = null,
	val maxAvailableDay: LocalDate? = null,
) : Serializable
