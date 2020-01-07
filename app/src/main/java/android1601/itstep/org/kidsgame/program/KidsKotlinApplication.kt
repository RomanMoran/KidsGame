package android1601.itstep.org.kidsgame.program

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android1601.itstep.org.kidsgame.program.data.LocalStorage
import android1601.itstep.org.kidsgame.program.di.ContextProvider
import android1601.itstep.org.kidsgame.program.di.PreferencesProvider
import android1601.itstep.org.kidsgame.program.ext.lang.SUPPORTED_LOCALES
import com.franmontiel.localechanger.LocaleChanger
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

class KidsKotlinApplication : Application() {

    companion object {
        fun getInstance(): Context {
            return ContextProvider().value
        }

    }

    private var instance: KidsKotlinApplication? = null


    init {
        PreferencesProvider.inject { LocalStorage(applicationContext) }
        ContextProvider.inject { applicationContext }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FlowManager.init(FlowConfig.Builder(this).build())
        LocaleChanger.initialize(this, SUPPORTED_LOCALES)


    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LocaleChanger.onConfigurationChanged()
    }

}