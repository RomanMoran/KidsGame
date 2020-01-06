package android1601.itstep.org.kidsgame.program.ext.data

import android.content.SharedPreferences
import android1601.itstep.org.kidsgame.program.di.ObjectMapperProvider
import android1601.itstep.org.kidsgame.program.ext.tryOr
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val mapper by ObjectMapperProvider()

inline fun <reified T> SharedPreferences.delegate(key: String? = null) = delegate<T?>(key, null)

inline fun <reified T> SharedPreferences.delegate(key: String? = null, default: T) = object
    : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>) = tryOr(default) {
        mapper.fromJson(getString(key ?: property.name, null), T::class.java) as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        edit().putString(key ?: property.name, mapper.toJson(value)).apply()
    }
}
