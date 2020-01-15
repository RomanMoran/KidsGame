package android1601.itstep.org.kidsgame.program.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android1601.itstep.org.kidsgame.program.ext.ui.toast


abstract class BaseMvpFragment<P : BasePresenter<V>, V : BaseView> : BaseFragment(),
        BaseView {

    protected abstract val presenter: P
    protected abstract val mvpView: V

    override fun closeKeyboard() {

    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        return super.onCreateView(inflater, parent, state)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(mvpView)
        presenter.setNavigator(navigator)
    }


    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }


    override fun runOnUiThread(action: Runnable) {
        activity?.runOnUiThread(action)
    }


    override fun showMessage(text: String?) = toast(text)
    override fun showMessage(textId: Int) = toast(textId)

    override fun showProgressIndicator() = run { /* progressBar.visibility = View.VISIBLE*/ }
    override fun hideProgressIndicator() = run { /* progressBar.visibility = View.GONE*/ }

}
