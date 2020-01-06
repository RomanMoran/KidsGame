package android1601.itstep.org.kidsgame.program.ui.base

import android.os.Bundle
import android.view.Menu
import android.widget.ProgressBar
import android1601.itstep.org.kidsgame.program.di.PreferencesProvider
import android1601.itstep.org.kidsgame.program.ui.navigation.ActivityViewNavigator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


abstract class BaseActivity : AppCompatActivity() {


    abstract val layoutRes: Int

    protected lateinit var progressBar: ProgressBar
    val navigator by lazy { ActivityViewNavigator(this) }
    private var menu: Menu? = null
    private val preferences by PreferencesProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(layoutRes)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

}