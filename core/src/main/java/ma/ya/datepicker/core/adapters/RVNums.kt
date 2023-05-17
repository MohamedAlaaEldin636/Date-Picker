@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ma.ya.datepicker.core.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ma.ya.datepicker.core.R
import ma.ya.datepicker.core.databinding.ItemNumBinding
import ma.ya.datepicker.core.extensions.toMonthDay
import ma.ya.datepicker.core.models.DisabledDays
import java.time.LocalDate

class RVNums(
	private var list: List<LocalDate> = emptyList(),
	selectedDay: LocalDate? = null,
	private var disabledDays: DisabledDays = DisabledDays(),
	private val onItemClick: (view: View, day: LocalDate) -> Unit
) : RecyclerView.Adapter<VHNums>() {

	var selectedDay: LocalDate? = selectedDay
		private set

	override fun getItemCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHNums {
		return VHNums(
			ItemNumBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		) { view, day, position ->
			val oldPosition = list.indexOfFirst { it == selectedDay }

			if (oldPosition == position) return@VHNums

			selectedDay = list[position]
			if (oldPosition != -1) {
				notifyItemChanged(oldPosition)
			}
			notifyItemChanged(position)

			onItemClick(view, day)
		}
	}

	override fun onBindViewHolder(holder: VHNums, position: Int) {
		val currentDay = list[position]

		holder.bind(currentDay, currentDay == selectedDay, currentDay.isDisabled(), position)
	}

	private fun LocalDate.isDisabled(): Boolean {
		return dayOfWeek in disabledDays.daysOfWeek
			|| toMonthDay() in disabledDays.daysOfMonth
			|| this in disabledDays.daysOfYear
			|| (disabledDays.minAvailableDay != null && this < disabledDays.minAvailableDay)
			|| (disabledDays.maxAvailableDay != null && this > disabledDays.maxAvailableDay)
	}

	@SuppressLint("NotifyDataSetChanged")
	fun submitChange(
		list: List<LocalDate> = emptyList(),
		selectedDay: LocalDate? = null,
		disabledDays: DisabledDays = DisabledDays(),
	) {
		this.list = list
		this.disabledDays = disabledDays
		this.selectedDay = if (selectedDay != null && selectedDay.isDisabled().not()) {
			selectedDay
		}else {
			null
		}
		notifyDataSetChanged()
	}

	@SuppressLint("NotifyDataSetChanged")
	fun submitList(
		list: List<LocalDate>,
	) {
		this.list = list
		notifyDataSetChanged()
	}

	fun isEmpty() = list.isEmpty()

}

class VHNums(
	private val binding: ItemNumBinding,
	private val onItemClick: (view: View, day: LocalDate, position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

	init {
		binding.textView.setOnClickListener {
			val day = binding.textView.tag as? LocalDate ?: return@setOnClickListener
			val position = binding.textView.getTag(R.id.position_tag) as? Int ?: return@setOnClickListener
			val isDisabled = binding.textView.getTag(R.id.is_disabled) as? Boolean ?: return@setOnClickListener

			if (isDisabled.not()) {
				onItemClick(it, day, position)
			}
		}
	}

	fun bind(day: LocalDate, isSelected: Boolean, isDisabled: Boolean, position: Int) {
		val context = binding.root.context ?: return

		binding.textView.tag = day
		binding.textView.setTag(R.id.position_tag, position)
		binding.textView.setTag(R.id.is_disabled, isDisabled)

		binding.textView.text = day.dayOfMonth.toString()

		val drawable = when {
			isDisabled -> null
			isSelected -> ContextCompat.getDrawable(
				context,
				R.drawable.circle_primary
			)
			else -> null
		}
		binding.textView.background = drawable

		val colorRes = when {
			isDisabled -> R.color.disabled_day
			isSelected -> R.color.white
			else -> R.color.available_day
		}
		binding.textView.setTextColor(
			ContextCompat.getColor(
				context, colorRes
			)
		)
	}

}