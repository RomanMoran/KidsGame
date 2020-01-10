package android1601.itstep.org.kidsgame.program.fragments

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.ext.fromJson
import android1601.itstep.org.kidsgame.program.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson

const val KEY_GIFT_ENTRY =
        "BaseEggDetailKotlinFragment.KEY_GIFT_ENTRY"
const val KEY_CARS_FOR_PUZZLE_ENTRY =
        "BaseEggDetailKotlinFragment.KEY_CARS_FOR_PUZZLE_ENTRY"


class BaseEggDetailKotlinFragment : BaseFragment() {

    override val layoutRes: Int = R.layout.fragment_toy_details

    private val giftsEntry
        get() = Gson().fromJson<Gifts>(arguments?.getString(KEY_GIFT_ENTRY) ?: "")

    protected var mMediaPlayerVoice: MediaPlayer? = null
    protected var mMediaPlayerSound: MediaPlayer? = null
    protected var mMediaPlayerSoundWin: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadResIdTask().execute()
        LoadResAudioTask().execute()
    }


    protected fun onInitImageAndText(toyImageId: Int, toyTextId: Int) {
        if (toyImageId != 0) {
            val revealedImage = view?.findViewById<ImageView>(R.id.revealedImage)
            if (revealedImage != null) {
                Glide.with(context!!)
                        .load(toyImageId)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(revealedImage)
            }
        }
        if (toyTextId != 0)
            try {
                view?.findViewById<TextView>(R.id.imageName)?.setText(toyTextId)

            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
    }


    protected fun onInitMediaPlayer() {
        if (mMediaPlayerVoice != null)
            mMediaPlayerVoice?.setOnCompletionListener { startItemSound() }
    }

    protected fun startItemSound() {
        if (mMediaPlayerSound != null && !mMediaPlayerVoice?.isPlaying!!)
            mMediaPlayerSound?.start()
    }

    private inner class LoadResIdTask : AsyncTask<Void, Void, Void>() {
        private var toyImageId: Int = 0
        private var toyTextId: Int = 0

        override fun doInBackground(vararg params: Void): Void? {
            if (giftsEntry != null) {
                toyImageId = Utility.getDrawableResourceIdByName(giftsEntry.resName)
                toyTextId = giftsEntry.textId
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
            if (giftsEntry != null) {
                val voiceRawId = giftsEntry.voiceRawId
                if (voiceRawId != 0)
                    if (context != null)
                        mMediaPlayerVoice = MediaPlayer.create(context, voiceRawId)
                val soundRawId = giftsEntry.soundRawId
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
            mMediaPlayerVoice?.release()
            mMediaPlayerVoice = null
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound?.release()
            mMediaPlayerSound = null
        }
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin?.release()
            mMediaPlayerSoundWin = null
        }
    }

    override fun onPause() {
        super.onPause()
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice?.pause()
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound?.pause()
        }
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin?.pause()
        }
    }

    fun stopAllAudio() {
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice?.pause()
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound?.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        val decorView = activity!!.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }


}