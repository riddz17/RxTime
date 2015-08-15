package com.github.simonpercic.observabletime.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.simonpercic.observabletime.ObservableTime;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private final ObservableTime observableTime = new ObservableTime();

    private TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTime = (TextView) findViewById(R.id.txt_time);
        findViewById(R.id.btn_refresh).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        observableTime.currentTime()
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
