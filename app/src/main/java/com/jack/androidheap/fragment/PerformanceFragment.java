package com.jack.androidheap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.jack.androidheap.R;
import com.jack.androidheap.fragment.base.BaseFragment;

/**
 * Created by zhai.yuehui on 2016/12/30.
 */

public class PerformanceFragment extends BaseFragment implements View.OnClickListener{

    private Button mBtn_UiPerf,mBtn_MenPerf = null;
    private Context mContext = null;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getHostActivity();
        View view = inflater.inflate(R.layout.fm_performance, container, false);
        mBtn_UiPerf = (Button)view.findViewById(R.id.btn_ui_perf);
        mBtn_UiPerf.setOnClickListener(this);

        mBtn_MenPerf = (Button)view.findViewById(R.id.btn_men_perf);
        mBtn_MenPerf.setOnClickListener(this);
        return view;
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

    @Override
    public void onClick(View v) {
        //TODO
    }
}
