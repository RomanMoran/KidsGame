package android1601.itstep.org.kidsgame.program;

import android.app.Application;
import android.view.View;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by roman on 14.03.2017.
 */

public class KidsApplication extends Application {
    private static final String TAG = KidsApplication.class.getName();
    private static KidsApplication instance;

    public static KidsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());

    }
}
