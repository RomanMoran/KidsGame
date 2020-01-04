package android1601.itstep.org.kidsgame.program.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.data.Gifts
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_toy_details.*

/**
 * Created by roman on 10.04.2017.
 */

class ToyDetailsFragment : BaseEggDetailFragment() {
    //public static final String ARGS = "Args";
    private var needStartMusic = false

    protected override val layoutResId: Int
        get() = R.layout.fragment_toy_details


    override fun onInitImageAndText(toyImageId: Int, toyTextId: Int) {
        var toyImageId = toyImageId
        if (mGiftsList!!.isUnlock) {
            super.onInitImageAndText(toyImageId, toyTextId)
        } else {
            var image = Utility.getStringResourseById(toyImageId)
            image += "_inactive"
            toyImageId = Utility.getDrawableResourceIdByName(image)
            revealedImage!!.setImageResource(toyImageId)
            imageName!!.setText(R.string.blocked)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        revealedImage!!.isEnabled = false
    }

    override fun startItemSound() {
        super.startItemSound()
        revealedImage!!.isEnabled = true
    }

    @OnClick(R.id.revealedImage)
    internal fun refresh() {
        revealedImage!!.isEnabled = false
        startVoice()
    }

    fun startVoice() {
        if (!mGiftsList!!.isUnlock)
            return
        if (mMediaPlayerVoice != null) {
            needStartMusic = false
            if (!mMediaPlayerVoice!!.isPlaying) {
                // Проверяем если звук играет то его отключаем и если он не нул
                if (mMediaPlayerSound != null && mMediaPlayerSound!!.isPlaying)
                    mMediaPlayerSound!!.pause()
                mMediaPlayerVoice!!.start()
                val mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.combo)
                revealedImage!!.startAnimation(mAnimation)

            }
        } else {
            needStartMusic = true
        }
    }

    fun stopAudio() {
        needStartMusic = false
        stopAllAudio()
    }

    override fun onInitMediaPlayer() {
        super.onInitMediaPlayer()
        if (needStartMusic) startVoice()
    }

    companion object {

        // Описание содержимого фрагмента

        val TAG = ToyDetailsFragment::class.java.name

        fun newInstance(/*ArrayList<Gifts>gifts*/gifts: Gifts): ToyDetailsFragment {
            val fragment = ToyDetailsFragment()
            val args = Bundle()
            args.putParcelable(BaseEggDetailFragment.BASE_EGG_FRAGMEN_ARGS, gifts)
            fragment.arguments = args

            return fragment
        }
    }
}
