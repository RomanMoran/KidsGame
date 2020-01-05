package android1601.itstep.org.kidsgame.program.ui.navigation

import android.app.Activity
import android.content.Intent

interface ViewNavigator {
    fun clearBackStack()
    fun startActivity(intent: Intent)
    fun startActivity(activityClass: Class<out Activity>, clearBackStack: Boolean = false)

}