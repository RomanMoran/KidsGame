package android1601.itstep.org.kidsgame.program.fragments;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.activity.BaseActivity;
import android1601.itstep.org.kidsgame.program.activity.KinderActivity;
import android1601.itstep.org.kidsgame.program.activity.MainActivity;
import android1601.itstep.org.kidsgame.program.activity.PuzzleActivity;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper;
import android1601.itstep.org.kidsgame.scratch_utility.ScratchImageView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by roman on 13.03.2017.
 */

public class ScratchEggFragment extends BaseEggDetailFragment{
    public static final String TAG = ScratchEggFragment.class.getName();

    @BindView(R.id.scratchImage)
    ScratchImageView scratchImageView;
    @BindView(R.id.btnYet)
    Button btnYet;
    @BindView(R.id.tvCounter)
    TextView tvCounter;
    private Animation mAnimation;
    private Gifts mGifts = null;
    private boolean isCarsForPuzzle;
    private boolean flagFromPuzzles = false;
    private Animation animWinText;

    Display display;
    Point size;

    public static ScratchEggFragment newInstance(Gifts gifts, boolean carsForPuzzle){
        ScratchEggFragment fragment = new ScratchEggFragment();
        Bundle args = new Bundle();

        //args.putParcelableArrayList(SCRATCH_EGG,gifts);
        args.putBoolean(MainActivity.CARS_FOR_PUZZLE,carsForPuzzle);
        args.putParcelable(BASE_EGG_FRAGMEN_ARGS, gifts);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {return R.layout.fragment_scratch_image;}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            mGifts = bundle.getParcelable(BASE_EGG_FRAGMEN_ARGS);
            isCarsForPuzzle = bundle.getBoolean(MainActivity.CARS_FOR_PUZZLE,true);
        }
        animWinText = AnimationUtils.loadAnimation(getContext(),R.anim.change_text_scale);
        display = getActivity().getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnYet.setEnabled(false);
        imgView.setVisibility(View.INVISIBLE);
        imageName.setVisibility(View.INVISIBLE);
        if (!isCarsForPuzzle) {
            int howRemainder = (4 - DBHelper.getUnlockedBySection().size());
            if (howRemainder >0){tvCounter.setText(getContext().getString(R.string.lacking_of_items)+" "+ howRemainder);
                tvCounter.setTypeface(Utility.getTypeface());
            }
        }//if
        tvCounter.setVisibility(isCarsForPuzzle ? View.INVISIBLE : View.VISIBLE);

    }

    private void showWinTextSoundButton(){
        if (mMediaPlayerSound!=null) {
            // В случае когда есть звук сопровождения
            mMediaPlayerSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mMediaPlayerSound.isPlaying() && DBHelper.getUnlockedBySection().size() >= 4) {
                        startTransformations();
                    }
                }
            });
        }else{
            // В случае когда нет звука сопровождения
            mMediaPlayerVoice.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    startTransformations();
                }
            });
        }
    }

    private void startTransformations(){
        mMediaPlayerSoundWin = MediaPlayer.create(getContext(), R.raw.sound_win_egg);
        mMediaPlayerSoundWin.start();
        tvCounter.setText(getResources().getString(R.string.go_to_puzzle));
        tvCounter.setTextColor(getResources().getColor(R.color.dorge_blue));

        tvCounter.setTypeface(Utility.getTypeface());
        tvCounter.startAnimation(animWinText);
        animWinText.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btnYet.startAnimation(mAnimation);
                btnYet.setBackgroundColor(getResources().getColor(R.color.dorge_blue));
                btnYet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvCounter.setVisibility(View.INVISIBLE);
                //btnYet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        btnYet.setText(R.string.go_to_puzzle_fragment);
        flagFromPuzzles = true;
    }


    public void checkRevealed(float percent){
        if (percent > 0.40){
            if (mGifts != null){
                // Редактированиие в БД открываем машинку

                mGifts.setUnlock(true);
                mGifts.save();
            }//if
            if (mMediaPlayerVoice != null){
                if (!mMediaPlayerVoice.isPlaying()){
                    mMediaPlayerVoice.start();
                    mAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.combo);
                    imgView.startAnimation(mAnimation);
                }else{
                    startItemSound();
                }
            }//if

            scratchImageView.setVisibility(View.INVISIBLE);
            imgView.setVisibility(View.VISIBLE);
            imageName.setVisibility(View.VISIBLE);
            if (!isCarsForPuzzle) {
                int howRemainder = (4 - DBHelper.getUnlockedBySection().size());
                tvCounter.setText(howRemainder==0?null:getContext().getString(R.string.lacking_of_items)+" "+howRemainder);

                if (DBHelper.getUnlockedBySection().size()>=4) {
                    showWinTextSoundButton();
                }

            }//if
            tvCounter.setVisibility(isCarsForPuzzle ?View.INVISIBLE:View.VISIBLE);
        }//if
        Log.d(getClass().getName(),"Revealed " + percent * 100 + "%");
    }


    @Override
    protected void startItemSound(){
        btnYet.setEnabled(true);
        if (scratchImageView.mMediaPlayer.isPlaying()){
            scratchImageView.mMediaPlayer.pause();
            scratchImageView.mMediaPlayer.stop();
        }
        if (!isCarsForPuzzle && DBHelper.getUnlockedBySection().size()>=4) btnYet.setVisibility(View.INVISIBLE);
        super.startItemSound();
    }

    @Override
    protected void onInitImageAndText(int toyImageId, int toyTextId) {
        super.onInitImageAndText(toyImageId, toyTextId);
        scratchImageView.setImageResource(toyImageId);
        scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView iv) {

            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                // Проверка для того чтобы когда пользователь задерживает палец на скречивании не проявлялось повторных действий анимации
                if (!btnYet.isEnabled())checkRevealed(percent);
            }
        });
    }

    @OnClick(R.id.btnYet) void refresh(){
        btnYet.setEnabled(false);
        KinderActivity kinderActivity = (KinderActivity)getActivity();
        // isCarsForPuzzle - флаг, откуда вызван метод :    true - из MainActivity
        //                                                  false - при нажатии на PuzzleActivity с учетом нехватки автомобилей

        if(DBHelper.getUnlockedBySection().size()>=4 && flagFromPuzzles) {
            BaseActivity.newInstance(getContext(),PuzzleActivity.class);activity.finish();}
        else kinderActivity.showScratchEggFragment(isCarsForPuzzle);
    }


}//ScratchEggFragment
