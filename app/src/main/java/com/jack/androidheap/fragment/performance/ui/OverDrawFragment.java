package com.jack.androidheap.fragment.performance.ui;

import com.jack.androidheap.R;
import com.jack.androidheap.fragment.base.BaseFragment;
import com.jack.androidheap.ui.CardsView;
import com.jack.androidheap.utils.JLog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

/**
 * Created by zhaiyh on 2017/4/6.
 */

public class OverDrawFragment extends BaseFragment {
    private int cardResId[] = {R.mipmap.test4, R.mipmap.test3,
            R.mipmap.test2, R.mipmap.test1};
    private CardsView cardsView = null;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_overdraw, container, false);
        cardsView = (CardsView) view.findViewById(R.id.cardview);
        cardsView.enableOverdrawOpt(true);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int cardWidth = width / 2;
        int cardHeight = height / 2;
        int yOffset = 40;
        int xOffset = 40;
        for (int i = 0; i < cardResId.length; i++) {
            Card cd = new Card(new RectF(xOffset, yOffset, xOffset + cardWidth, yOffset + cardHeight));
            Bitmap bitmap = loadImageResource(cardResId[i], cardWidth, cardHeight);
            cd.setBitmap(bitmap);
            cardsView.addCards(cd);
            xOffset += cardWidth / 3;
        }
        Button overdraw = (Button) view.findViewById(R.id.btn_overdraw);
        overdraw.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            cardsView.enableOverdrawOpt(false);
                                        }
                                    }
        );
        Button perfectdraw = (Button) view.findViewById(R.id.btn_perfectdraw);
        perfectdraw.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View v) {
                                               cardsView.enableOverdrawOpt(true);
                                           }
                                       }
        );
        return view;
    }

    public Bitmap loadImageResource(int imageResId, int cardWidth, int cardHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inScreenDensity = DisplayMetrics.DENSITY_DEFAULT;
            BitmapFactory.decodeResource(getResources(), imageResId, options);

            // ����decode
            options.inJustDecodeBounds = false;
            options.inSampleSize = findBestSampleSize(options.outWidth, options.outHeight, cardWidth, cardHeight);
            bitmap = BitmapFactory.decodeResource(getResources(), imageResId, options);
        } catch (OutOfMemoryError exception) {

            JLog.d("img", "loadImageResource : out of memory resid: " + imageResId);
        }
        return bitmap;
    }

    /**
     * Returns the largest power-of-two divisor for use in downscaling a bitmap
     * that will not result in the scaling past the desired dimensions.
     *
     * @param actualWidth   Actual width of the bitmap
     * @param actualHeight  Actual height of the bitmap
     * @param desiredWidth  Desired width of the bitmap
     * @param desiredHeight Desired height of the bitmap
     */
    public static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    @Override
    protected void resume() {

    }

    @Override
    protected void stop() {

    }

    @Override
    protected void pause() {

    }

    @Override
    protected void start() {

    }

    @Override
    public void onEnterAnimationEnd(Animation animation) {

    }

    @Override
    public void clearView() {

    }

    @Override
    public void clear() {

    }

    @Override
    protected void initData(Bundle data) {

    }

    @Override
    public int getFromID() {
        return 0;
    }

    @Override
    public void loginOk() {

    }

    @Override
    public void logoutOk() {

    }

}
