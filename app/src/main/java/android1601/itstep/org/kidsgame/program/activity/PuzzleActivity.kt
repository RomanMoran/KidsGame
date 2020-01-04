package android1601.itstep.org.kidsgame.program.activity

import android.os.Bundle

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper

class PuzzleActivity : BaseActivity() {

    override val layoutResId: Int
        get() = R.layout.activity_with_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val giftsSection = DBHelper.getRandomFourItems()
        showPuzzleFragment(giftsSection)
    }

    companion object {

        val TAG = PuzzleActivity::class.java.name
    }
}
