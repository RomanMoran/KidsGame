package android1601.itstep.org.kidsgame.program.fragments

import android.graphics.Point
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

import java.util.ArrayList

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.activity.PuzzleActivity
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import android1601.itstep.org.kidsgame.program.enums.POSITION_TYPE
import butterknife.BindView
import butterknife.BindViews
import butterknife.OnTouch

/**
 * Created by roman on 14.03.2017.
 */

class PuzzleFragment : BaseFragment() {
    @BindViews(R.id.imgCar1, R.id.imgCar2, R.id.imgCar3, R.id.imgCar4)
    internal var imgLeftCarsList: List<ImageView>? = null
    @BindViews(R.id.leftEmptyImage1, R.id.leftEmptyImage2, R.id.leftEmptyImage3, R.id.leftEmptyImage4)
    internal var imgEmptyLeftCarsList: List<View>? = null
    @BindViews(R.id.imgEmpty1, R.id.imgEmpty2, R.id.imgEmpty3, R.id.imgEmpty4)
    internal var imgEmptyCarsList: List<ImageView>? = null


    private var touchFlag = false
    private var lastDragImageParams: ViewGroup.LayoutParams? = null

    private var displayWidth = 0
    private var displayHeight = 0
    private var imageWidth = 0
    private var imageHeight = 0

    private var offsetX = 0
    private var offsetY = 0
    private var targetEmptyImage: ImageView? = null
    private var draggableImage: ImageView? = null
    internal var mGiftsList: ArrayList<Gifts>? = null

    private var countAir = 0
    private var countGround = 0
    private var countWater = 0

    private var mMediaPlayerSound: MediaPlayer? = null
    private var mMediaPlayerSoundWin: MediaPlayer? = null

    private var countImages = 0

    @BindView(R.id.puzzleFragment)
    internal var mRelativeLayout: RelativeLayout? = null


    override val layoutResId: Int
        get() {
            for (gifts in mGiftsList!!) {
                when (gifts.positionEnum) {
                    POSITION_TYPE.WATER -> countWater++
                    POSITION_TYPE.AIR -> countAir++
                    POSITION_TYPE.GROUND, POSITION_TYPE.UNKNOWN -> countGround++
                    else -> countGround++
                }
            }

            if (countAir == 1 && countWater == 1) {
                requery()
                return R.layout.fragment_puzzle_water_one_air
            }
            if (countAir == 2 && countWater == 1) {
                requery()
                return R.layout.fragment_puzzle_water_two_air
            }
            if (countWater == 1 && countGround == 3) {
                requery()
                return R.layout.fragment_puzzle_water_only
            }

            if (countAir == 2 && countGround == 2) {
                requery()
                return R.layout.fragment_puzzle_two_air_two_ground
            }
            if (countAir == 1 && countGround == 3) {
                requery()
                return R.layout.fragment_puzzle_one_air_three_ground
            }


            return R.layout.fragment_puzzle
        }

    private fun requery() {
        val containerAirs = ArrayList<Gifts>()
        val containerWaters = ArrayList<Gifts>()
        val containerCars = ArrayList<Gifts>()
        for (i in mGiftsList!!.indices) {
            if (mGiftsList!![i].positionEnum == POSITION_TYPE.AIR) {
                containerAirs.add(mGiftsList!![i])
                continue
            }
            if (mGiftsList!![i].positionEnum == POSITION_TYPE.WATER) {
                containerWaters.add(mGiftsList!![i])
                continue
            }
            containerCars.add(mGiftsList!![i])
        }
        for (i in containerCars.indices) {
            containerWaters.add(containerCars[i])
        }
        for (i in containerAirs.indices) {
            containerWaters.add(containerAirs[i])
        }
        mGiftsList = containerWaters
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val bundle = arguments
            mGiftsList = bundle!!.getParcelableArrayList(BUNDLE_TOYS)
        }

        val displaySize = Utility.displaySize
        displayWidth = displaySize!!.x
        displayHeight = displaySize!!.y
        // Определение размера картинки(ImageView) на экране
        imageWidth = (displayWidth / 3.0f).toInt()       // Т.е. занимает 1/3 экрана по ширине
        imageHeight = (displayHeight / 4.0f).toInt()     // и 1/4 по высоте
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImages()
    }


    private fun initImages() {
        // Инициализируем передвигаемые объекты (машинки)
        for (i in imgLeftCarsList!!.indices) {
            val imageView = imgLeftCarsList!![i]
            if (mGiftsList!!.size > i) {
                // Получаем значение int нашего изображение
                val resourceId = mGiftsList!![i].imageResId

                // Динамическая загрузка необходимых
                // изображений в пустые imgCar's

                Glide.with(imageView.context)
                        .load(resourceId)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .skipMemoryCache(true)
                        //.animate(R.anim.combo)
                        .into(imageView)
            }
            updateSize(imageView, imageWidth, imageHeight)
        }

        for (i in imgEmptyCarsList!!.indices) {
            val imageView = imgEmptyCarsList!![i]
            if (mGiftsList!!.size > i) {
                // Получаем значение int нашего изображения
                val resourceId = mGiftsList!![i].silhouetteResId


                // Динамическая загрузка необходимых
                // изображений в пустые imgCar's
                Glide.with(imageView.context)
                        .load(resourceId)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .skipMemoryCache(true)
                        //.animate(R.anim.change)
                        .into(imageView)
            }
            updateSize(imageView, imageWidth, imageHeight)
        }

        for (i in imgEmptyLeftCarsList!!.indices) {
            val imageView = imgEmptyLeftCarsList!![i]
            updateSize(imageView, imageWidth, imageHeight)
        }
    }

    private fun updateSize(view: View, width: Int, height: Int) {
        view.layoutParams.height = height
        view.layoutParams.width = width
        view.requestLayout()
    }

    private fun whatImage(view: View, event: MotionEvent) {
        for (i in imgLeftCarsList!!.indices) {
            val imageView = imgLeftCarsList!![i]
            if (imageView == view) {
                draggableImage = imgLeftCarsList!![i]
                targetEmptyImage = imgEmptyCarsList!![i]
                LoadResIdSound().execute()
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

    @OnTouch(R.id.puzzleFragment)
    internal fun rootTouched(view: View, event: MotionEvent): Boolean {
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
                    val lp = draggableImage!!.layoutParams as RelativeLayout.LayoutParams
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

                        if (mMediaPlayerSound != null && !mMediaPlayerSound!!.isPlaying)
                            mMediaPlayerSound!!.start()
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
        return true
    }

    @OnTouch(R.id.imgCar1, R.id.imgCar2, R.id.imgCar3, R.id.imgCar4)
    internal fun draggableImageTouched(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> whatImage(view, event)
        }
        return false
    }

    private fun showWinTextAndSound() {
        if (!mMediaPlayerSound!!.isPlaying && countImages >= 4) {
            mMediaPlayerSoundWin!!.start()
            val tv = TextView(context)
            tv.text = resources.getString(R.string.success)
            tv.textSize = (displayWidth / 23).toFloat()
            tv.setTextColor(resources.getColor(R.color.dorge_blue))
            val tvParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            tvParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            tv.gravity = Gravity.CENTER
            mRelativeLayout!!.addView(tv, tvParams)
            val anim = AnimationUtils.loadAnimation(context, R.anim.change_text)

            tv.setTypeface(Utility.typeface)
            tv.startAnimation(anim)
        }
    }

    protected fun onInitMediaPlayer() {
        if (mMediaPlayerSoundWin != null)
            mMediaPlayerSound!!.setOnCompletionListener {
                showWinTextAndSound()
                mMediaPlayerSoundWin!!.setOnCompletionListener {
                    val puzzleActivity = getActivity() as PuzzleActivity?
                    puzzleActivity!!.showPuzzleFragment(DBHelper.randomFourItems)
                }
            }

    }

    private inner class LoadResIdSound : AsyncTask<Void, Void, Void>() {
        internal var soundRawId: Int = 0
        internal var soundRawWin: Int = 0

        override fun doInBackground(vararg params: Void): Void? {
            /*soundRawId = MainActivity.muteSound ? R.raw.magic_sound : R.raw.mute_sound;
            soundRawWin = MainActivity.muteSound ? R.raw.sound_win : R.raw.mute_sound;*/
            soundRawId = R.raw.magic_sound
            soundRawWin = R.raw.sound_win
            if (soundRawId != 0 && soundRawWin != 0) {
                if (context != null)
                    mMediaPlayerSound = MediaPlayer.create(context, soundRawId)
                mMediaPlayerSoundWin = MediaPlayer.create(context, soundRawWin)
            }
            return null
        }

        override fun onPostExecute(aVoid: Void) {
            super.onPostExecute(aVoid)
            onInitMediaPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin!!.release()
            mMediaPlayerSoundWin = null
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound!!.release()
            mMediaPlayerSound = null
        }
    }

    companion object {

        val TAG = PuzzleFragment::class.java.name


        private val BUNDLE_TOYS = "BUNDLE_KEY"

        fun newInstance(toyList: List<Gifts>): PuzzleFragment {
            val fragment = PuzzleFragment()
            val bundle = Bundle()

            bundle.putParcelableArrayList(BUNDLE_TOYS, toyList as ArrayList<out Parcelable>)
            fragment.arguments = bundle
            return fragment
        }
    }

}
