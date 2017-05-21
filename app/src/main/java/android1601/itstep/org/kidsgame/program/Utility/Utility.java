package android1601.itstep.org.kidsgame.program.Utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;

import java.util.Random;

import android1601.itstep.org.kidsgame.program.KidsApplication;

/**
 * Created by roman on 14.03.2017.
 */

public class Utility {

    public static int getDrawableResourceIdByName(@NonNull String resName) {
        return getIdResourceByNameAndType(resName, "drawable");
    }

    public static int getIdResourceByNameAndType(@NonNull String resName, String resType) {
        try {
            Context context= KidsApplication.getInstance();
            String packageName = context.getPackageName();
            int resId = context.getResources().getIdentifier(resName, resType, packageName);
            return resId;// getString(resId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getStringResourseById(int id){
        Context context = KidsApplication.getInstance();
        String name = context.getResources().getResourceName(id);
        return name;
    }

    public static int getRawIdByName(String resName){
        return getIdResourceByNameAndType(resName,"raw");
    }


    public static Point getDisplaySize() {
        Context context= KidsApplication.getInstance();
        Point point = null;
        if (Build.VERSION.SDK_INT >= 13) {
            point = getDisplaySizeAFTER13(context);
        } else {
            point = getDisplaySizeBEFORE13(context);
        }
        //DebugUtility.logTest(TAG, "DisplaySize x = " + point.x + " y = " + point.y);
        return point;
    }
    public static int getStringResourceIdByName(@NonNull String resName) {
        return getIdResourceByNameAndType(resName, "string");
    }

    @SuppressWarnings("deprecation")
    private static Point getDisplaySizeBEFORE13(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        return new Point(display.getWidth(), display.getHeight());

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static Point getDisplaySizeAFTER13(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getRndFromDb(int limit){
        return new Random().nextInt(limit);
    }

    public static Typeface getTypeface(){
        Context context= KidsApplication.getInstance();
        String custom_font_en = "font/comic.ttf";
        String custom_font_ru = "font/sumkin_typeface.otf";
        Typeface CF = Typeface.createFromAsset(context.getAssets(), LocaleHelper.getLanguage()=="en"?custom_font_en:custom_font_ru);
        return CF;
    }

}
