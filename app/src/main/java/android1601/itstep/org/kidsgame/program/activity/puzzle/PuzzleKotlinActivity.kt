package android1601.itstep.org.kidsgame.program.activity.puzzle

import android.os.Bundle
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.ui.base.BaseActivity

class PuzzleKotlinActivity : BaseActivity() {

    override val layoutRes: Int = R.layout.activity_with_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.showPuzzleFragment()
    }

}