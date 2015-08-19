package com.github.simonpercic.rxtime.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.simonpercic.rxtime.RxTime;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private final RxTime rxTime = new RxTime();

    private TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTime = (TextView) findViewById(R.id.txt_time);
        findViewById(R.id.btn_refresh).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        rxTime.currentTime()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long time) {
                        txtTime.setText("Current time: " + time);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Log.d("TEST", throwable.getMessage());
                    }
                });
    }
}
