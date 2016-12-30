package com.jack.androidheap.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jack.androidheap.R;

/**
 * Created by zhai.yuehui on 2016/12/15.
 */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //注意Handler可以引起的内存泄漏
        mStartHandler.sendEmptyMessageDelayed(0,1000);

    }

    private Handler mStartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //test
            Intent it = new Intent(StartActivity.this, HomePageActivity.class);
            startActivity(it);
            finish();
        }
    };
}
