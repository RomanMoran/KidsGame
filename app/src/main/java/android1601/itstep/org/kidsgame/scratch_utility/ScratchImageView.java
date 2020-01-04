package android1601.itstep.org.kidsgame.scratch_utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android1601.itstep.org.kidsgame.R;


/**
 * Created by Harish on 25/03/16.
 */
public class ScratchImageView extends AppCompatImageView {


    public static final float STROKE_WIDTH = 12f;
    private static final float TOUCH_TOLERANCE = 4;

    public MediaPlayer mMediaPlayer;
    private float mX, mY;
    /**
     * Bitmap holding the scratch region.
     */
    private Bitmap mScratchBitmap;
    /**
     * Drawable canvas area through which the scratchable area is drawn.
     */
    private Canvas mCanvas;
    /**
     * Path holding the erasing path done by the user.
     */
    private Path mErasePath;
    /**
     * Path to indicate where the user have touched.
     */
    private Path mTouchPath;
    /**
     * Paint properties for drawing the scratch area.
     */
    private Paint mBitmapPaint;
    /**
     * Paint properties for erasing the scratch region.
     */
    private Paint mErasePaint;
    /**
     * Gradient paint properties that lies as a background for scratch region.
     */
    private Paint mGradientBgPaint;
    /**
     * Sample Drawable mBitmap having the scratch pattern.
     */
    private BitmapDrawable mDrawable;
    /**
     * Listener object callback reference to send back the callback when the image has been revealed.
     */
    private IRevealListener mRevealListener;
    /**
     * Reveal percent value.
     */
    private float mRevealPercent;
    /**
     * Thread Count
     */
    private int mThreadCount = 0;

    public ScratchImageView(Context context) {
        super(context);
        init();

    }


    public ScratchImageView(Context context, AttributeSet set) {
        super(context, set);
        init();
    }

    public ScratchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Set the strokes width based on the parameter multiplier.
     *
     * @param multiplier can be 1,2,3 and so on to set the stroke width of the paint.
     */
    public void setStrokeWidth(int multiplier) {
        mErasePaint.setStrokeWidth(multiplier * STROKE_WIDTH);
    }

    /**
     * Initialises the paint drawing elements.
     */
    private void init() {


        mTouchPath = new Path();

        mErasePaint = new Paint();
        mErasePaint.setAntiAlias(true);
        mErasePaint.setDither(true);
        mErasePaint.setColor(0xFFFF0000);
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeJoin(Paint.Join.BEVEL);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);
        setStrokeWidth(6);

        mGradientBgPaint = new Paint();

        mErasePath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        Bitmap scratchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kinder_surprise);
        mDrawable = new BitmapDrawable(getResources(), scratchBitmap);

        setEraserMode();

        mMediaPlayer = MediaPlayer.create(getContext(),R.raw.clear);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScratchBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mScratchBitmap);

        Rect rect = new Rect(0, 0, mScratchBitmap.getWidth(), mScratchBitmap.getHeight());
        //setBound - нужны чтобы отображать границы нашего стираемого объекта если убрать то его не будет
        mDrawable.setBounds(rect);
        mDrawable.draw(mCanvas);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawBitmap(mScratchBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mErasePath, mErasePaint);


    }

    private void touch_start(float x, float y) {
        mErasePath.reset();
        mErasePath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    /**
     * clears the scratch area to reveal the hidden image.
     */
    public void clear() {

        int[] bounds = getImageBounds();
        int left = bounds[0];
        int top = bounds[1];
        int right = bounds[2];
        int bottom = bounds[3];

        int width = right - left;
        int height = bottom - top;
        int centerX = left + width / 2;
        int centerY = top + height / 2;

        left = centerX - width / 2;
        top = centerY - height / 2;
        right = left + width;
        bottom = top + height;

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));

        //mCanvas.drawRect(left, top, right, bottom, paint);
        RectF rectF = new RectF(left, top, right, bottom);
        mCanvas.drawOval(rectF, paint);
        checkRevealed();
        invalidate();
    }

    private void touch_move(float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mErasePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            drawPath();
        }


        mTouchPath.reset();
        mTouchPath.addCircle(mX, mY, 30, Path.Direction.CW);

    }

    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void drawPath() {
        mErasePath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mErasePath, mErasePaint);
        // kill this so we don't double draw
        mTouchPath.reset();
        mErasePath.reset();
        mErasePath.moveTo(mX, mY);

        checkRevealed();
    }

    public void reveal() {
        clear();
    }

    private void touch_up() {

        drawPath();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying())
                    mMediaPlayer.start();

                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mMediaPlayer != null && mMediaPlayer.isPlaying())
                    mMediaPlayer.pause();
                touch_up();

                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public int getColor() {
        return mErasePaint.getColor();
    }

    public Paint getErasePaint() {
        return mErasePaint;
    }

    public void setEraserMode() {

        getErasePaint().setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));

    }

    public void setRevealListener(IRevealListener listener) {
        this.mRevealListener = listener;
    }

    public boolean isRevealed() {
        return mRevealPercent == 1;
    }

    private void checkRevealed() {

        if (!isRevealed() && mRevealListener != null) {

            int[] bounds = getImageBounds();
            int left = bounds[0];
            int top = bounds[1];
            int width = bounds[2] - left;
            int height = bounds[3] - top;

            // Do not create multiple calls to compare.
            if (mThreadCount > 1) {
                Log.d("Captcha", "Count greater than 1");
                return;
            }

            mThreadCount++;

            new AsyncTask<Integer, Void, Float>() {

                @Override
                protected Float doInBackground(Integer... params) {

                    try {
                        int left = params[0];
                        int top = params[1];
                        int width = params[2];
                        int height = params[3];

                        Bitmap croppedBitmap = Bitmap.createBitmap(mScratchBitmap, left, top, width, height);

                        return BitmapUtils.getTransparentPixelPercent(croppedBitmap);
                    } finally {
                        mThreadCount--;
                    }
                }

                public void onPostExecute(Float percentRevealed) {

                    // check if not revealed before.
                    if (!isRevealed()) {

                        float oldValue = mRevealPercent;
                        mRevealPercent = percentRevealed;

                        if (oldValue != percentRevealed) {
                            mRevealListener.onRevealPercentChangedListener(ScratchImageView.this, percentRevealed);
                        }

                        // if now revealed.
                        if (isRevealed()) {
                            mRevealListener.onRevealed(ScratchImageView.this);
                        }
                    }
                }
            }.execute(left, top, width, height);

        }
    }

    public int[] getImageBounds() {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int vwidth = getWidth() - paddingLeft - paddingRight;
        int vheight = getHeight() - paddingBottom - paddingTop;

        int centerX = vwidth / 2;
        int centerY = vheight / 2;


        Drawable drawable = getDrawable();
        if (drawable==null)
            drawable = new ColorDrawable(Color.TRANSPARENT);
        Rect bounds = drawable.getBounds();

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        if (width <= 0) {
            width = bounds.right - bounds.left;
        }

        if (height <= 0) {
            height = bounds.bottom - bounds.top;
        }

        int left;
        int top;

        if (height > vheight) {
            height = vheight;
        }

        if (width > vwidth) {
            width = vwidth;
        }


        ScaleType scaleType = getScaleType();

        switch (scaleType) {
            case FIT_START:
                left = paddingLeft;
                top = centerY - height / 2;
                break;
            case FIT_END:
                left = vwidth - paddingRight - width;
                top = centerY - height / 2;
                break;
            case CENTER:
                left = centerX - width / 2;
                top = centerY - height / 2;
                break;
            default:
                left = paddingLeft;
                top = paddingTop;
                width = vwidth;
                height = vheight;
                break;

        }

        return new int[]{left, top, left + width, top + height};
    }

    public interface IRevealListener {
        void onRevealed(ScratchImageView iv);

        void onRevealPercentChangedListener(ScratchImageView siv, float percent);
    }


}
