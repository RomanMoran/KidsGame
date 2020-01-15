package android1601.itstep.org.kidsgame.program.fragments;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import butterknife.BindView;

/**
 * Created by roman on 27.03.2017.
 */

public class BaseEggDetailFragment extends BaseFragment {

    public Gifts mGiftsList = null;
    @BindView(R.id.revealedImage)
    ImageView imgView;
    @BindView(R.id.imageName)
    TextView imageName;
    protected MediaPlayer mMediaPlayerVoice;
    protected MediaPlayer mMediaPlayerSound;
    protected MediaPlayer mMediaPlayerSoundWin;

    public static final String BASE_EGG_FRAGMEN_ARGS = "BASE_EGG_FRAGMENT";

    public static BaseEggDetailFragment newInstance(){
        BaseEggDetailFragment fragment = new BaseEggDetailFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_toy_details;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            mGiftsList = bundle.getParcelable(BASE_EGG_FRAGMEN_ARGS);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new LoadResIdTask().execute();
        new LoadResAudioTask().execute();
    }

    protected void onInitImageAndText(int toyImageId, int toyTextId) {
        if (toyImageId != 0)
        {
            Glide.with(getContext())
                    .load(toyImageId)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(imgView);
        }
        if (toyTextId != 0)
            try {
                imageName.setText(toyTextId);
            }catch (Resources.NotFoundException e){
                e.printStackTrace();
            }


    }

    protected void onInitMediaPlayer (){
        if (mMediaPlayerVoice != null)
            mMediaPlayerVoice.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    startItemSound();
                }
            });

    }

    protected void startItemSound(){
        if (mMediaPlayerSound != null && !mMediaPlayerVoice.isPlaying())
            mMediaPlayerSound.start();
    }


    private class LoadResIdTask extends AsyncTask<Void,Void,Void>{
        private int toyImageId;
        private int toyTextId;

        @Override
        protected Void doInBackground(Void... params) {
            if (mGiftsList != null){
                toyImageId = Utility.getDrawableResourceIdByName(mGiftsList.getResName());
                toyTextId = mGiftsList.getTextId();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            onInitImageAndText(toyImageId,toyTextId);
        }
    }

    private class LoadResAudioTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            if (mGiftsList != null){
                int voiceRawId = mGiftsList.getVoiceRawId();
                if (voiceRawId != 0)
                    if (getContext() != null)
                        mMediaPlayerVoice = MediaPlayer.create(getContext(), voiceRawId);
                int soundRawId = mGiftsList.getSoundRawId();
                if (soundRawId != 0)
                    if (getContext() != null)
                        mMediaPlayerSound = MediaPlayer.create(getContext(), soundRawId);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onInitMediaPlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice.release();
            mMediaPlayerVoice = null;
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound.release();
            mMediaPlayerSound = null;
        }
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin.release();
            mMediaPlayerSoundWin = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice.pause();
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound.pause();
        }
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin.pause();
        }
    }

    public void stopAllAudio() {
        if (mMediaPlayerVoice != null) {
            mMediaPlayerVoice.pause();
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


}
