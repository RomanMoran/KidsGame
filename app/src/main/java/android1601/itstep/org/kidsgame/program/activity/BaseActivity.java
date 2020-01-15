package android1601.itstep.org.kidsgame.program.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Random;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.LocaleHelper;
import android1601.itstep.org.kidsgame.program.ui.navigation.ViewNavigatorImlpKt;
import butterknife.ButterKnife;

/**
 * Created by roman on 13.03.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private FragmentManager mCurrentFragmentManager;


    // Вызывается если необходимо стартануть fragment с параметром
    // backStack - false
    public static void newInstance(Context context, final Class<? extends AppCompatActivity> activityClass) {
        newInstance(context, activityClass, false, true);
    }

    public static void newInstance(Context context, final Class<? extends AppCompatActivity> activityClass, boolean clearBackStack, boolean carsForPuzzle) {
        if (activityClass == context.getClass()) {
            return;
        }
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ViewNavigatorImlpKt.CARS_FOR_PUZZLE, carsForPuzzle);
        if (clearBackStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityCompat.startActivity(context, intent, null);
    }

    public abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setLocaleFromPrefs(this);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void showFragment(int container, Fragment fragment, String tag, boolean addToBackStack) {
        FragmentManager fragmentManager = getCurrentFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(container, fragment, tag);
        if (addToBackStack) ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    public FragmentManager getCurrentFragmentManager() {
        if (mCurrentFragmentManager == null)
            mCurrentFragmentManager = getSupportFragmentManager();
        return mCurrentFragmentManager;
    }


}
