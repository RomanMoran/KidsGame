package android1601.itstep.org.kidsgame.program.ui.base

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.ProgressBar
import android1601.itstep.org.kidsgame.program.di.PreferencesProvider
import android1601.itstep.org.kidsgame.program.ui.navigation.ActivityViewNavigator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.franmontiel.localechanger.LocaleChanger
import com.franmontiel.localechanger.utils.ActivityRecreationHelper


abstract class BaseActivity : AppCompatActivity() {


    abstract val layoutRes: Int

    protected lateinit var progressBar: ProgressBar
    val navigator by lazy { ActivityViewNavigator(this) }
    private var menu: Menu? = null
    val preferences by PreferencesProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(layoutRes)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleChanger.configureBaseContext(newBase))
    }

    override fun onResume() {
        super.onResume()
        ActivityRecreationHelper.onResume(this)
    }

    override fun onDestroy() {
        ActivityRecreationHelper.onDestroy(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

}