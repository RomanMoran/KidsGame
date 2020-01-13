package android1601.itstep.org.kidsgame.program.ui.navigation

import android.app.Activity
import android.content.Intent

interface ViewNavigator {
    fun clearBackStack()
    fun startActivity(intent: Intent)
    fun startActivity(activityClass: Class<out Activity>, clearBackStack: Boolean = false)
    fun showKinderView()
    fun showOpenPuzzlesView(clearBackStack: Boolean = false)
    fun showCollectionView()
    fun showScratchEggFragment(carsForPuzzle: Boolean)
    fun showPuzzleFragment()
    fun showOpenPuzzlesKotlinView(clearBackStack: Boolean = false)

}