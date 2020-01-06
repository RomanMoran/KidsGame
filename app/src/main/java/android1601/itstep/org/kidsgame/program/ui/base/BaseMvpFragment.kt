package android1601.itstep.org.kidsgame.program.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class BaseMvpFragment<P : BasePresenter<V>, V : BaseView> : BaseFragment(),
        BaseView {

    protected abstract val presenter: P
    protected abstract val mvpView: V

    override fun closeKeyboard() {

    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        presenter.setNavigator(navigator)
        return super.onCreateView(inflater, parent, state)
    }


    override fun runOnUiThread(action: Runnable) {
        activity?.runOnUiThread(action)
    }

}
