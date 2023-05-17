package ma.ya.datepicker.core.listeners

import android.widget.Toast
import ma.ya.datepicker.core.DatePickerFragment
import ma.ya.datepicker.core.models.DatePickerInput
import ma.ya.datepicker.core.models.DisabledDays
import java.time.LocalDate

interface DatePickerListener {

	/**
	 * @param canNotGo Due to [DisabledDays.minAvailableDay] maybe will be `true` & in that case
	 * you may want to show a [Toast] error for example if you want
	 */
	fun onPreviousMonthClick(canNotGo: Boolean)

	/**
	 * @param canNotGo Due to [DisabledDays.maxAvailableDay] maybe will be `true` & in that case
	 * you may want to show a [Toast] error for example if you want
	 */
	fun onNextMonthClick(canNotGo: Boolean)

	/**
	 * - Called only in two cases
	 *
	 * 1. If [day] is selected by user click not on re-selection as it is ignored.
	 * 2. If this [DatePickerListener] is set before displaying the [DatePickerFragment]
	 * **&&** [DatePickerInput.startingDay] is available so not disabled via [DatePickerInput.disabledDays]
	 * **&&** [DatePickerInput.callListenerOnStartingDay] is `true`
	 */
	fun onDaySelected(day: LocalDate)

}
