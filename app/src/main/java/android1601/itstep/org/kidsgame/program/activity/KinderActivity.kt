package android1601.itstep.org.kidsgame.program.activity

import android.os.Bundle

import android1601.itstep.org.kidsgame.R

class KinderActivity : BaseActivity() {

    override val layoutResId: Int
        get() = R.layout.activity_with_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val carsForPuzzle = intent.getBooleanExtra(MainActivity.CARS_FOR_PUZZLE, true)
        showScratchEggFragment(carsForPuzzle)
    }

}
