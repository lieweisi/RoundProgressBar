package com.liluo.roundprogressbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liluo.library.RoundProgressBar;

public class MainActivity extends AppCompatActivity {
    private RoundProgressBar rb_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rb_test= (RoundProgressBar) findViewById(R.id.rb_test);
        rb_test.setAnimProgress(50);//设置进度
        rb_test.setCenterText("50");
        rb_test.setSubText("完成度");
        rb_test.setTextSize(25);
        rb_test.setTextSubSize(30);
        rb_test.setTextColor(Color.parseColor("#3F51B5"));
        rb_test.setTextSubColor(Color.parseColor("#FF4081"));
        rb_test.setCricleColor(Color.parseColor("#3F51B5"));
        rb_test.setCricleProgressColor(Color.parseColor("#FF4081"));
    }
}
