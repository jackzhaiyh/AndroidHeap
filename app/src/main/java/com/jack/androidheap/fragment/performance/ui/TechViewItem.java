package com.jack.androidheap.fragment.performance.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zhaiyh on 2017/4/6.
 */

public class TechViewItem extends LinearLayout {
    private Context mContext;

    public TechViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    public TechViewItem(Context context, AttributeSet attrs) {
        super(context, null);
        initView(context);
    }
    public TechViewItem(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {

        mContext = context;

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item, this, true);

    }

}
