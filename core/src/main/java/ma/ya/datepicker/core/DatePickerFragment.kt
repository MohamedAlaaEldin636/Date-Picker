@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ma.ya.datepicker.core

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ya.datepicker.core.databinding.FragmentDatePickerBinding
import ma.ya.datepicker.core.extensions.toYearMonth
import ma.ya.datepicker.core.listeners.DatePickerListener
import ma.ya.datepicker.core.models.DatePickerInput
import ma.ya.datepicker.core.models.orEmpty
import java.time.LocalDate

class DatePickerFragment : Fragment() {

	companion object {
		private const val KEY_ARGS_DATE_PICKER_INPUT = "KEY_ARGS_DATE_PICKER_INPUT"

		fun create(input: DatePickerInput, listener: DatePickerListener?): DatePickerFragment {
			return DatePickerFragment().apply {
				arguments = bundleOf(
					KEY_ARGS_DATE_PICKER_INPUT to input
				)

				this.listener = listener
			}
		}

		fun createAndPutInContainer(
			owner: Fragment,
			@IdRes idOfContainer: Int,
			input: DatePickerInput,
			listener: DatePickerListener?,
		): DatePickerFragment {
			val fragment = create(input, listener)

			owner.childFragmentManager.beginTransaction()
				.replace(idOfContainer, fragment)
				.commit()

			return fragment
		}

		fun createAndPutInContainer(
			owner: FragmentActivity,
			@IdRes idOfContainer: Int,
			input: DatePickerInput,
			listener: DatePickerListener?,
		): DatePickerFragment {
			val fragment = create(input, listener)

			owner.supportFragmentManager.beginTransaction()
				.replace(idOfContainer, fragment)
				.commit()

			return fragment
		}
	}

	private val viewModel by viewModels<DatePickerViewModel>()

	private var binding: FragmentDatePickerBinding? = null

	var listener: DatePickerListener? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.input = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			arguments?.getSerializable(KEY_ARGS_DATE_PICKER_INPUT, DatePickerInput::class.java)
		}else {
			@Suppress("DEPRECATION")
			arguments?.getSerializable(KEY_ARGS_DATE_PICKER_INPUT) as? DatePickerInput
		}).orEmpty()
		viewModel.currentYearMonth.value = viewModel.input.startingDay.toYearMonth()

		viewModel.adapterNums.submitChange(
			emptyList(), viewModel.input.startingDay, viewModel.input.disabledDays
		)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_picker, container, false)

		binding?.viewModel = viewModel
		binding?.lifecycleOwner = viewLifecycleOwner

		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding?.recyclerViewDays?.layoutManager = object : GridLayoutManager(
			requireContext(), 7, VERTICAL, false
		) {
			override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams?): Boolean {
				if (layoutParams != null) {
					layoutParams.width = width / 7
					//layoutParams.height = layoutParams.width
				}

				return super.checkLayoutParams(layoutParams)
			}
		}
		binding?.recyclerViewDays?.adapter = viewModel.adapterDays

		binding?.recyclerViewNums?.layoutManager = object : GridLayoutManager(
			requireContext(), 7, VERTICAL, false
		) {
			override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams?): Boolean {
				if (layoutParams != null) {
					layoutParams.width = width / 7
					//layoutParams.height = layoutParams.width
				}

				return super.checkLayoutParams(layoutParams)
			}
		}
		binding?.recyclerViewNums?.adapter = viewModel.adapterNums

		viewModel.currentYearMonth.distinctUntilChanged().observe(viewLifecycleOwner) {
			val wasEmpty = viewModel.adapterNums.isEmpty()

			// Changes the adapter nums isa only, not disabled days nor selection isa.
			viewModel.onChangeOfCurrentYearMonth(it)

			if (wasEmpty && viewModel.input.callListenerOnStartingDay) {
				viewModel.adapterNums.selectedDay?.also { selectedDay ->
					listener?.onDaySelected(selectedDay)
				}
			}
		}
	}

	override fun onDestroyView() {
		binding = null

		super.onDestroyView()
	}

	fun getSelectedDayOrNull(): LocalDate? {
		return viewModel.adapterNums.selectedDay
	}

}
