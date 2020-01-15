package android1601.itstep.org.kidsgame.program.fragments.puzzle

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.ext.fromJson
import android1601.itstep.org.kidsgame.program.fragments.KEY_CARS_FOR_PUZZLE_ENTRY
import android1601.itstep.org.kidsgame.program.fragments.KEY_GIFT_ENTRY
import android1601.itstep.org.kidsgame.program.ui.base.BaseMvpFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson

class PuzzleKotlinFragment : BaseMvpFragment<PuzzlePresenter, PuzzleView>(), PuzzleView {

    override val presenter by lazy { PuzzlePresenter(giftsEntry) }

    override val mvpView = this

    override val layoutRes get() = presenter.getCompatibleLayout()

    private val giftsEntry
        get() = Gson().fromJson<List<Gifts>>(arguments?.getString(KEY_GIFT_ENTRY) ?: "")

    private val carsForPuzzle get() = arguments?.getBoolean(KEY_CARS_FOR_PUZZLE_ENTRY) ?: true


    private var imgLeftCarsList = arrayOf(R.id.imgCar1, R.id.imgCar2, R.id.imgCar3, R.id.imgCar4)
    private var imgEmptyCarsList = arrayOf(R.id.imgEmpty1, R.id.imgEmpty2, R.id.imgEmpty3, R.id.imgEmpty4)

    private var imgEmptyLeftCarsList = arrayOf(R.id.leftEmptyImage1, R.id.leftEmptyImage2, R.id.leftEmptyImage3, R.id.leftEmptyImage4)

    private var displayWidth = 0
    private var displayHeight = 0
    private var imageWidth = 0
    private var imageHeight = 0

    private var draggableImage: ImageView? = null
    private var targetEmptyImage: ImageView? = null
    private var lastDragImageParams: ViewGroup.LayoutParams? = null
    private var touchFlag = false

    private var offsetX = 0
    private var offsetY = 0

    private var countImages = 0

    companion object {
        fun newInstance(gifts: List<Gifts>): PuzzleKotlinFragment {
            return PuzzleKotlinFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_GIFT_ENTRY, Gson().toJson(gifts))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displaySize = Utility.getDisplaySize()
        displayWidth = displaySize.x
        displayHeight = displaySize.y
        // Определение размера картинки(ImageView) на экране
        imageWidth = (displayWidth / 3.0f).toInt()       // Т.е. занимает 1/3 экрана по ширине
        imageHeight = (displayHeight / 4.0f).toInt()     // и 1/4 по высоте
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImages()
        imgLeftCarsList.forEach { resourseId ->
            val imageView = view.findViewById<ImageView>(resourseId)
            imageView.setOnTouchListener { v, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> whatImage(imageView, event)
                }
                false
            }
        }
        view.findViewById<RelativeLayout>(R.id.puzzleFragment).setOnTouchListener { v, event ->
            if (touchFlag) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
                        //расчет отступов перемещаемого изображения согласно координат прикосновения на перемещаемое изображение
                        var x = (event.x - offsetX).toInt()
                        var y = (event.y - offsetY).toInt()
                        // Границы чтобы изображение не выходило за пределы экрана
                        if (x > displayWidth - imageWidth)
                            x = displayWidth - imageWidth
                        else if (x < 0) x = 0
                        if (y > displayHeight - imageHeight)
                            y = displayHeight - imageHeight
                        else if (y < 0) y = 0
                        var lp = RelativeLayout.LayoutParams(lastDragImageParams!!.width, lastDragImageParams!!.height)
                        lp.setMargins(x, y, 0, 0)
                        draggableImage!!.layoutParams = lp
                    }
                    MotionEvent.ACTION_UP -> {
                        touchFlag = false
                        //определяем координаті центра перемещаемого изображения
                        val lp = draggableImage?.getLayoutParams() as RelativeLayout.LayoutParams
                        val centerX = lp.leftMargin + imageWidth / 2
                        val centerY = lp.topMargin + imageHeight / 2
                        // сравниваем попал ли центр перемещаемого изображения в "целевой прямоуголник"
                        // отнимаем 1/3 ширины и высоты от краев тем самым уменьшая область прямоугольника
                        // тем самым образуя "целевой прямоугольник"

                        if (targetEmptyImage != null &&
                                centerX > targetEmptyImage!!.left + imageWidth / 3 &&
                                centerX < targetEmptyImage!!.right - imageWidth / 3 &&
                                centerY > targetEmptyImage!!.top + imageHeight / 3 &&
                                centerY < targetEmptyImage!!.bottom - imageHeight / 3) {
                            targetEmptyImage!!.setImageDrawable(draggableImage!!.drawable)
                            draggableImage!!.visibility = View.INVISIBLE

                            countImages++
                            presenter.countImages++


                            presenter.play()
                        } else {
                            //если не попали возвращаем в исходное положение
                            draggableImage!!.layoutParams = lastDragImageParams
                        }
                        draggableImage = null
                        targetEmptyImage = null
                    }
                    else -> {
                    }
                }
            }
            true
        }
    }

    private fun whatImage(targetView: View, event: MotionEvent) {
        for (i in imgLeftCarsList.indices) {
            val imageView = view?.findViewById<ImageView>(imgLeftCarsList[i])
            if (imageView != null){
                if (imageView == targetView) {
                    draggableImage = view?.findViewById(imgLeftCarsList[i])
                    targetEmptyImage = view?.findViewById(imgEmptyCarsList[i])

                    presenter.startLoadResIdSound()
                    // перемещаемое изображение на передний план
                    draggableImage!!.bringToFront()
                    // исходные параметры расположения перемещаемого изображения
                    // в случае если оно не было перемещно в необходимые координаты изображению задаем эти параметры
                    // тем самым возвращая его в исходное положение
                    lastDragImageParams = draggableImage!!.layoutParams
                    touchFlag = true
                    // offsets - это координаты прикосновения (ширины(Х) и высоты(Y)) в плоскости нашего изображения
                    // запоминаем для дальнейшего расчета left and top margins перемещаемого изображения
                    offsetX = event.x.toInt()
                    offsetY = event.y.toInt()
                    break
                }
            }
        }

    }


    private fun initImages() {
        // Инициализируем передвигаемые объекты (машинки)
        for (i in imgLeftCarsList.indices) {
            val imageView = view?.findViewById<ImageView>(imgLeftCarsList[i])
            if (imageView != null) {

                if (giftsEntry.size > i) {
                    // Получаем значение int нашего изображение
                    val resourceId = giftsEntry[i].imageResId

                    // Динамическая загрузка необходимых
                    // изображений в пустые imgCar's

                    Glide.with(context!!)
                            .load(resourceId)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .skipMemoryCache(true)
                            //.animate(R.anim.combo)
                            .into(imageView)
                }
                updateSize(imageView, imageWidth, imageHeight)
            }

        }

        for (i in imgEmptyCarsList.indices) {
            val imageView = view?.findViewById<ImageView>(imgEmptyCarsList[i])
            if (imageView != null) {
                if (giftsEntry.size > i) {
                    // Получаем значение int нашего изображения
                    val resourceId = giftsEntry[i].silhouetteResId

                    // Динамическая загрузка необходимых
                    // изображений в пустые imgCar's
                    Glide.with(imageView.getContext())
                            .load(resourceId)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .skipMemoryCache(true)
                            //.animate(R.anim.change)
                            .into(imageView)
                }
                updateSize(imageView, imageWidth, imageHeight)
            }
        }

        for (i in imgEmptyLeftCarsList.indices) {
            val imageView = view?.findViewById<View>(imgEmptyLeftCarsList[i])
            if (imageView != null) {
                updateSize(imageView, imageWidth, imageHeight)
            }
        }
    }


    private fun updateSize(view: View, width: Int, height: Int) {
        view.layoutParams.height = height
        view.layoutParams.width = width
        view.requestLayout()
    }

    override fun showWinTextAndSound() {
        val tv = TextView(context)
        tv.text = resources.getString(R.string.success)
        tv.textSize = (displayWidth / 23).toFloat()
        tv.setTextColor(resources.getColor(R.color.dorge_blue))
        val tvParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        tvParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        tv.gravity = Gravity.CENTER
        view?.findViewById<RelativeLayout>(R.id.puzzleFragment)?.addView(tv, tvParams)
        val anim = AnimationUtils.loadAnimation(context, R.anim.change_text)

        tv.typeface = Utility.getTypeface()
        tv.startAnimation(anim)
    }


}