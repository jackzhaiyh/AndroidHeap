package com.jack.androidheap.activity.base;

import android.content.Intent;
import android.os.Bundle;

import com.jack.androidheap.fragment.base.BaseFragment;

import java.util.HashMap;

/**
 * Created by zhai.yuehui on 2016/12/15.
 */

public interface IContentFragmentStackManager {

    void push(Class<? extends BaseFragment> cls, Bundle args, HashMap<String, Object> hashMap);

    boolean pop(int requestCode, int resultCode, Intent data);

    boolean empty();

    boolean full();

    void clear();

    int size();

    BaseFragment top();

    void destroy();

    public void loginOk();

    public void logoutOk();
}
