package android1601.itstep.org.kidsgame.program.ext.ui

import android.app.Dialog
import android.content.Context
import android1601.itstep.org.kidsgame.R
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.checkSelfPermission

fun Context.checkPermissionCompat(permission: String) = checkSelfPermission(this, permission)

inline fun Context.dialog(@LayoutRes layoutRes: Int, init: Dialog.() -> Unit = {}): Dialog {
    return Dialog(this, R.style.AppDialog).apply {
        setContentView(layoutRes)
        setCancelable(false)
        init(this)
    }
}