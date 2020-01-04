package android1601.itstep.org.kidsgame.program.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import java.util.Random

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.LocaleHelper
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import android1601.itstep.org.kidsgame.program.fragments.PuzzleFragment
import android1601.itstep.org.kidsgame.program.fragments.ScratchEggFragment
import android1601.itstep.org.kidsgame.program.fragments.ToysCollectionFragment
import butterknife.ButterKnife

/**
 * Created by roman on 13.03.2017.
 */

abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutResId: Int

    val currentFragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.setLocaleFromPrefs(this)
        setContentView(layoutResId)
        ButterKnife.bind(this)
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

    }

    fun showScratchEggFragment(carsForPuzzle: Boolean) {
        val giftsList = if (carsForPuzzle) DBHelper.getall() else DBHelper.getLocked()
        val gifts = giftsList[Random().nextInt(giftsList.size)]
        showScratchEggFragment(gifts, carsForPuzzle)
    }

    fun showScratchEggFragment(gifts: Gifts, carsForPuzzle: Boolean) {
        val tag = ScratchEggFragment.TAG
        val fragment = ScratchEggFragment.newInstance(gifts, carsForPuzzle)
        showFragment(R.id.fragmentConatiner, fragment, tag, false)
    }

    fun showToysCollectionFragment(section: Int/*,ArrayList<Gifts> giftsArrayList*/) {
        val tag = ToysCollectionFragment.TAG
        val fragment = ToysCollectionFragment.newInstance(section/*,giftsArrayList*/)
        showFragment(R.id.fragmentConatiner, fragment, tag, false)
    }

    fun showPuzzleFragment(toyList: List<Gifts>) {
        val tag = PuzzleActivity.TAG
        val fragment = PuzzleFragment.newInstance(toyList)
        showFragment(R.id.fragmentConatiner, fragment, tag, false)

    }


    fun showFragment(container: Int, fragment: Fragment, tag: String, addToBackStack: Boolean) {
        val fragmentManager = currentFragmentManager
        val ft = fragmentManager.beginTransaction()
        ft.replace(container, fragment, tag)
        if (addToBackStack) ft.addToBackStack(tag)
        ft.commitAllowingStateLoss()
    }

    companion object {

        @JvmOverloads
        fun newInstance(context: Context, activityClass: Class<out AppCompatActivity>, clearBackStack: Boolean = false, carsForPuzzle: Boolean = true) {
            if (activityClass == context.javaClass) {
                return
            }
            val intent = Intent(context, activityClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(MainActivity.CARS_FOR_PUZZLE, carsForPuzzle)
            if (clearBackStack)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            ActivityCompat.startActivity(context, intent, null)
        }
    }


}// Вызывается если необходимо стартануть fragment с параметром
// backStack - false
