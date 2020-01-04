package android1601.itstep.org.kidsgame.program.fragments

import android.graphics.Point
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.activity.BaseActivity
import android1601.itstep.org.kidsgame.program.activity.KinderActivity
import android1601.itstep.org.kidsgame.program.activity.MainActivity
import android1601.itstep.org.kidsgame.program.activity.PuzzleActivity
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import android1601.itstep.org.kidsgame.scratch_utility.ScratchImageView
import butterknife.BindView
import butterknife.OnClick

/**
 * Created by roman on 13.03.2017.
 */

class ScratchEggFragment : BaseEggDetailFragment() {

    @BindView(R.id.scratchImage)
    internal var scratchImageView: ScratchImageView? = null
    @BindView(R.id.btnYet)
    internal var btnYet: Button? = null
    @BindView(R.id.tvCounter)
    internal var tvCounter: TextView? = null
    private var mAnimation: Animation? = null
    private var mGifts: Gifts? = null
    private var isCarsForPuzzle: Boolean = false
    private var flagFromPuzzles = false
    private var animWinText: Animation? = null

    internal lateinit var display: Display
    internal lateinit var size: Point

    protected override val layoutResId: Int
        get() = R.layout.fragment_scratch_image


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val bundle = arguments
            mGifts = bundle!!.getParcelable(BaseEggDetailFragment.BASE_EGG_FRAGMEN_ARGS)
            isCarsForPuzzle = bundle.getBoolean(MainActivity.CARS_FOR_PUZZLE, true)
        }
        animWinText = AnimationUtils.loadAnimation(context, R.anim.change_text_scale)
        display = getActivity()!!.windowManager.defaultDisplay
        size = Point()
        display.getSize(size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnYet!!.isEnabled = false
        imgView!!.visibility = View.INVISIBLE
        imageName!!.visibility = View.INVISIBLE
        if (!isCarsForPuzzle) {
            val howRemainder = 4 - DBHelper.unlockedBySection.size
            if (howRemainder > 0) {
                tvCounter!!.text = context!!.getString(R.string.lacking_of_items) + " " + howRemainder
                tvCounter!!.setTypeface(Utility.typeface)
            }
        }//if
        tvCounter!!.visibility = if (isCarsForPuzzle) View.INVISIBLE else View.VISIBLE

    }

    private fun showWinTextSoundButton() {
        if (mMediaPlayerSound != null) {
            // В случае когда есть звук сопровождения
            mMediaPlayerSound!!.setOnCompletionListener {
                if (!mMediaPlayerSound!!.isPlaying && DBHelper.unlockedBySection.size >= 4) {
                    startTransformations()
                }
            }
        } else {
            // В случае когда нет звука сопровождения
            mMediaPlayerVoice!!.setOnCompletionListener { startTransformations() }
        }
    }

    private fun startTransformations() {
        mMediaPlayerSoundWin = MediaPlayer.create(context, R.raw.sound_win_egg)
        mMediaPlayerSoundWin!!.start()
        tvCounter!!.text = resources.getString(R.string.go_to_puzzle)
        tvCounter!!.setTextColor(resources.getColor(R.color.dorge_blue))

        tvCounter!!.setTypeface(Utility.typeface)
        tvCounter!!.startAnimation(animWinText)
        animWinText!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                btnYet!!.startAnimation(mAnimation)
                btnYet!!.setBackgroundColor(resources.getColor(R.color.dorge_blue))
                btnYet!!.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                tvCounter!!.visibility = View.INVISIBLE
                //btnYet.setVisibility(View.VISIBLE);
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        btnYet!!.setText(R.string.go_to_puzzle_fragment)
        flagFromPuzzles = true
    }


    fun checkRevealed(percent: Float) {
        if (percent > 0.40) {
            if (mGifts != null) {
                // Редактированиие в БД открываем машинку

                mGifts!!.isUnlock = true
                mGifts!!.save()
            }//if
            if (mMediaPlayerVoice != null) {
                if (!mMediaPlayerVoice!!.isPlaying) {
                    mMediaPlayerVoice!!.start()
                    mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.combo)
                    imgView!!.startAnimation(mAnimation)
                } else {
                    startItemSound()
                }
            }//if

            scratchImageView!!.visibility = View.INVISIBLE
            imgView!!.visibility = View.VISIBLE
            imageName!!.visibility = View.VISIBLE
            if (!isCarsForPuzzle) {
                val howRemainder = 4 - DBHelper.unlockedBySection.size
                tvCounter!!.text = if (howRemainder == 0) null else context!!.getString(R.string.lacking_of_items) + " " + howRemainder

                if (DBHelper.unlockedBySection.size >= 4) {
                    showWinTextSoundButton()
                }

            }//if
            tvCounter!!.visibility = if (isCarsForPuzzle) View.INVISIBLE else View.VISIBLE
        }//if
        Log.d(javaClass.name, "Revealed " + percent * 100 + "%")
    }


    override fun startItemSound() {
        btnYet!!.isEnabled = true
        if (scratchImageView!!.mMediaPlayer.isPlaying) {
            scratchImageView!!.mMediaPlayer.pause()
            scratchImageView!!.mMediaPlayer.stop()
        }
        if (!isCarsForPuzzle && DBHelper.unlockedBySection.size >= 4) btnYet!!.visibility = View.INVISIBLE
        super.startItemSound()
    }

    override fun onInitImageAndText(toyImageId: Int, toyTextId: Int) {
        super.onInitImageAndText(toyImageId, toyTextId)
        scratchImageView!!.setImageResource(toyImageId)
        scratchImageView!!.setRevealListener(object : ScratchImageView.IRevealListener {
            override fun onRevealed(iv: ScratchImageView) {

            }

            override fun onRevealPercentChangedListener(siv: ScratchImageView, percent: Float) {
                // Проверка для того чтобы когда пользователь задерживает палец на скречивании не проявлялось повторных действий анимации
                if (!btnYet!!.isEnabled) checkRevealed(percent)
            }
        })
    }

    @OnClick(R.id.btnYet)
    internal fun refresh() {
        btnYet!!.isEnabled = false
        val kinderActivity = getActivity() as KinderActivity?
        // isCarsForPuzzle - флаг, откуда вызван метод :    true - из MainActivity
        //                                                  false - при нажатии на PuzzleActivity с учетом нехватки автомобилей

        if (DBHelper.unlockedBySection.size >= 4 && flagFromPuzzles) {
            BaseActivity.newInstance(requireContext(), PuzzleActivity::class.java)
            activity.finish()
        } else
            kinderActivity!!.showScratchEggFragment(isCarsForPuzzle)
    }

    companion object {
        val TAG = ScratchEggFragment::class.java.name

        fun newInstance(gifts: Gifts, carsForPuzzle: Boolean): ScratchEggFragment {
            val fragment = ScratchEggFragment()
            val args = Bundle()

            //args.putParcelableArrayList(SCRATCH_EGG,gifts);
            args.putBoolean(MainActivity.CARS_FOR_PUZZLE, carsForPuzzle)
            args.putParcelable(BaseEggDetailFragment.BASE_EGG_FRAGMEN_ARGS, gifts)

            fragment.arguments = args
            return fragment
        }
    }


}//ScratchEggFragment
