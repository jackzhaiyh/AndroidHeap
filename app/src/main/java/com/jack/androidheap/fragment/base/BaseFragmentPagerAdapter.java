package com.jack.androidheap.fragment.base;

import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.jack.androidheap.utils.JLog;

/**
 * Created by zhai.yuehui on 2016/12/30.
 */

public abstract class BaseFragmentPagerAdapter extends PagerAdapter {
    private static final String TAG = "BaseFragmentPagerAdapter";
    private static final boolean DEBUG = false;
    private String mTag;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        this(fm, "time is:" + SystemClock.currentThreadTimeMillis());
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, String Tag) {
        mFragmentManager = fm;
        mTag = Tag;
    }

    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(View container) {
    }

    @Override
    public Object instantiateItem(View container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), position);
        JLog.d(TAG, "instantiateItem name is:" + name);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        JLog.d(TAG, "instantiateItem fragment is:" + fragment);
        if (fragment != null) {
            if (DEBUG)
                JLog.d(TAG, "Attaching item #" + position + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            if (fragment == null) {
                return null;
            }
            JLog.d(TAG, "instantiateItem getItem fragment is:" + fragment);
            if (DEBUG)
                JLog.d(TAG, "Adding item #" + position + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), position));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG)
            JLog.d(TAG, "Detaching item #" + position + ": f=" + object + " v=" + ((Fragment) object).getView());
        mCurTransaction.detach((Fragment) object);
    }

    @Override
    public void setPrimaryItem(View container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(View container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    private String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index + ":" + mTag;
    }

    public void clear() {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        try {
            for (int i = 0; i < getCount(); i++) {
                Fragment f = (Fragment) getItem(i);
                mCurTransaction.remove(f);
            }
            mCurTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
