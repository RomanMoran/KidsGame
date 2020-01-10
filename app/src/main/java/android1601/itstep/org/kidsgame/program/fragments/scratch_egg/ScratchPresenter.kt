package android1601.itstep.org.kidsgame.program.fragments.scratch_egg

import android.media.MediaPlayer
import android.os.AsyncTask
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import android1601.itstep.org.kidsgame.program.di.ContextProvider
import android1601.itstep.org.kidsgame.program.ui.base.BasePresenterImpl

class ScratchPresenter(private val giftsEntry: Gifts?) : BasePresenterImpl<ScratchView>() {

    protected var mMediaPlayerVoice: MediaPlayer? = null
    protected var mMediaPlayerSound: MediaPlayer? = null
    protected var mMediaPlayerSoundWin: MediaPlayer? = null

    private val context by ContextProvider()

    override fun onAttachView() {
        super.onAttachView()
        LoadResIdTask().execute()
        LoadResAudioTask().execute()
    }

    private inner class LoadResIdTask : AsyncTask<Void?, Void?, Void?>() {
        private var toyImageId: Int = 0
        private var toyTextId: Int = 0

        override fun doInBackground(vararg params: Void?): Void? {
            if (giftsEntry != null) {
                toyImageId = Utility.getDrawableResourceIdByName(giftsEntry.resName)
                toyTextId = giftsEntry.textId
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)

            view?.onInitImageAndText(toyImageId, toyTextId)
        }
    }

    private inner class LoadResAudioTask : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
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

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            onInitMediaPlayer()
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

    override fun onDetachView() {
        super.onDetachView()
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

    fun checkRevealed(percent: Float) {
        if (percent > 0.40) {
            if (giftsEntry != null) {
                // Редактированиие в БД открываем машинку

                giftsEntry.isUnlock = true
                giftsEntry.save()
            }
            if (mMediaPlayerVoice != null) {
                if (!mMediaPlayerVoice!!.isPlaying) {
                    mMediaPlayerVoice?.start()
                    //mAnimation = AnimationUtils.loadAnimation(activity, R.anim.combo)
                    //imgView.startAnimation(mAnimation)
                } else {
                    startItemSound()
                }
            }
            view?.revealImage()
        }
    }

    fun showWinTextSoundButton() {
        if (mMediaPlayerSound != null) {
            // В случае когда есть звук сопровождения
            mMediaPlayerSound!!.setOnCompletionListener {
                if (!mMediaPlayerSound!!.isPlaying && DBHelper.getUnlockedBySection().size >= 4) {
                    startTransformation()
                }
            }
        } else {
            // В случае когда нет звука сопровождения
            mMediaPlayerVoice?.setOnCompletionListener { startTransformation() }
        }
    }

    fun startTransformation() {
        mMediaPlayerSoundWin = MediaPlayer.create(context, R.raw.sound_win_egg)
        mMediaPlayerSoundWin?.start()
        view?.startTransformations()
    }

}