package android1601.itstep.org.kidsgame.program

import android.app.Application
import android.view.View

import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

/**
 * Created by roman on 14.03.2017.
 */

class KidsApplication : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()
        FlowManager.init(FlowConfig.Builder(this).build())

    }

    companion object {
        private val TAG = KidsApplication::class.java.name
        var instance: KidsApplication? = null
            private set
    }
}
