package android1601.itstep.org.kidsgame.program.activity.kinder

import android.os.Bundle
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.activity.MainActivity
import android1601.itstep.org.kidsgame.program.ui.base.BaseActivity

class KinderKotlinActivity : BaseActivity(){

    override val layoutRes: Int = R.layout.activity_with_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val carsForPuzzle = intent.getBooleanExtra(MainActivity.CARS_FOR_PUZZLE, true)
        navigator.showScratchEggFragment(carsForPuzzle)
    }

}