package android1601.itstep.org.kidsgame.program.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import android1601.itstep.org.kidsgame.program.Utility.LocaleHelper
import android1601.itstep.org.kidsgame.program.ui.navigation.ActivityViewNavigator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class BaseKotlinActivity : AppCompatActivity() {

    abstract val layoutRes: Int

    val navigator by lazy { ActivityViewNavigator(this) }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        LocaleHelper.setLocaleFromPrefs(this)
        setContentView(layoutRes)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


}