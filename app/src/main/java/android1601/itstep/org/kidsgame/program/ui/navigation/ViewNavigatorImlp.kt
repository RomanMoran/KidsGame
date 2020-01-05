package android1601.itstep.org.kidsgame.program.ui.navigation

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
}

class ActivityViewNavigator(fragmentActivity: FragmentActivity) : AbstractViewNavigator() {
    override val fragmentActivity get() = reference.get()
    private val reference = WeakReference(fragmentActivity)
}

class FragmentViewNavigator(override val fragment: Fragment?) : AbstractViewNavigator() {
    override val fragmentActivity get() = reference.get()?.activity
    private val reference = WeakReference(fragment)
}