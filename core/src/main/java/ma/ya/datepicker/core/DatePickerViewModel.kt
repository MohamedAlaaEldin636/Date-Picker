package ma.ya.datepicker.core

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ma.ya.datepicker.core.adapters.RVDays
import ma.ya.datepicker.core.adapters.RVNums
import ma.ya.datepicker.core.extensions.getCurrentLocaleOrNull
import ma.ya.datepicker.core.extensions.toActualString
import ma.ya.datepicker.core.extensions.toLocalDate
import ma.ya.datepicker.core.extensions.toYearMonth
import ma.ya.datepicker.core.models.DatePickerInput
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle

class DatePickerViewModel(
	application: Application
) : AndroidViewModel(application) {

	var input = DatePickerInput()

	/** Current year month shown on screen isa. */
	val currentYearMonth: MutableLiveData<YearMonth> = MutableLiveData(YearMonth.now())

	val textCurrentYearMonth = currentYearMonth.map {
		getApplication<Application>()/*.getCurrentLocaleOrNull()?*/.let { context ->
			val month = it.month.toActualString(context)//getDisplayName(TextStyle.FULL, locale)

			if (input.showYearInText) "$month ${it.year}" else month
		}
	}

	val adapterDays = RVDays()

	val adapterNums = RVNums { view: View, day: LocalDate ->
		val fragment = view.findFragmentOrNull<DatePickerFragment>() ?: return@RVNums

		if (day.toYearMonth() != currentYearMonth.value) {
			currentYearMonth.value = day.toYearMonth()
		}

		fragment.listener?.onDaySelected(day)
	}

	fun goToPrevMonth(view: View) {
		val day = input.disabledDays.minAvailableDay ?: return

		val canGo = day.toYearMonth() < currentYearMonth.value
		if (canGo) {
			currentYearMonth.value = currentYearMonth.value?.minusMonths(1)
		}

		val fragment = view.findFragmentOrNull<DatePickerFragment>() ?: return

		fragment.listener?.onPreviousMonthClick(canGo.not())
	}

	fun goToNextMonth(view: View) {
		val day = input.disabledDays.maxAvailableDay ?: return

		val canGo = day.toYearMonth() > currentYearMonth.value
		if (canGo) {
			currentYearMonth.value = currentYearMonth.value?.plusMonths(1)
		}

		val fragment = view.findFragmentOrNull<DatePickerFragment>() ?: return

		fragment.listener?.onNextMonthClick(canGo.not())
	}

	fun onChangeOfCurrentYearMonth(yearMonth: YearMonth) {
		// List of all days
		val allDays = mutableListOf<LocalDate>()

		// Only days from current month
		val currentMonthDays = buildList {
			for (num in 1..28) {
				add(yearMonth.toLocalDate(num))
			}

			for (num in 29..31) {
				if (yearMonth.isValidDay(num)) {
					add(yearMonth.toLocalDate(num))
				}
			}
		}

		// Days from previous month
		val weekDaysCount = DayOfWeek.values().size
		val previousMonthDaysToAdd = currentMonthDays.first().dayOfWeek.value.minus(
			DayOfWeek.SATURDAY.value
		).let {
			if (it < 0) it.plus(weekDaysCount) else it
		}

		// Starting Day Of Current Month -> for now
		var day = currentMonthDays.first().minusDays(previousMonthDaysToAdd.toLong())
		// Add previous month days if needed
		if (currentMonthDays.size % weekDaysCount != 0) {
			repeat(previousMonthDaysToAdd) {
				allDays += day
				day = day.plusDays(1)
			}
		}

		// Add current month days
		allDays.addAll(currentMonthDays)

		// Add next month days if needed
		val remainder = allDays.size % weekDaysCount
		if (remainder != 0) {
			val countOfNextMonthDaysToBeAdded = weekDaysCount.minus(remainder)
			day = currentMonthDays.last()
			repeat(countOfNextMonthDaysToBeAdded) {
				day = day.plusDays(1)
				allDays += day
			}
		}

		adapterNums.submitList(allDays.toList())
	}

	private inline fun <reified F : Fragment> View.findFragmentOrNull(): F? {
		return try {
			findFragment()
		}catch (e: IllegalStateException) {
			null
		}
	}

}
