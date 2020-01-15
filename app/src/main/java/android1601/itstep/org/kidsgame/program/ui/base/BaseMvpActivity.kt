package android1601.itstep.org.kidsgame.program.ui.base

import android.os.Bundle
import android.view.View
import android1601.itstep.org.kidsgame.program.ext.ui.toast

abstract class BaseMvpActivity<P : BasePresenter<V>, V : BaseView> : BaseActivity(),
        BaseView {

    protected abstract val presenter: P
    protected abstract val mvpView: V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setNavigator(navigator)
        presenter.attachView(mvpView)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun closeKeyboard() {

    }

    override fun showMessage(text: String?) = toast(text)
    override fun showMessage(textId: Int) = toast(textId)

    override fun showProgressIndicator() = run { progressBar.visibility = View.VISIBLE }
    override fun hideProgressIndicator() = run { progressBar.visibility = View.GONE }

}
