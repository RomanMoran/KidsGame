package android1601.itstep.org.kidsgame.program.activity.main

import android.os.Bundle
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.ext.lang.EN_LOCALE
import android1601.itstep.org.kidsgame.program.ext.lang.RU_LOCALE
import android1601.itstep.org.kidsgame.program.ui.base.BaseActivity
import com.franmontiel.localechanger.LocaleChanger
import com.franmontiel.localechanger.utils.ActivityRecreationHelper
import kotlinx.android.synthetic.main.activity_main_kotlin.*

class MainKotlinActivity : BaseActivity() {

    override val layoutRes: Int = R.layout.activity_main_kotlin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openEggs.setOnClickListener { navigator.showKinderView() }
        openPuzzle.setOnClickListener { navigator.showOpenPuzzlesView() }
        openCollection.setOnClickListener { navigator.showCollectionView() }
        aboutUs.setOnClickListener { }
        rgLanguages.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbEng -> LocaleChanger.setLocale(EN_LOCALE)
                else -> LocaleChanger.setLocale(RU_LOCALE)
            }
            ActivityRecreationHelper.recreate(this, true)
        }

    }


}