package android1601.itstep.org.kidsgame.program.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import butterknife.OnClick;

/**
 * Created by roman on 10.04.2017.
 */

public class ToyDetailsFragment extends BaseEggDetailFragment {

    // Описание содержимого фрагмента

    public static final String TAG = ToyDetailsFragment.class.getName();
    //public static final String ARGS = "Args";
    private boolean needStartMusic = false;

    public static ToyDetailsFragment newInstance(/*ArrayList<Gifts>gifts*/Gifts gifts){
        ToyDetailsFragment fragment = new ToyDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BASE_EGG_FRAGMEN_ARGS, gifts);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    protected void onInitImageAndText(int toyImageId, int toyTextId) {
        if (mGiftsList.isUnlock()) {
            super.onInitImageAndText(toyImageId, toyTextId);
        }else {
            String image = Utility.getStringResourseById(toyImageId);
            image+="_inactive";
            toyImageId = Utility.getDrawableResourceIdByName(image);
            imgView.setImageResource(toyImageId);
            imageName.setText(R.string.blocked);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_toy_details;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgView.setEnabled(false);
    }

    @Override
    protected void startItemSound() {
        super.startItemSound();
        imgView.setEnabled(true);
    }

    @OnClick(R.id.revealedImage)
    void refresh(){
        imgView.setEnabled(false);
        startVoice();
    }

    public void startVoice() {
        if (!mGiftsList.isUnlock())
            return;
        if (mMediaPlayerVoice != null ){
            needStartMusic = false;
            if (!mMediaPlayerVoice.isPlaying()){
                // Проверяем если звук играет то его отключаем и если он не нул
                if (mMediaPlayerSound!=null && mMediaPlayerSound.isPlaying())
                    mMediaPlayerSound.pause();
                mMediaPlayerVoice.start();
                Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.combo);
                imgView.startAnimation(mAnimation);

            }
        }else{
            needStartMusic = true;
        }
    }

    public void stopAudio() {
        needStartMusic = false;
        stopAllAudio();
    }

    @Override
    protected void onInitMediaPlayer() {
        super.onInitMediaPlayer();
        if (needStartMusic) startVoice();
    }
}
