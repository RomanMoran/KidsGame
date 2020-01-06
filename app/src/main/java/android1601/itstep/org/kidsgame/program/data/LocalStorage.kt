package android1601.itstep.org.kidsgame.program.data

import android.content.Context
import android.content.Context.MODE_PRIVATE


class LocalStorage(context: Context) : Preferences {

    private val sharedPrefs by lazy { context.getSharedPreferences("sharedprefs", MODE_PRIVATE) }


}
