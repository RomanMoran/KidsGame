package android1601.itstep.org.kidsgame.program.ui.navigation

import android.app.Activity
import android.content.Intent
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.activity.CollectionActivity
import android1601.itstep.org.kidsgame.program.activity.KinderActivity
import android1601.itstep.org.kidsgame.program.activity.MainActivity
import android1601.itstep.org.kidsgame.program.activity.PuzzleActivity
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import android1601.itstep.org.kidsgame.program.ext.tryTo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import java.lang.ref.WeakReference

abstract class AbstractViewNavigator : ViewNavigator {

    protected abstract val fragmentActivity: FragmentActivity?
    open val fragment: Fragment? = null

    override fun clearBackStack() {
        val fragmentManager = fragmentActivity?.supportFragmentManager ?: return
        for (i in 0 until fragmentManager.backStackEntryCount) fragmentManager.popBackStack()
    }

    override fun startActivity(activityClass: Class<out Activity>, clearBackStack: Boolean) {
        fragmentActivity.let { activity ->
            startActivity(Intent(activity, activityClass))
            if (clearBackStack) activity?.finishAffinity()
        }
    }

    override fun startActivity(intent: Intent) {
        fragmentActivity.let { activity ->
            activity?.startActivity(intent)
        }
    }

    override fun showKinderView() {
        startActivity(KinderActivity::class.java)
    }

    override fun showOpenPuzzlesView() {
        if (DBHelper.getUnlockedBySection().size >= 4) {
            startActivity(PuzzleActivity::class.java)
        } else {
            startActivity(Intent(fragmentActivity, KinderActivity::class.java).apply {
                putExtra(MainActivity.CARS_FOR_PUZZLE, false)
            })
        }
    }

    override fun showCollectionView() {
        startActivity(Intent(fragmentActivity, CollectionActivity::class.java).apply {
            putExtra(MainActivity.CARS_FOR_PUZZLE, true)
        })
    }

    private fun Fragment.add(toBackStack: Boolean = true) = addOrReplace(toBackStack, true)
    private fun Fragment.replace(toBackStack: Boolean = false) = addOrReplace(toBackStack, false)

    private fun Fragment.addOrReplace(toBackStack: Boolean, add: Boolean) = tryTo {
        val fragmentManager = fragmentActivity?.supportFragmentManager ?: return
        val transaction = fragmentManager.beginTransaction()
        transaction.toBackStack(toBackStack).addOrReplace(this, add).commit()
    }

    private fun FragmentTransaction.toBackStack(toBackStack: Boolean) = if (toBackStack) {
        addToBackStack(null)
        setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right)
    } else {
        setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
    }

    private fun FragmentTransaction.addOrReplace(fragment: Fragment, add: Boolean) =
            if (add) add(R.id.container, fragment) else replace(R.id.container, fragment)


}

class ActivityViewNavigator(fragmentActivity: FragmentActivity) : AbstractViewNavigator() {
    override val fragmentActivity get() = reference.get()
    private val reference = WeakReference(fragmentActivity)
}

class FragmentViewNavigator(override val fragment: Fragment?) : AbstractViewNavigator() {
    override val fragmentActivity get() = reference.get()?.activity
    private val reference = WeakReference(fragment)
}