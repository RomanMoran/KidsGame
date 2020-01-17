package android1601.itstep.org.kidsgame.program.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android1601.itstep.org.kidsgame.program.Utility.LocaleHelper
import android1601.itstep.org.kidsgame.program.ext.data.delegate


class LocalStorage(context: Context) : Preferences {

    private val sharedPrefs by lazy { context.getSharedPreferences("sharedprefs", MODE_PRIVATE) }

    override var language: String by sharedPrefs.delegate("appLanguage", LocaleHelper.getLanguage())
    override var countEnter: Int by sharedPrefs.delegate("countEnter", 0)
    override var playMarketVisited: Boolean by sharedPrefs.delegate("playMarketVisited", false)

}
