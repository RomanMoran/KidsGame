package android1601.itstep.org.kidsgame.program.activity.main

import android.os.Bundle
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_kotlin.*

class MainKotlinActivity : BaseActivity() {

    override val layoutRes: Int = R.layout.activity_main_kotlin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openEggs.setOnClickListener { }
        openPuzzle.setOnClickListener { }
        openCollection.setOnClickListener { }
        aboutUs.setOnClickListener { }
        rgLanguages.setOnCheckedChangeListener { group, checkedId ->
            if (rbEng.id == checkedId) {

            } else {

            }
        }

    }


}