package ma.ya.datepicker.core.extensions

import android.content.Context
import android.os.Build
import java.util.Locale

fun Context.getCurrentLocaleOrNull(): Locale? {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		if (resources.configuration.locales.size() > 1) {
			resources.configuration.locales[0]
		}else {
			null
		}
	}else {
		@Suppress("DEPRECATION")
		resources.configuration.locale
	}
}
