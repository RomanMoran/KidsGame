package android1601.itstep.org.kidsgame.program.fragments

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.data.Gifts
import butterknife.BindView

/**
 * Created by roman on 27.03.2017.
 */

open class BaseEggDetailFragment : BaseFragment() {

    var mGiftsList: Gifts? = null
    protected var mMediaPlayerVoice: MediaPlayer? = null
    protected var mMediaPlayerSound: MediaPlayer? = null
    protected var mMediaPlayerSoundWin: MediaPlayer? = null

    protected override val layoutResId: Int
        get() = R.layout.fragment_toy_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val bundle = arguments
            mGiftsList = bundle!!.getParcelable(BASE_EGG_FRAGMEN_ARGS)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoadResIdTask().execute()
        LoadResAudioTask().execute()
    }

    protected open fun onInitImageAndText(toyImageId: Int, toyTextId: Int) {
        if (toyImageId != 0) {
            val revealedImage = view?.findViewById<ImageView>(R.id.revealedImage)
            Glide.with(context!!)
                    .load(toyImageId)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(revealedImage!!)
        }
        if (toyTextId != 0)
            try {
                val imageName = view?.findViewById<TextView>(R.id.imageName)
                imageName!!.setText(toyTextId)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }


    }

    protected open fun onInitMediaPlayer() {
        if (mMediaPlayerVoice != null)
            mMediaPlayerVoice!!.setOnCompletionListener { startItemSound() }

    }

    protected open fun startItemSound() {
        if (mMediaPlayerSound != null && !mMediaPlayerVoice!!.isPlaying)
            mMediaPlayerSound!!.start()
    }


    private inner class LoadResIdTask : AsyncTask<Void, Void, Void>() {
        private var toyImageId: Int = 0
        private var toyTextId: Int = 0

        override fun doInBackground(vararg params: Void): Void? {
            if (mGiftsList != null) {
                toyImageId = Utility.getDrawableResourceIdByName(mGiftsList!!.resName!!)
                toyTextId = mGiftsList!!.textId
            }
            return null
        }

        override fun onPostExecute(aVoid: Void) {
            super.onPostExecute(aVoid)

            onInitImageAndText(toyImageId, toyTextId)
        }
    }

    private inner class LoadResAudioTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            if (mGiftsList != null) {
                val voiceRawId = mGiftsList!!.voiceRawId
                if (voiceRawId != 0)
                    if (context != null)
                        mMediaPlayerVoice = MediaPlayer.create(context, voiceRawId)
                val soundRawId = mGiftsList!!.soundRawId
                if (soundRawId != 0)
                    if (context != null)
                        mMediaPlayerSound = MediaPlayer.create(context, soundRawId)

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
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice!!.release()
            mMediaPlayerVoice = null
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound!!.release()
            mMediaPlayerSound = null
        }
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin!!.release()
            mMediaPlayerSoundWin = null
        }
    }

    override fun onPause() {
        super.onPause()
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice!!.pause()
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound!!.pause()
        }
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin!!.pause()
        }
    }

    fun stopAllAudio() {
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice!!.pause()
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound!!.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        val decorView = getActivity()!!.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    companion object {

        val BASE_EGG_FRAGMEN_ARGS = "BASE_EGG_FRAGMENT"

        fun newInstance(): BaseEggDetailFragment {
            return BaseEggDetailFragment()
        }
    }


}
