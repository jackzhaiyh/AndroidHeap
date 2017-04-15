package com.jack.androidheap.fragment.performance.ui;

import com.jack.androidheap.R;
import com.jack.androidheap.activity.HomePageActivity;
import com.jack.androidheap.fragment.base.BaseFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

/**
 * Created by zhaiyh on 2017/4/6.
 */

public class UiPerfFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtn_OverDraw,mBtn_Xml,mBtn_ListView;
    private Context mContext = null;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getHostActivity();
        View view = inflater.inflate(R.layout.fm_ui_perf, container, false);
        mBtn_OverDraw = (Button)view.findViewById(R.id.btn_overdraw);
        mBtn_OverDraw.setOnClickListener(this);
        mBtn_Xml = (Button)view.findViewById(R.id.btn_xml);
        mBtn_Xml.setOnClickListener(this);
        mBtn_ListView = (Button)view.findViewById(R.id.btn_listview);
        mBtn_ListView.setOnClickListener(this);
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
        if(v.getId() == mBtn_OverDraw.getId()){
            Bundle mBundle = new Bundle();
            ((HomePageActivity) mContext).addSecondFragment(OverDrawFragment.class, mBundle, null);
        }else if(v.getId() == mBtn_Xml.getId()){
            Bundle mBundle = new Bundle();
            ((HomePageActivity) mContext).addSecondFragment(XmlShowFragment.class, mBundle, null);
        }else if(v.getId() == mBtn_ListView.getId()){
            Bundle mBundle = new Bundle();
            ((HomePageActivity) mContext).addSecondFragment(ListViewFragment.class, mBundle, null);
        }
    }
}
