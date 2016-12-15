package com.jack.androidheap.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jack.androidheap.fragment.base.BaseFragment;
import com.jack.androidheap.utils.JLog;

import java.util.HashMap;

/**
 * Created by zhai.yuehui on 2016/12/15.
 */

public abstract class BaseFragmentActivity extends BaseActivity {


    private static final String TAG = "BaseFragmentActivity";

    private ContentFragmentStackManager mContentFragmentStackManager;

    protected FragmentManager mManager;

    private int mContainerId;

    private String mContentTag;

    private StackLayout mMainFragmentContainer;

    protected static HashMap<String, Object> fragmentFields;
    protected int mSelectedIndex;
    protected Bundle mArgs;
    protected Class<? extends BaseFragment> mFragmentCls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mManager = getSupportFragmentManager();
    }

    private void setContainerId(int id) {
        this.mContainerId = id;
    }

    private void setContentTag(String tag) {
        this.mContentTag = TAG;
    }

    private void setStackLayout(StackLayout layout) {
        this.mMainFragmentContainer = layout;
    }

    protected void makeNewContentFragmentStackManager(int id, String tag, StackLayout layout) {
        setContainerId(id);
        setContentTag(tag);
        setStackLayout(layout);
        mContentFragmentStackManager = new ContentFragmentStackManager(this, mManager, mContainerId, mContentTag,
                mMainFragmentContainer);
    }

    public void addSecondFragment(Class<? extends BaseFragment> cls, Bundle args, HashMap<String, Object> fields) {
        if (args == null) {
            args = new Bundle();
        }
        BaseFragment topFragment = mContentFragmentStackManager.top();

//		if (topFragment != null) {
//			if (topFragment.getClass().equals(cls)) {
//				if (!JumpToFragement.isCanGotoNewFragment(BaseFragmentActivity.this, topFragment, args, -1)) {
//					return;
//				}
//			}
//		}
//		BaseFragment secondFragment = mContentFragmentStackManager.getSecondFragment();
//		if (secondFragment != null) {
//			if (secondFragment.getClass().equals(cls)) {
//				if (!JumpToFragement.isCanGotoNewFragment(BaseFragmentActivity.this, secondFragment, args, -1)) {
//					popBackStack();
//					return;
//				}
//			}
//		}
        mContentFragmentStackManager.push(cls, args, fields);
    }

    public void popBackStack(int requestCode, int resultCode, Intent data) {
        mContentFragmentStackManager.pop(requestCode, resultCode, data);
    }

    public void popBackStack() {
        mContentFragmentStackManager.pop(-100, -100, null);
    }

    public void clearBackStack() {
        mContentFragmentStackManager.clear();
    }

    /**
     * Hide all fragments except the top one in the stack. don't call this
     * method.
     */
    public void hideFragments() {
        FragmentTransaction ft = mManager.beginTransaction();
        BaseFragment fragment = (BaseFragment) mManager.findFragmentByTag(mContentTag);
        if (!mContentFragmentStackManager.empty()) {
            ft.hide(fragment);
        }
    }

    public void addContent() {
        addContent(-1);
    }

    public BaseFragment top() {
        return mContentFragmentStackManager.top();
    }

    protected int size() {
        JLog.d(TAG, "zxg@@@@@ mContentFragmentStackManager.size() is:" + mContentFragmentStackManager.size());
        return mContentFragmentStackManager.size();
    }

    public void addContent(int isDefaultTabs) {
        mContentFragmentStackManager.push(mFragmentCls, mArgs, fragmentFields);
    }

    protected void loginOk() {
        if (mContentFragmentStackManager != null) {
            JLog.d(TAG, "loginOk ----->1");
            mContentFragmentStackManager.loginOk();
            JLog.d(TAG, "loginOk ----->2");
        }
    }

    protected void logoutOk() {
        if (mContentFragmentStackManager != null) {
            JLog.d(TAG, "logoutOk ----->1");
            mContentFragmentStackManager.logoutOk();
            JLog.d(TAG, "logoutOk ----->2");
        }
    }





    public BaseFragment getCurrentFragment() {
        return top();
    }

}
