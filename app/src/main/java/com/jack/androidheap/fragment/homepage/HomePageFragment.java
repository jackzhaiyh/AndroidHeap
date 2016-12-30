package com.jack.androidheap.fragment.homepage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.androidheap.R;
import com.jack.androidheap.fragment.PerformanceFragment;
import com.jack.androidheap.fragment.TestFragment;
import com.jack.androidheap.fragment.base.BaseFragment;
import com.jack.androidheap.config.PageIndexer;
import com.jack.androidheap.fragment.base.BaseFragmentPagerAdapter;

/**
 * Created by zhai.yuehui on 2016/12/30.
 */

public class HomePageFragment extends BaseFragment implements PageIndexer {
    private final static String TAG = "HomePageFragment";
    public final static int MSG_REFRESH_BANNER = 1;
    public final static String IS_SHOW_PUSH_DIALOG = "is_show_dialog";
    public final static String PUSH_DIALOG_TITLE = "dialog_title";
    public final static String PUSH_DIALOG_MSG = "dialog_message";
    private int mViewIndex = TAB_VIEW_00;
    private MyFragmentStatePagerAdapter mAdapter;// 存放fragment供pager使用的adapter。
    private BaseFragment[] mFragments;
    public BaseViewPager mPagerDetail;
    public ImageView mSearchBtn;
    public FrameLayout mMoreBtn;
    public TextView mMyMusicTab;
    public TextView mMusicHallTab;
    public TextView mFindTab;
    public ImageView mMyMusicNewFlag;
    public ImageView mFindNewFlag;
    public ImageView mMoreNewFlag;
    private FragmentManager mFragmentManager;

    private boolean mIsFirstIn = true;

    public static final int MY_MUSIC_NEW_FLAG = 0x0001;

    public static final int FIND_NEW_FLAG = 0x0002;

    public static final int MORE_NEW_FLAG = 0x0003;

    public interface OnNewFlagChangedLitener {

        public void onNewFlagShow(int newFlag);

        public void onNewFlagHide(int newFlag);
    }

    private OnNewFlagChangedLitener mOnNewFlagChangedLitener = new OnNewFlagChangedLitener() {

        @Override
        public void onNewFlagShow(int newFlag) {
            switch (newFlag) {
                case MY_MUSIC_NEW_FLAG:
                    mMyMusicNewFlag.setVisibility(View.VISIBLE);
                    break;
                case FIND_NEW_FLAG:
                    mFindNewFlag.setVisibility(View.VISIBLE);
                    break;
                case MORE_NEW_FLAG:
                    mMoreNewFlag.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onNewFlagHide(int newFlag) {
            switch (newFlag) {
                case MY_MUSIC_NEW_FLAG:
                    mMyMusicNewFlag.setVisibility(View.GONE);
                    break;
                case FIND_NEW_FLAG:
                    mFindNewFlag.setVisibility(View.GONE);
                    break;
                case MORE_NEW_FLAG:
                    mMoreNewFlag.setVisibility(View.GONE);
                    break;
            }
        }

    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getHostActivity().getLayoutInflater().inflate(R.layout.fragment_homepage, null);
        mPagerDetail = (BaseViewPager)view.findViewById(R.id.main_desk_fragment_pager);
        mSearchBtn = (ImageView)view.findViewById(R.id.main_desk_title_search_btn);
        mMoreBtn =  (FrameLayout)view.findViewById(R.id.main_desk_title_more_btn);
        mMyMusicTab = (TextView)view.findViewById(R.id.main_desk_title_tab_mymusic);
        mMusicHallTab = (TextView)view.findViewById(R.id.main_desk_title_tab_musichall);
        mFindTab = (TextView)view.findViewById(R.id.main_desk_title_tab_find);
        mMyMusicNewFlag = (ImageView)view.findViewById(R.id.main_desk_title_tab_mymusic_new_flag);
        mFindNewFlag = (ImageView)view.findViewById(R.id.main_desk_title_tab_find_new_flag);
        mMoreNewFlag = (ImageView)view.findViewById(R.id.main_desk_title_more_new_flag);

        initView();
        return view;
    }

    private void initView() {
        mFragments = new BaseFragment[3];
        PerformanceFragment fragment1 = new PerformanceFragment();
        mFragments[0] = fragment1;
        fragment1.setRetainInstance(true);


        TestFragment fragment2 = new TestFragment();
        fragment2.setRetainInstance(true);
        mFragments[1] = fragment2;


        TestFragment fragment3 = new TestFragment();
        fragment3.setRetainInstance(true);
        mFragments[2] = fragment3;


        mAdapter = new MyFragmentStatePagerAdapter(mFragmentManager);
        new setAdapterTask().execute();
        mPagerDetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {// 给pager的滑动添加属性

            @Override
            public void onPageSelected(int position) {// 当一个pager被选中的时候
                if (position >= 0 && position < mAdapter.getCount() && position != mViewIndex) {
                    setSelectedTab(position);
                }
                BaseFragment f = mFragments[position];
                OnShowListener onShowListener = f.getOnShowListener();
                if (onShowListener != null) {
                    if (onShowListener.isReShow()) {
                        onShowListener.onShowFromNet();
                    } else if (!onShowListener.isOnShow()) {
                        onShowListener.onShowFromLocal();
                    }
                }
                for (int i = 0; i < mFragments.length; i++) {
                    if (i != position) {
                        OnShowListener onShowListener1 = mFragments[i].getOnShowListener();
                        if (onShowListener1 != null) {
                            onShowListener1.onFragmentUnShow();
                        }

                    }
                }
                if (position == 0) {
                    mMyMusicNewFlag.setVisibility(View.GONE);
                } else if (position == 2) {
                    mFindNewFlag.setVisibility(View.GONE);
                }
//                ListViewWithViewPager v = null;
//                QmfBaseViewPager vp = null;
//                if (f instanceof MusicHallsFragment) {
//                    v = ((MusicHallsFragment) f).getInterceptView();
//                    vp = ((MusicHallsFragment) f).getViewPager();
//                }
//                mPagerDetail.setInterceptView(v);
//                mPagerDetail.setViewPager(vp);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {// 滑动pager的时候触发

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mMyMusicTab.setOnClickListener(mTabClickListener);
        mMusicHallTab.setOnClickListener(mTabClickListener);
        mFindTab.setOnClickListener(mTabClickListener);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle arg = new Bundle();
//                if (getHostActivity() == null) {
//                    return;
//                }
//                ((MusicMainWithMiniBarActivity) getHostActivity()).addSecondFragment(OnlineSearchFragment.class, arg,
//                        null);
            }
        });
        mMoreBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    private View.OnClickListener mTabClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = 0;
            if (v == mMyMusicTab) {
                position = 0;
            } else if (v == mMusicHallTab) {
                position = 1;
            } else if (v == mFindTab) {
                position = 2;
            }
            setSelectedTab(position);
            mPagerDetail.setCurrentItem(position);
        }
    };

    public void gotoSelectedTab(int position) {
        setSelectedTab(position);
        mPagerDetail.setCurrentItem(position);
    }

    private void setSelectedTab(int position) {
        mViewIndex = position;
        mMyMusicTab.setTextColor(0x66FFFFFF);
        mMusicHallTab.setTextColor(0x66FFFFFF);
        mFindTab.setTextColor(0x66FFFFFF);
        switch (mViewIndex) {
            case 0:
                mMyMusicTab.setTextColor(0xFFFFFFFF);
                break;
            case 1:
                mMusicHallTab.setTextColor(0xFFFFFFFF);;
                break;
            case 2:
                mFindTab.setTextColor(0xFFFFFFFF);
                break;
        }
    }

    @Override
    protected void resume() {
        if (mIsFirstIn) {
            mIsFirstIn = false;
            return;
        }

        OnShowListener onShowListener = mFragments[mViewIndex].getOnShowListener();
        if (onShowListener != null) {
            if (onShowListener.isReShow()) {
                onShowListener.onShowFromNet();
            } else if (!onShowListener.isOnShow()) {
                onShowListener.onShowFromLocal();
            }
        }
        for (int i = 0; i < mFragments.length; i++) {
            if (i != mViewIndex) {
                OnShowListener onShowListener1 = mFragments[i].getOnShowListener();
                if (onShowListener1 != null) {
                    onShowListener1.onFragmentUnShow();
                }

            }
        }

        // 调用包含的fragment的对应生命周期
        for (Fragment f : mFragments) {
            if (f.isAdded()) {
                f.onResume();
            }
        }
    }

    public BaseFragment getViewPage(int index) {
        if (mFragments != null && index >= 0 && index < mFragments.length) {
            return mFragments[index];
        }
        return null;
    }

    @Override
    protected void stop() {
        for (Fragment f : mFragments) {
            if (f.isAdded()) {
                f.onStop();
            }
        }

        mIsFirstIn = true;
    }

    @Override
    protected void pause() {
        for (BaseFragment f : mFragments) {
            OnShowListener onShowListener = f.getOnShowListener();
            if (onShowListener != null) {
                onShowListener.onFragmentUnShow();
            }
            if (f.isAdded()) {
                f.onPause();
            }
        }
    }

    @Override
    protected void start() {
        for (Fragment f : mFragments) {
            if (f.isAdded()) {
                f.onStart();
            }
        }
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
    public int getFromID() {
        return 0;
    }

    @Override
    protected void initData(Bundle data) {
        mFragmentManager = getChildFragmentManager();
        int gotoAppIndex = data.getInt(APP_INDEX_KEY, TAB_VIEW_00);
        // 退出时view索引
        // int savedAppIndex = MusicPreferences.getInstance().getAppIndex() -
        // APP_MAIN_1;
        if (gotoAppIndex != TAB_VIEW_00 && (gotoAppIndex - APP_MAIN_1) >= TAB_VIEW_00
                && (gotoAppIndex - APP_MAIN_1) < TAB_VIEW_02 + 1) {
            mViewIndex = gotoAppIndex - APP_MAIN_1;
        }
        // else if (savedAppIndex >= TAB_VIEW_00 && savedAppIndex < TAB_VIEW_02
        // + 1) {
        // mViewIndex = savedAppIndex;
        // }不在记录退出时所在的tab，每次回来都回到我的音乐，这个之后可能修改先注释掉，by lugh
        else {
            mViewIndex = TAB_VIEW_00;
        }
    }

    /**
     * pager中要放的adapter，主要实现getItem和getCount和构造函数
     *
     * @author lughzhang
     */
    public class MyFragmentStatePagerAdapter extends BaseFragmentPagerAdapter {
        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    /**
     * @author lughzhang
     */
    private class setAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPagerDetail.setAdapter(mAdapter);
            mPagerDetail.setOffscreenPageLimit(mAdapter.getCount() + 1);
            mPagerDetail.postDelayed(new Runnable() {

                @Override
                public void run() {
                    gotoSelectedFragment();
                }
            }, 300);
        }
    }

    private void gotoSelectedFragment() {
        if (mFragments != null && mFragments.length > mViewIndex) {
            setSelectedTab(mViewIndex);
            mPagerDetail.setCurrentItem(mViewIndex);
        }
        if (mViewIndex == 0 && mFragments != null && mFragments.length > 0) {
            OnShowListener onShowListener = mFragments[mViewIndex].getOnShowListener();
            if (onShowListener != null) {
                if (onShowListener.isReShow()) {
                    onShowListener.onShowFromNet();
                } else if (!onShowListener.isOnShow()) {
                    onShowListener.onShowFromLocal();
                }
            }
            for (int i = 0; i < mFragments.length; i++) {
                if (i != mViewIndex) {
                    if (mFragments[i] != null) {
                        OnShowListener onShowListener1 = mFragments[i].getOnShowListener();
                        if (onShowListener1 != null) {
                            onShowListener1.onFragmentUnShow();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void loginOk() {
        if (mFragments != null && isAdded()) {
            for (BaseFragment f : mFragments) {
                if (f != null) {
                    f.loginOk();
                }
            }
        }
    }

    @Override
    public void logoutOk() {
        if (mFragments != null && isAdded()) {
            for (BaseFragment f : mFragments) {
                if (f != null) {
                    f.logoutOk();
                }
            }
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                gotoSelectedTab(0);
            }
        });

    }
}
