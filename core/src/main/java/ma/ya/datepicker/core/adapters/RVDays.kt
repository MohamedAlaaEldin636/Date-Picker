@file:Suppress("unused")

package ma.ya.datepicker.core.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ma.ya.datepicker.core.databinding.ItemDayBinding
import ma.ya.datepicker.core.extensions.getCurrentLocaleOrNull
import ma.ya.datepicker.core.extensions.toActualString
import java.time.DayOfWeek
import java.time.format.TextStyle

class RVDays(
	startingDay: DayOfWeek = DayOfWeek.SATURDAY,
	numOfDays: Int = 7,
) : RecyclerView.Adapter<VHDays>() {

	private var list = calculateList(startingDay, numOfDays)

	override fun getItemCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHDays {
		return VHDays(
			ItemDayBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			),
		)
	}

	override fun onBindViewHolder(holder: VHDays, position: Int) {
		holder.bind(list[position])
	}

	private fun calculateList(
		startingDay: DayOfWeek = DayOfWeek.SATURDAY,
		numOfDays: Int = 7,
	) : List<DayOfWeek> {
		return List(numOfDays) {
			val startingValue = startingDay.value

			var newValue = startingValue + (it % 7)
			if (newValue > 7) newValue -= 7

			DayOfWeek.of(newValue)
		}
	}

	@SuppressLint("NotifyDataSetChanged")
	fun submitChange(
		startingDay: DayOfWeek = DayOfWeek.SATURDAY,
		numOfDays: Int = 7,
	) {
		list = calculateList(startingDay, numOfDays)

		notifyDataSetChanged()
	}

}

class VHDays(
	private val binding: ItemDayBinding,
) : RecyclerView.ViewHolder(binding.root) {

	fun bind(day: DayOfWeek) {
		//val currentLocale = binding.root.context?.getCurrentLocaleOrNull()
		val context = binding.root.context ?: return

		val name = day.toActualString(context)//.getDisplayName(TextStyle.FULL, currentLocale ?: return).orEmpty()
		binding.textView.text = name
	}

}
