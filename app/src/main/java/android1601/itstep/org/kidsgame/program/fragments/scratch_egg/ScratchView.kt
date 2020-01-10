package android1601.itstep.org.kidsgame.program.fragments.scratch_egg

import android1601.itstep.org.kidsgame.program.ui.base.BaseView

interface ScratchView : BaseView  {
    fun onInitImageAndText(toyImageId: Int, toyTextId: Int)
    fun revealImage()
    fun startTransformations()
}