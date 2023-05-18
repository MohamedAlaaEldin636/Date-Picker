package ma.ya.datepicker.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import ma.ya.datepicker.app.databinding.ActivityMainBinding
import ma.ya.datepicker.core.DatePickerFragment
import ma.ya.datepicker.core.listeners.DatePickerListener
import ma.ya.datepicker.core.models.DatePickerInput
import ma.ya.datepicker.core.models.DisabledDays
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay

class MainActivity : AppCompatActivity(), DatePickerListener {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
		binding.lifecycleOwner = this

		DatePickerFragment.createAndPutInContainer(
			this,
			R.id.fragmentContainerView,
			DatePickerInput(
				disabledDays = DisabledDays(
					minAvailableDay = LocalDate.now().minusDays(1),
					maxAvailableDay = LocalDate.now().plusMonths(2).plusDays(1),
					daysOfMonth = listOf(
						MonthDay.of(Month.MAY, 19)
					),
					daysOfYear = listOf(
						LocalDate.of(2023, Month.MAY, 21)
					),
					daysOfWeek = listOf(
						DayOfWeek.FRIDAY
					)
				),
				showYearInText = true, // Default
			),
			this
		)
	}

	override fun onDaySelected(day: LocalDate) {
		Toast.makeText(this, "ON DAY SEELCTION $day", Toast.LENGTH_SHORT).show()
	}

	override fun onNextMonthClick(canNotGo: Boolean) {
		if (canNotGo) {
			Toast.makeText(this, "can't go more than this month", Toast.LENGTH_SHORT).show()
		}
	}

	override fun onPreviousMonthClick(canNotGo: Boolean) {
		if (canNotGo) {
			Toast.makeText(this, "can't go less than this month", Toast.LENGTH_SHORT).show()
		}
	}
}