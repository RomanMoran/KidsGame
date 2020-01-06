package android1601.itstep.org.kidsgame.program.ui.base

import android1601.itstep.org.kidsgame.program.ui.navigation.ViewNavigator

interface BasePresenter<V : BaseView> {
    val view: V?
    fun setNavigator(navigator: ViewNavigator)
    fun getNavigator(): ViewNavigator
    //fun attachView(view: V){}
    //fun detachView(){}
}