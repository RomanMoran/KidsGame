package android1601.itstep.org.kidsgame.program.activity.main

import android.os.Bundle
import android.widget.TextView
import android1601.itstep.org.kidsgame.BuildConfig
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.ext.lang.EN_LOCALE
import android1601.itstep.org.kidsgame.program.ext.lang.RU_LOCALE
import android1601.itstep.org.kidsgame.program.ext.ui.dialog
import android1601.itstep.org.kidsgame.program.ui.base.BaseActivity
import com.franmontiel.localechanger.LocaleChanger
import com.franmontiel.localechanger.utils.ActivityRecreationHelper
import kotlinx.android.synthetic.main.activity_main_kotlin.*

class MainKotlinActivity : BaseActivity() {

    override val layoutRes: Int = R.layout.activity_main_kotlin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openEggs.setOnClickListener { navigator.showKinderView() }
        openPuzzle.setOnClickListener { navigator.showOpenPuzzlesKotlinView() }
        openCollection.setOnClickListener { navigator.showCollectionView() }
        aboutUs.setOnClickListener { }
        rgLanguages.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbEng -> LocaleChanger.setLocale(EN_LOCALE)
                else -> LocaleChanger.setLocale(RU_LOCALE)
            }
            ActivityRecreationHelper.recreate(this, true)
        }
        checkIfNeedRateDialog()
    }

    private fun checkIfNeedRateDialog() {
        preferences.countEnter = preferences.countEnter.plus(1)
        val countEnter = preferences.countEnter
        if (countEnter == 21 && !preferences.playMarketVisited) {
            dialog(R.layout.dialog_rate_us) {
                findViewById<TextView>(R.id.cancelTextView).setOnClickListener {
                    preferences.playMarketVisited = false
                    preferences.countEnter = 0
                    dismiss()
                }
                findViewById<TextView>(R.id.applyTextView).setOnClickListener {
                    preferences.playMarketVisited = true
                    navigator.showWebBrowser(getAppGooglePlayUrl())
                    dismiss()
                }
            }.show()
        }
        if (countEnter == 51 && preferences.playMarketVisited) {
            dialog(R.layout.dialog_rate_us) {
                findViewById<TextView>(R.id.cancelTextView).setOnClickListener { dismiss() }
                findViewById<TextView>(R.id.applyTextView).setOnClickListener {
                    navigator.showWebBrowser(getAppGooglePlayUrl())
                    dismiss()
                }
            }.show()
        }
    }

    private fun getAppGooglePlayUrl(): String {
        return "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
    }


}