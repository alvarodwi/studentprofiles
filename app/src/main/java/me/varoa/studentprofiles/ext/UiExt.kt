package me.varoa.studentprofiles.ext

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import me.varoa.studentprofiles.core.domain.model.AppTheme

private fun Fragment.createSnackbar(
    message: String,
    duration: Int,
): Snackbar = Snackbar.make(requireView(), message, duration)

fun Fragment.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    createSnackbar(message, duration).show()
}

fun Fragment.snackbar(
    message: String,
    anchorView: View,
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    createSnackbar(message, duration).apply { setAnchorView(anchorView) }.show()
}

fun Fragment.toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun toggleAppTheme(value: String) {
    AppCompatDelegate.setDefaultNightMode(
        when (value) {
            AppTheme.LIGHT.name -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK.name -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        },
    )
}

fun String.decodeHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}
