package android1601.itstep.org.kidsgame.program.ui.base

interface BaseView {

    fun runOnUiThread(action: Runnable)

    fun closeKeyboard()

    fun showMessage(text: String?)
    fun showMessage(textId: Int)

    fun showProgressIndicator()
    fun hideProgressIndicator()
}

