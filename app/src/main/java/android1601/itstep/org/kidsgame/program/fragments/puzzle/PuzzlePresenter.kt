package android1601.itstep.org.kidsgame.program.fragments.puzzle

import android.media.MediaPlayer
import android.os.AsyncTask
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.di.ContextProvider
import android1601.itstep.org.kidsgame.program.enums.POSITION_TYPE
import android1601.itstep.org.kidsgame.program.ui.base.BasePresenterImpl
import java.util.*

class PuzzlePresenter(private var giftsEntry: List<Gifts>) : BasePresenterImpl<PuzzleView>() {

    private var countWater = 0
    private var countAir = 0
    private var countGround = 0

    private val context by ContextProvider()

    var countImages = 0

    private var mMediaPlayerSound: MediaPlayer? = null
    private var mMediaPlayerSoundWin: MediaPlayer? = null

    fun getCompatibleLayout(): Int {
        for (gifts in giftsEntry) {
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
        for (i in giftsEntry.indices) {
            if (giftsEntry[i].positionEnum == POSITION_TYPE.AIR) {
                containerAirs.add(giftsEntry[i])
                continue
            }
            if (giftsEntry[i].positionEnum == POSITION_TYPE.WATER) {
                containerWaters.add(giftsEntry[i])
                continue
            }
            containerCars.add(giftsEntry[i])
        }
        for (i in containerCars.indices) {
            containerWaters.add(containerCars[i])
        }
        for (i in containerAirs.indices) {
            containerWaters.add(containerAirs[i])
        }
        giftsEntry = containerWaters
    }

    fun startLoadResIdSound() {
        LoadResIdSound().execute()
    }

    fun play() {
        if (mMediaPlayerSound != null && !mMediaPlayerSound!!.isPlaying())
            mMediaPlayerSound!!.start()
    }


    private inner class LoadResIdSound : AsyncTask<Void?, Void?, Void?>() {
        internal var soundRawId: Int = 0
        internal var soundRawWin: Int = 0

        override fun doInBackground(vararg params: Void?): Void? {
            /*soundRawId = MainActivity.muteSound ? R.raw.magic_sound : R.raw.mute_sound;
            soundRawWin = MainActivity.muteSound ? R.raw.sound_win : R.raw.mute_sound;*/
            soundRawId = R.raw.magic_sound
            soundRawWin = R.raw.sound_win
            if (soundRawId != 0 && soundRawWin != 0) {
                mMediaPlayerSound = MediaPlayer.create(context, soundRawId)
                mMediaPlayerSoundWin = MediaPlayer.create(context, soundRawWin)
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            onInitMediaPlayer()
        }
    }


    protected fun onInitMediaPlayer() {
        if (mMediaPlayerSoundWin != null)
            mMediaPlayerSound?.setOnCompletionListener {
                if (!mMediaPlayerSound!!.isPlaying && countImages >= 4) {
                    view?.showWinTextAndSound()
                    mMediaPlayerSoundWin?.start()
                }
                mMediaPlayerSoundWin!!.setOnCompletionListener {
                    getNavigator().showPuzzleFragment()
                }
            }

    }

    override fun onDetachView() {
        super.onDetachView()
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin?.release()
            mMediaPlayerSoundWin = null
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound?.release()
            mMediaPlayerSound = null
        }
    }



}