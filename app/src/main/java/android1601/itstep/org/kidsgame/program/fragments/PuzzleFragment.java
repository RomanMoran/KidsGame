package android1601.itstep.org.kidsgame.program.fragments;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.activity.PuzzleActivity;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper;
import android1601.itstep.org.kidsgame.program.enums.POSITION_TYPE;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnTouch;

/**
 * Created by roman on 14.03.2017.
 */

public class PuzzleFragment extends BaseFragment {

    public static final String TAG = PuzzleFragment.class.getName();
    @BindViews({
            R.id.imgCar1,
            R.id.imgCar2,
            R.id.imgCar3,
            R.id.imgCar4})
    List<ImageView> imgLeftCarsList;
    @BindViews({
            R.id.leftEmptyImage1,
            R.id.leftEmptyImage2,
            R.id.leftEmptyImage3,
            R.id.leftEmptyImage4})
    List<View> imgEmptyLeftCarsList;
    @BindViews({
            R.id.imgEmpty1,
            R.id.imgEmpty2,
            R.id.imgEmpty3,
            R.id.imgEmpty4})
    List<ImageView> imgEmptyCarsList;


    private boolean touchFlag = false;
    private ViewGroup.LayoutParams lastDragImageParams;

    private int displayWidth = 0;
    private int displayHeight = 0;
    private int imageWidth = 0;
    private int imageHeight = 0;

    private int offsetX = 0;
    private int offsetY = 0;
    private ImageView targetEmptyImage;
    private ImageView draggableImage;
    ArrayList<Gifts> mGiftsList;

    private int countAir = 0;
    private int countGround = 0;
    private int countWater = 0;

    private MediaPlayer mMediaPlayerSound;
    private MediaPlayer mMediaPlayerSoundWin;

    private int countImages = 0;

    @BindView(R.id.puzzleFragment)
    RelativeLayout mRelativeLayout;


    private static final String BUNDLE_TOYS = "BUNDLE_KEY";

    public static PuzzleFragment newInstance(List<Gifts> toyList) {
        PuzzleFragment fragment = new PuzzleFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(BUNDLE_TOYS, (ArrayList<? extends Parcelable>) toyList);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getLayoutResId() {
        for (Gifts gifts : mGiftsList) {
            switch (gifts.getPositionEnum()) {
                case WATER:
                    countWater++;
                    break;
                case AIR:
                    countAir++;
                    break;
                case GROUND:
                case UNKNOWN:
                default:
                    countGround++;
                    break;
            }
        }

        if (countAir == 1 && countWater == 1) {
            requery();
            return R.layout.fragment_puzzle_water_one_air;
        }
        if (countAir == 2 && countWater == 1) {
            requery();
            return R.layout.fragment_puzzle_water_two_air;
        }
        if (countWater == 1 && countGround == 3) {
            requery();
            return R.layout.fragment_puzzle_water_only;
        }

        if (countAir == 2 && countGround == 2) {
            requery();
            return R.layout.fragment_puzzle_two_air_two_ground;
        }
        if (countAir == 1 && countGround == 3) {
            requery();
            return R.layout.fragment_puzzle_one_air_three_ground;
        }


        return R.layout.fragment_puzzle;
    }

    private void requery() {
        ArrayList<Gifts> containerAirs = new ArrayList<>();
        ArrayList<Gifts> containerWaters = new ArrayList<>();
        ArrayList<Gifts> containerCars = new ArrayList<>();
        for (int i = 0; i < mGiftsList.size(); i++) {
            if (mGiftsList.get(i).getPositionEnum() == POSITION_TYPE.AIR) {
                containerAirs.add(mGiftsList.get(i));
                continue;
            }
            if (mGiftsList.get(i).getPositionEnum() == POSITION_TYPE.WATER) {
                containerWaters.add(mGiftsList.get(i));
                continue;
            }
            containerCars.add(mGiftsList.get(i));
        }
        for (int i = 0; i < containerCars.size(); i++) {
            containerWaters.add(containerCars.get(i));
        }
        for (int i = 0; i < containerAirs.size(); i++) {
            containerWaters.add(containerAirs.get(i));
        }
        mGiftsList = containerWaters;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mGiftsList = bundle.getParcelableArrayList(BUNDLE_TOYS);
        }

        Point displaySize = Utility.getDisplaySize();
        displayWidth = displaySize.x;
        displayHeight = displaySize.y;
        // Определение размера картинки(ImageView) на экране
        imageWidth = (int) (displayWidth / 3.0f);       // Т.е. занимает 1/3 экрана по ширине
        imageHeight = (int) (displayHeight / 4.0f);     // и 1/4 по высоте
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initImages();
    }


    private void initImages() {
        // Инициализируем передвигаемые объекты (машинки)
        for (int i = 0; i < imgLeftCarsList.size(); i++) {
            ImageView imageView = imgLeftCarsList.get(i);
            if (mGiftsList.size() > i) {
                // Получаем значение int нашего изображение
                int resourceId = mGiftsList.get(i).getImageResId();

                // Динамическая загрузка необходимых
                // изображений в пустые imgCar's

                Glide.with(imageView.getContext())
                        .load(resourceId)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .skipMemoryCache(true)
                        //.animate(R.anim.combo)
                        .into(imageView);
            }
            updateSize(imageView, imageWidth, imageHeight);
        }

        for (int i = 0; i < imgEmptyCarsList.size(); i++) {
            ImageView imageView = imgEmptyCarsList.get(i);
            if (mGiftsList.size() > i) {
                // Получаем значение int нашего изображения
                int resourceId = mGiftsList.get(i).getSilhouetteResId();


                // Динамическая загрузка необходимых
                // изображений в пустые imgCar's
                Glide.with(imageView.getContext())
                        .load(resourceId)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .skipMemoryCache(true)
                        //.animate(R.anim.change)
                        .into(imageView);
            }
            updateSize(imageView, imageWidth, imageHeight);
        }

        for (int i = 0; i < imgEmptyLeftCarsList.size(); i++) {
            View imageView = imgEmptyLeftCarsList.get(i);
            updateSize(imageView, imageWidth, imageHeight);
        }
    }

    private void updateSize(View view, int width, int height) {
        view.getLayoutParams().height = height;
        view.getLayoutParams().width = width;
        view.requestLayout();
    }

    private void whatImage(View view, MotionEvent event) {
        for (int i = 0; i < imgLeftCarsList.size(); i++) {
            ImageView imageView = imgLeftCarsList.get(i);
            if (imageView.equals(view)) {
                draggableImage = imgLeftCarsList.get(i);
                targetEmptyImage = imgEmptyCarsList.get(i);
                new LoadResIdSound().execute();
                // перемещаемое изображение на передний план
                draggableImage.bringToFront();
                // исходные параметры расположения перемещаемого изображения
                // в случае если оно не было перемещно в необходимые координаты изображению задаем эти параметры
                // тем самым возвращая его в исходное положение
                lastDragImageParams = draggableImage.getLayoutParams();
                touchFlag = true;
                // offsets - это координаты прикосновения (ширины(Х) и высоты(Y)) в плоскости нашего изображения
                // запоминаем для дальнейшего расчета left and top margins перемещаемого изображения
                offsetX = (int) event.getX();
                offsetY = (int) event.getY();
                break;
            }
        }

    }

    @OnTouch(R.id.puzzleFragment)
    boolean rootTouched(View view, MotionEvent event) {
        if (touchFlag) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    //расчет отступов перемещаемого изображения согласно координат прикосновения на перемещаемое изображение
                    int x = (int) (event.getX() - offsetX);
                    int y = (int) (event.getY() - offsetY);
                    // Границы чтобы изображение не выходило за пределы экрана
                    if (x > displayWidth - imageWidth) x = displayWidth - imageWidth;
                    else if (x < 0) x = 0;
                    if (y > displayHeight - imageHeight) y = displayHeight - imageHeight;
                    else if (y < 0) y = 0;
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(lastDragImageParams.width, lastDragImageParams.height);
                    lp.setMargins(x, y, 0, 0);
                    draggableImage.setLayoutParams(lp);

                    break;
                case MotionEvent.ACTION_UP:
                    touchFlag = false;
                    //определяем координаті центра перемещаемого изображения
                    lp = (RelativeLayout.LayoutParams) draggableImage.getLayoutParams();
                    int centerX = lp.leftMargin + imageWidth / 2;
                    int centerY = lp.topMargin + imageHeight / 2;
                    // сравниваем попал ли центр перемещаемого изображения в "целевой прямоуголник"
                    // отнимаем 1/3 ширины и высоты от краев тем самым уменьшая область прямоугольника
                    // тем самым образуя "целевой прямоугольник"
                    if (targetEmptyImage != null &&
                            centerX > targetEmptyImage.getLeft() + imageWidth / 3 &&
                            centerX < targetEmptyImage.getRight() - imageWidth / 3 &&
                            centerY > targetEmptyImage.getTop() + imageHeight / 3 &&
                            centerY < targetEmptyImage.getBottom() - imageHeight / 3) {
                        targetEmptyImage.setImageDrawable(draggableImage.getDrawable());
                        draggableImage.setVisibility(View.INVISIBLE);
                        countImages++;

                        if (mMediaPlayerSound != null && !mMediaPlayerSound.isPlaying())
                            mMediaPlayerSound.start();
                    } else {
                        //если не попали возвращаем в исходное положение
                        draggableImage.setLayoutParams(lastDragImageParams);
                    }
                    draggableImage = null;
                    targetEmptyImage = null;
                    break;
                default:
                    break;

            }
        }
        return true;
    }

    @OnTouch({
            R.id.imgCar1,
            R.id.imgCar2,
            R.id.imgCar3,
            R.id.imgCar4})
    boolean draggableImageTouched(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                whatImage(view, event);
                break;
        }
        return false;
    }

    private void showWinTextAndSound() {
        if (!mMediaPlayerSound.isPlaying() && countImages >= 4) {
            mMediaPlayerSoundWin.start();
            TextView tv = new TextView(getContext());
            tv.setText(getResources().getString(R.string.success));
            tv.setTextSize(displayWidth / 23);
            tv.setTextColor(getResources().getColor(R.color.dorge_blue));
            final RelativeLayout.LayoutParams tvParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            tv.setGravity(Gravity.CENTER);
            mRelativeLayout.addView(tv, tvParams);
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.change_text);

            tv.setTypeface(Utility.getTypeface());
            tv.startAnimation(anim);
        }
    }

    protected void onInitMediaPlayer() {
        if (mMediaPlayerSoundWin != null)
            mMediaPlayerSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    showWinTextAndSound();
                    mMediaPlayerSoundWin.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            PuzzleActivity puzzleActivity = (PuzzleActivity) getActivity();
                            puzzleActivity.showPuzzleFragment(DBHelper.getRandomFourItems());

                        }
                    });
                }
            });

    }

    private class LoadResIdSound extends AsyncTask<Void, Void, Void> {
        int soundRawId;
        int soundRawWin;

        @Override
        protected Void doInBackground(Void... params) {
            /*soundRawId = MainActivity.muteSound ? R.raw.magic_sound : R.raw.mute_sound;
            soundRawWin = MainActivity.muteSound ? R.raw.sound_win : R.raw.mute_sound;*/
            soundRawId = R.raw.magic_sound;
            soundRawWin = R.raw.sound_win;
            if (soundRawId != 0 && soundRawWin != 0) {
                if (getContext() != null)
                    mMediaPlayerSound = MediaPlayer.create(getContext(), soundRawId);
                mMediaPlayerSoundWin = MediaPlayer.create(getContext(), soundRawWin);
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
        if (mMediaPlayerSoundWin != null) {
            mMediaPlayerSoundWin.release();
            mMediaPlayerSoundWin = null;
        }
        if (mMediaPlayerSound != null) {
            mMediaPlayerSound.release();
            mMediaPlayerSound = null;
        }
    }

}
