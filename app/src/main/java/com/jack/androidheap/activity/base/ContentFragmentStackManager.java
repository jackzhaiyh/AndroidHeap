package com.jack.androidheap.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jack.androidheap.fragment.base.BaseFragment;
import com.jack.androidheap.utils.JLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhai.yuehui on 2016/12/15.
 */

public class ContentFragmentStackManager implements IContentFragmentStackManager {

    private static final String TAG = "FlipperContentFragmentStack44";
    public static final int STACK_SIZE = 10;
    private StackLayout mFlipperContent;

    private ArrayList<BaseFragment> mFragmentStack;
    private int mSize;
    private int mMax;
    private FragmentManager mManager;
    private BaseFragment mTopFragment;

    private Activity mActivity;
    private ClearAsyncTask mClearAsyncTask;

    private int mContentId;

    private String mContentName;

    private boolean isPushOrPop = false;

    private final Object mPushLock = new Object();

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     *
     * @param activity
     *            Activity
     * @param manager
     *            FragmentManager
     */
    public ContentFragmentStackManager(Activity activity, FragmentManager manager, int contentId,
                                       String contentName, StackLayout view) {
        this(10, activity, manager, contentId, contentName, view);
    }

    /**
     *
     * @param size
     *            栈大小
     * @param activity
     *            上下文
     * @param manager
     *            FragmentManager
     */
    public ContentFragmentStackManager(int size, Activity activity, FragmentManager manager, int contentId,
                                       String contentName, StackLayout view) {
        mMax = size;
        mFragmentStack = new ArrayList<BaseFragment>(STACK_SIZE);
        mSize = 0;
        mManager = manager;
        mContentId = contentId;
        mContentName = contentName;

        mActivity = activity;
        mFlipperContent = view;

        mFlipperContent.setOnStackAnimationListener(new StackLayout.OnStackAnimationListener() {
            @Override
            public void onStackPushAnimationEnd() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        setFlipperHeadMode(mTopFragment);
                        if (mTopFragment != null) {
                            mTopFragment.onEnterAnimationEnd(null);
                        }


                        //
                        if (mFragmentStack.size() > 1) {
                            BaseFragment hideFragment = mFragmentStack
                                    .get(mFragmentStack.size() - 2);
                            hideFragment.onPause();
                        }
                        synchronized (mPushLock) {
                            isPushOrPop = false;
                        }
                    }
                });
            }

            @Override
            public void onStackPushAnimationStart() {

            }

            @Override
            public void onStackPopAnimationEnd() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        setFlipperHeadMode(mTopFragment);

                        if (mFragmentStack.size() > 0) {
                            BaseFragment topFragment = mFragmentStack
                                    .get(mFragmentStack.size() - 1);
                            topFragment.onResume();
                        }

                        synchronized (mPushLock) {
                            isPushOrPop = false;
                        }
                    }
                });

            }

            @Override
            public void onStackPopAnimationStart() {
            }
        });

    }

    public boolean empty() {
        return mSize == 0;
    }

    private void setFlipperHeadMode(BaseFragment fragment) {
    }

    // -------------------------- OTHER METHODS --------------------------

    public void clear() {
        // mManager.beginTransaction().remove(mManager.findFragmentByTag(mContentName)).commitAllowingStateLoss();
        if (empty()) {
            return;
        }

        mFlipperContent.removeAllViews();
        mClearAsyncTask = new ClearAsyncTask();
        mClearAsyncTask.execute();
    }

    public boolean pop(int requestCode, int resultCode, Intent data) {
        if (empty() || mFlipperContent.isAnimating() || isPushOrPop()) {
            return false;
        }
        synchronized (mPushLock) {
            isPushOrPop = true;
        }
        int index = mFragmentStack.size() - 1;
        final BaseFragment _fragment = mFragmentStack.remove(index);
        _fragment.buildDrawCacheBitmap();
        mManager.beginTransaction().remove(_fragment).commitAllowingStateLoss();
        JLog.d(TAG, "zxg@@@@@ pop fragment is:" + _fragment);
        sync();
        if (mSize == 1) {

        }
        if (mTopFragment != null) {
            mTopFragment.onActivityResult(requestCode, resultCode, data);
        }
        return true;
    }

    private boolean isPushOrPop() {
        synchronized (mPushLock) {
            return isPushOrPop;
        }
    }

    /**
     * 将一个BaseFragment添加到栈中。
     *
     * @param cls
     *            二级页面Fragment类型 BaseSecondFragment.class
     * @param args
     *            要传入的Bundle参数
     * @param hashMap
     *            要赋值的字段
     * @see com.renren.mobile.android.base.FragmentFieldsHelper
     */
    public void push(Class<? extends BaseFragment> cls, Bundle args, HashMap<String, Object> hashMap) {
        if (mActivity.isFinishing() || mFlipperContent.isAnimating() || isPushOrPop())
            return;
        synchronized (mPushLock) {
            isPushOrPop = true;
        }
        try {
            BaseFragment _fragment = cls.newInstance();
            _fragment.setArguments(args);
            FragmentTransaction ft = mManager.beginTransaction();
            ft.add(mContentId, _fragment, mContentName);

            if (full()) {
                BaseFragment second = mFragmentStack.remove(2);
                ft.remove(second);
            }
            mFragmentStack.add(_fragment);
            if (!mActivity.isFinishing())
                ft.commitAllowingStateLoss();
            JLog.d(TAG, "zxg@@@@@ push fragment is:" + _fragment);
            sync();
        } catch (IllegalAccessException e) {
            JLog.e(TAG, "--------Caught IllegalAccessException--------");
            JLog.e(TAG, e);
        } catch (InstantiationException e) {
            JLog.e(TAG, "--------Caught InstantiationException--------");
            JLog.e(TAG, e);
        }catch(Exception e){
            JLog.e(TAG, "--------Caught Exception--------");
            JLog.e(TAG, e);
        }
    }

    public boolean full() {
        return mSize == mMax;
    }

    public int size() {
        JLog.d(TAG, "zxg@@@@@ size mSize is:" + mSize + " and mFragmentStack.size() is:" + mFragmentStack.size());
        return mSize;
    }

    private synchronized void sync() {
        mSize = mFragmentStack.size();
        if (mSize == 0) {
            mTopFragment = null;
        } else {
            mTopFragment = mFragmentStack.get(mSize - 1);
            assert mTopFragment == mFragmentStack.get(mSize - 1);
        }
        JLog.d(TAG, "zxg@@@@@ sync mSize is:" + mSize + " and mFragmentStack.size() is:" + mFragmentStack.size());
        assert mSize == mFragmentStack.size();
    }

    public BaseFragment top() {
        return mTopFragment;
    }

    public BaseFragment getSecondFragment() {
        return mSize >= 2 ? mFragmentStack.get(mSize - 2) : null;
    }

    public void destroy() {

    }

    private class ClearAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            FragmentTransaction transaction = mManager.beginTransaction();
            for (BaseFragment f : mFragmentStack) {
                transaction.remove(f);
            }
            transaction.commitAllowingStateLoss();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mFragmentStack.clear();
            sync();
            System.gc();
        }
    }

    @Override
    public void loginOk() {
        if (mFragmentStack != null) {
            for (BaseFragment f : mFragmentStack) {
                if (f != null) {
                    f.loginOk();
                }
            }
        }
    }

    @Override
    public void logoutOk() {
        if (mFragmentStack != null) {
            for (BaseFragment f : mFragmentStack) {
                if (f != null) {
                    f.logoutOk();
                }
            }
        }
    }
}
