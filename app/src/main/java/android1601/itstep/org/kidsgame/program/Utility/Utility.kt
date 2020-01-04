package android1601.itstep.org.kidsgame.program.Utility

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.os.Build
import android.view.Display
import android.view.WindowManager

import java.util.Random

import android1601.itstep.org.kidsgame.program.KidsApplication

/**
 * Created by roman on 14.03.2017.
 */

object Utility {


    //DebugUtility.logTest(TAG, "DisplaySize x = " + point.x + " y = " + point.y);
    val displaySize: Point?
        get() {
            val context = KidsApplication.instance
            var point: Point? = null
            if (Build.VERSION.SDK_INT >= 13) {
                point = getDisplaySizeAFTER13(context!!)
            } else {
                point = getDisplaySizeBEFORE13(context!!)
            }
            return point
        }

    val typeface: Typeface
        get() {
            val context = KidsApplication.instance
            val custom_font_en = "font/comic.ttf"
            val custom_font_ru = "font/sumkin_typeface.otf"
            return Typeface.createFromAsset(context!!.assets, if (LocaleHelper.language === "en") custom_font_en else custom_font_ru)
        }

    fun getDrawableResourceIdByName(resName: String?): Int {
        return getIdResourceByNameAndType(resName, "drawable")
    }

    fun getIdResourceByNameAndType(resName: String?, resType: String): Int {
        try {
            val context = KidsApplication.instance
            val packageName = context!!.packageName
            return context.resources.getIdentifier(resName, resType, packageName)// getString(resId);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    fun getStringResourseById(id: Int): String {
        val context = KidsApplication.instance
        return context!!.resources.getResourceName(id)
    }

    fun getRawIdByName(resName: String): Int {
        return getIdResourceByNameAndType(resName, "raw")
    }

    fun getStringResourceIdByName(resName: String): Int {
        return getIdResourceByNameAndType(resName, "string")
    }

    private fun getDisplaySizeBEFORE13(context: Context): Point {
        val window = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = window.defaultDisplay
        return Point(display.width, display.height)

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun getDisplaySizeAFTER13(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    fun getRndFromDb(limit: Int): Int {
        return Random().nextInt(limit)
    }

}
