package android1601.itstep.org.kidsgame.program.ui.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android1601.itstep.org.kidsgame.program.ui.navigation.FragmentViewNavigator
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

    abstract val layoutRes: Int

    val navigator by lazy { FragmentViewNavigator(this) }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        return inflater.createContentView()
    }

    private fun LayoutInflater.createContentView() = FrameLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        if (layoutRes != 0) inflate(layoutRes, this)
    }

}