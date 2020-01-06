package android1601.itstep.org.kidsgame.program.ui.base

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.di.ObjectMapperProvider
import android1601.itstep.org.kidsgame.program.di.PreferencesProvider
import android1601.itstep.org.kidsgame.program.ext.net.isOffline
import android1601.itstep.org.kidsgame.program.ext.tryOrNull
import android1601.itstep.org.kidsgame.program.ui.navigation.ViewNavigator
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BasePresenterImpl<V : BaseView> : BasePresenter<V> {


    protected val localStorage by PreferencesProvider()
    protected val mapper by ObjectMapperProvider()

    private lateinit var navigator: ViewNavigator


    override fun setNavigator(navigator: ViewNavigator) {
        this.navigator = navigator
    }

    override fun getNavigator(): ViewNavigator = navigator

    protected fun onError(throwable: Throwable) {
        throwable.printStackTrace()
        view?.hideProgressIndicator()
        view?.onError(throwable)
        onError()
    }

    protected open fun onError() = Unit

    protected fun setProgressIndicator(active: Boolean) {
        if (active) view?.showProgressIndicator()
        else view?.hideProgressIndicator()
    }

    protected fun V.onError(throwable: Throwable) = when {
        throwable is SocketTimeoutException -> showMessage(R.string.timeout_error)
        throwable is HttpException -> showMessage(throwable.readMessage())
        isOffline() -> showMessage(R.string.network_error)
        else -> showMessage(throwable.message)
    }

    private fun HttpException.readMessage(): String? {
        return response()?.errorBody()?.readMessage() ?: message()
    }

    private fun ResponseBody.readMessage() = tryOrNull {
        string()
    }

}