package android1601.itstep.org.kidsgame.program.ext.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import android1601.itstep.org.kidsgame.program.ext.data.mapper
import java.lang.reflect.Type
import kotlin.reflect.KClass


fun <T : Any> Context.intent(
    cls: KClass<T>,
    vararg args: Pair<String, Any?>
) = Intent(this, cls.java).apply {
    args.forEach { (key, value) -> putExtra(key, mapper.toJson(value)) }
}

fun Context.putExtras(vararg args: Pair<String, Any>) = Intent().apply {
    args.forEach { (key, value) -> putExtra(key, mapper.toJson(value)) }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Intent.get(key: String, objectType: Class<T>): T? {
    return try {
        mapper.fromJson(getStringExtra(key), objectType) as T
    } catch (exception: TypeCastException) {
        null
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Intent.get(key: String, objectType: Type): T? {
    return try {
        mapper.fromJson(getStringExtra(key), objectType) as T
    } catch (exception: TypeCastException) {
        null
    }
}

fun <T : Activity> Context.start(
    cls: KClass<T>,
    vararg args: Pair<String, Any>
) {
    startActivity(intent(cls, *args))
}

fun <T : Activity> Activity.startForResult(
    cls: KClass<T>,
    requestCode: Int,
    vararg args: Pair<String, Any>
) {
    startActivityForResult(intent(cls, *args), requestCode)
}

fun <T : Activity> Fragment.startForResult(
    cls: KClass<T>,
    requestCode: Int,
    vararg args: Pair<String, Any?>
) {
    startActivityForResult(requireContext().intent(cls, *args), requestCode)
}

fun <T : Activity> Activity.startAffinity(
    cls: KClass<T>,
    vararg args: Pair<String, Any>
) {
    startActivity(intent(cls, *args))
    ActivityCompat.finishAffinity(this)
}

fun <T : Activity> Context.startForResult(
    cls: KClass<T>,
    vararg args: Pair<String, Any>,
    requestCode: Int
) {
    startActivityForResult(this as Activity, intent(cls, *args), requestCode, null)
}

fun <T : Activity> Context.startActivityWithFlagReorder(cls: KClass<T>) {
    val intent = Intent(this, cls.java).apply { flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT }
    startActivity(intent)
}

fun <T : Activity> Context.startActivityNew(cls: KClass<T>) {
    val activity = this as Activity
    activity.finishAffinity()
    val intent = Intent(this, cls.java)
    startActivity(intent)
}
