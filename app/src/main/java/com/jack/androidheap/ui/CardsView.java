package com.jack.androidheap.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.jack.androidheap.fragment.performance.ui.Card;
import com.jack.androidheap.utils.JLog;

import java.util.ArrayList;

/**
 * Created by zhaiyh on 2017/4/6.
 */

public class CardsView extends View {

    private ArrayList<Card> cardsList = new ArrayList<Card>(5);
    private boolean enableOverdrawOpt = true;

    public CardsView(Context context) {
        this(context, null, 0);

    }

    public CardsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CardsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addCards(Card card) {
        cardsList.add(card);
    }

    public void enableOverdrawOpt(boolean enableOrNot) {
        this.enableOverdrawOpt = enableOrNot;
        invalidate();
    }

    public void setCardsNeedYOffset(boolean needYOffset) {
        int yOffset = 50;
        for (int i = 0; i < cardsList.size(); i++) {
            Card cd = cardsList.get(i);
            float cardHeight = cd.area.height();
            if (needYOffset) {
                cd.area.top = i * yOffset;
            } else {
                cd.area.top = yOffset;
            }
            cd.area.bottom = cd.area.top + cardHeight;
        }

        invalidate();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmaps(canvas);
    }

    protected void drawBitmaps(Canvas canvas) {
        if (cardsList == null || canvas == null)
            return;
        Rect clip = canvas.getClipBounds();
        JLog.d("draw", String.format("clip bounds %d %d %d %d", clip.left, clip.top, clip.right, clip.bottom));
        if (enableOverdrawOpt) {
            drawCardsWithClipRecursive(canvas, cardsList.size() - 1);
        } else {
            drawCardsRecursive(canvas, cardsList.size() - 1);
        }
    }

    protected void drawCardsWithClipRecursive(Canvas canvas, int index) {
        if (canvas == null || index < 0 || index >= cardsList.size())
            return;
        Card card = cardsList.get(index);
        if (card != null && !canvas.quickReject(card.area, Canvas.EdgeType.BW)) {
            //1. at first,draw cards under current index .
            int saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            //1.1 before draw under cards, we should set canvas's clip region different from current card area.
            if (canvas.clipRect(card.area, Region.Op.DIFFERENCE)) {
                drawCardsWithClipRecursive(canvas, index - 1);
            }
            canvas.restoreToCount(saveCount);

            //2.when card under the current one is drawn��we draw current cards
            saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            //2.1 clip card area first
            if (canvas.clipRect(card.area)) {
                JLog.d("draw", "overdraw opt: draw cards index: " + index);
                Rect clip = canvas.getClipBounds();
                JLog.d("draw", String.format("current clip bounds %d %d %d %d", clip.left, clip.top, clip.right, clip.bottom));
                card.draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        } else {
            //0.when current card is null or quickReject by canvas's clip region, continue to draw next card .
            drawCardsWithClipRecursive(canvas, index - 1);
        }
    }

    protected void drawCardsRecursive(Canvas canvas, int index) {
        if (canvas == null || index < 0 || index >= cardsList.size())
            return;
        Card card = cardsList.get(index);
        if (card != null) {
            drawCardsRecursive(canvas, index - 1);
            JLog.d("draw", "draw cards index: " + index);
            card.draw(canvas);

        } else {
            drawCardsRecursive(canvas, index - 1);
        }
    }
}
