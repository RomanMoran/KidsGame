package android1601.itstep.org.kidsgame.program.ui.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.activity.CollectionActivity
import android1601.itstep.org.kidsgame.program.activity.kinder.KinderKotlinActivity
import android1601.itstep.org.kidsgame.program.activity.puzzle.PuzzleKotlinActivity
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import android1601.itstep.org.kidsgame.program.ext.tryTo
import android1601.itstep.org.kidsgame.program.fragments.puzzle.PuzzleKotlinFragment
import android1601.itstep.org.kidsgame.program.fragments.scratch_egg.ScratchEggKotlinFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import java.lang.ref.WeakReference
import java.util.*

const val CARS_FOR_PUZZLE = "CARS_FOR_PUZZLE"

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

    private fun Activity.createActivityIntent(screenKey: String?, data: Any?): Intent? =
            when (screenKey) {
                Screens.SEND_EMAIL_SCREEN -> {
                    val emailIntent = Intent(
                            Intent.ACTION_SENDTO,
                            Uri.fromParts("mailto", data as String, null)
                    )

                    Intent.createChooser(emailIntent, getString(R.string.send_email))
                }
                Screens.WEB_BROWSER_SCREEN -> {
                    Intent(Intent.ACTION_VIEW).apply {
                        (data as String).let {
                            if (it.startsWith("http://") || it.startsWith("https://")) {
                                setData(Uri.parse(it))
                            } else {
                                setData(Uri.parse("http://$it"))
                            }
                        }
                    }
                }
                else -> null
            }

    override fun showWebBrowser(url: String) {
        fragmentActivity?.createActivityIntent(
                Screens.WEB_BROWSER_SCREEN,
                url
        )?.let { startActivity(it) }
    }

    override fun showKinderView() {
        startActivity(KinderKotlinActivity::class.java)
    }


    override fun showScratchEggFragment(carsForPuzzle: Boolean) {
        val giftsList = if (carsForPuzzle) DBHelper.getall() else DBHelper.getLocked()
        val gifts = giftsList[Random().nextInt(giftsList.size)]
        showScratchEggKotlinFragment(gifts, carsForPuzzle)
    }

    override fun showPuzzleFragment() {
        val giftsSection = DBHelper.getRandomFourItems()
        PuzzleKotlinFragment.newInstance(giftsSection).replace()
    }

    fun showScratchEggKotlinFragment(gifts: Gifts, carsForPuzzle: Boolean) {
        ScratchEggKotlinFragment.newInstance(gifts, carsForPuzzle).replace()
    }

    override fun showOpenPuzzlesView(clearBackStack: Boolean) {
        if (DBHelper.getUnlockedBySection().size >= 4) {
            startActivity(PuzzleKotlinActivity::class.java)
        } else {
            startActivity(Intent(fragmentActivity, KinderKotlinActivity::class.java).apply {
                putExtra(CARS_FOR_PUZZLE, false)
            })
        }
        if (clearBackStack) fragmentActivity?.finishAffinity()
    }

    override fun showOpenPuzzlesKotlinView(clearBackStack: Boolean) {
        if (DBHelper.getUnlockedBySection().size >= 4) {
            startActivity(PuzzleKotlinActivity::class.java)
        } else {
            startActivity(Intent(fragmentActivity, KinderKotlinActivity::class.java).apply {
                putExtra(CARS_FOR_PUZZLE, false)
            })
        }
        if (clearBackStack) fragmentActivity?.finishAffinity()
    }

    override fun showCollectionView() {
        startActivity(Intent(fragmentActivity, CollectionActivity::class.java).apply {
            putExtra(CARS_FOR_PUZZLE, true)
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