package android1601.itstep.org.kidsgame.program.fragments.scratch_egg

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import android1601.itstep.org.kidsgame.program.ext.fromJson
import android1601.itstep.org.kidsgame.program.fragments.KEY_CARS_FOR_PUZZLE_ENTRY
import android1601.itstep.org.kidsgame.program.fragments.KEY_GIFT_ENTRY
import android1601.itstep.org.kidsgame.program.ui.base.BaseMvpFragment
import android1601.itstep.org.kidsgame.scratch_utility.ScratchImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_kotlin_scratch_image.*

class ScratchEggKotlinFragment : BaseMvpFragment<ScratchPresenter, ScratchView>(), ScratchView {

    override val presenter: ScratchPresenter by lazy { ScratchPresenter(giftsEntry) }
    override val mvpView: ScratchView = this
    override val layoutRes: Int = R.layout.fragment_kotlin_scratch_image

    private val giftsEntry
        get() = Gson().fromJson<Gifts>(arguments?.getString(KEY_GIFT_ENTRY) ?: "")

    private val carsForPuzzle get() = arguments?.getBoolean(KEY_CARS_FOR_PUZZLE_ENTRY) ?: true

    private val animWinText: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.change_text_scale) }
    private val mAnimation by lazy { AnimationUtils.loadAnimation(activity, R.anim.combo) }
    private var flagFromPuzzles = false

    companion object {

        fun newInstance(gifts: Gifts, carsForPuzzle: Boolean): ScratchEggKotlinFragment {
            return ScratchEggKotlinFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_GIFT_ENTRY, Gson().toJson(gifts))
                    putBoolean(KEY_CARS_FOR_PUZZLE_ENTRY, carsForPuzzle)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!carsForPuzzle) {
            val howRemainder = 4 - DBHelper.getUnlockedBySection().size
            tvCounter.isVisible = true
            if (howRemainder > 0) {
                tvCounter.text = context!!.getString(R.string.lacking_of_items) + " " + howRemainder
                tvCounter.typeface = Utility.getTypeface()
            }
        }
        btnYet.setOnClickListener { refresh() }
    }

    override fun onInitImageAndText(toyImageId: Int, toyTextId: Int) {
        if (toyImageId != 0) {
            val revealedImage = view?.findViewById<ImageView>(R.id.revealedImage)
            if (revealedImage != null) {
                Glide.with(context!!)
                        .load(toyImageId)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(revealedImage)
            }
        }
        if (toyTextId != 0) {
            try {
                view?.findViewById<TextView>(R.id.imageName)?.setText(toyTextId)

            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }
        scratchImage.setImageResource(toyImageId)
        scratchImage.setRevealListener(object : ScratchImageView.IRevealListener {
            override fun onRevealed(iv: ScratchImageView) {

            }

            override fun onRevealPercentChangedListener(siv: ScratchImageView, percent: Float) {
                // Проверка для того чтобы когда пользователь задерживает палец на скречивании не проявлялось повторных действий анимации
                if (!btnYet.isEnabled) {
                    presenter.checkRevealed(percent)
                }
            }
        })
    }

    override fun revealImage(withAnimation: Boolean) {
        scratchImage.visibility = View.INVISIBLE
        revealedImage.visibility = View.VISIBLE
        imageName.visibility = View.VISIBLE

        val mAnimation = AnimationUtils.loadAnimation(activity, R.anim.combo)
        revealedImage.startAnimation(mAnimation)
        btnYet.isEnabled = true
        tvCounter.visibility = if (carsForPuzzle) View.INVISIBLE else View.VISIBLE

        if (!carsForPuzzle) {
            val howRemainder = 4 - DBHelper.getUnlockedBySection().size
            tvCounter.text = if (howRemainder == 0) null else context!!.getString(R.string.lacking_of_items) + " " + howRemainder

            if (DBHelper.getUnlockedBySection().size >= 4) {
                btnYet.isEnabled = false
                presenter.showWinTextSoundButton()
            }

        }
    }

    override fun startTransformations() {
        tvCounter.text = resources.getString(R.string.go_to_puzzle)
        tvCounter.setTextColor(resources.getColor(R.color.dorge_blue))

        tvCounter.setTypeface(Utility.getTypeface())
        tvCounter.startAnimation(animWinText)
        animWinText.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                btnYet.startAnimation(mAnimation)
                btnYet.isEnabled = true
                btnYet.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                tvCounter.visibility = View.INVISIBLE
                //btnYet.setVisibility(View.VISIBLE);
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        btnYet.setText(R.string.go_to_puzzle_fragment)
        flagFromPuzzles = true
    }

    private fun refresh() {
        btnYet.isEnabled = false
        // isCarsForPuzzle - флаг, откуда вызван метод :    true - из MainActivity
        //                                                  false - при нажатии на PuzzleActivity с учетом нехватки автомобилей

        if (DBHelper.getUnlockedBySection().size >= 4 && flagFromPuzzles) {
            requireActivity().finish()
            navigator.showOpenPuzzlesKotlinView(false)
        } else {
            // todo recreate this fragment
            navigator.showScratchEggFragment(carsForPuzzle)
        }
    }


}