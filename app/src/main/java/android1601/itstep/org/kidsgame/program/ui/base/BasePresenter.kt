package android1601.itstep.org.kidsgame.program.ui.base

import android1601.itstep.org.kidsgame.program.ui.navigation.ViewNavigator

interface BasePresenter<V : BaseView> {
    val view: V?
    fun attachView(view: V)
    fun detachView()
    fun onPause()
    fun setNavigator(navigator: ViewNavigator)
    fun getNavigator(): ViewNavigator
}