package com.jack.androidheap.fragment.performance.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by zhaiyh on 2017/4/6.
 */

public class Card {

    public RectF area;
    private Bitmap bitmap;
    private Paint paint = new Paint();

    public Card(RectF area) {
        this.area = area;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, area, paint);
    }
}
