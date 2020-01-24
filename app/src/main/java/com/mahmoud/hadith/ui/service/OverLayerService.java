package com.mahmoud.hadith.ui.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.sharedpreference.UserData;

import java.util.ArrayList;
import java.util.List;

public class OverLayerService extends Service {
    private static final String TAG = "OverLayerService";

    private View mAzkarView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private Handler mPeriodAppearHandler, mMainHandler;
    private Runnable mPeriodAppearRunnable, mMainRunnable;
    private TextView mAzkarTextView;
    private UserData mUserData;
    private List<String> mAzkarArray;
    private long PERIOD_APPEAR_TIME = 8 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        startForeground(1, new Notification());

        Log.i(TAG, "onCreate: ");

        initViews();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand: ");
        mMainHandler.postDelayed(mMainRunnable, 10);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAzkarTextView != null)
            mAzkarTextView.setVisibility(View.GONE);

        disAppearAzkar();

        mMainHandler.removeCallbacks(mMainRunnable);

        mPeriodAppearHandler.removeCallbacks(mPeriodAppearRunnable);

        mAzkarView = null;


        mLayoutParams = null;

        mWindowManager = null;

        Log.i(TAG, "onDestroy: ");
    }

    public void initViews() {

        mAzkarView = LayoutInflater.from(this).inflate(R.layout.azkar_over_layer, null);

        mAzkarTextView = mAzkarView.findViewById(R.id.name_text);

        mLayoutParams = createLayoutParams();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mAzkarView.setOnClickListener(v -> disAppearAzkar());

        mMainHandler = new Handler();

        mPeriodAppearHandler = new Handler();

        mUserData = UserData.getInstance(this);

        mAzkarArray = new ArrayList<>(mUserData.getAzkarSet());

        initRunnable();

    }

    private void initRunnable() {

        mMainRunnable = () -> {


            Log.i(TAG, "initRunnable: 1");
            prepareAzkarTextView();

            appearAzkar();

            //handler to disappear view after 8 seconds
            mPeriodAppearHandler.postDelayed(mPeriodAppearRunnable, PERIOD_APPEAR_TIME);


        };

        mPeriodAppearRunnable = () -> {
            Log.i(TAG, "initRunnable: 2");
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_out_left);
            animation.setDuration(1500);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    disAppearAzkar();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            mAzkarTextView.startAnimation(animation);

            if (isScreenOn()) {
                Log.i(TAG, "initRunnable: screen on");
                new Handler().postDelayed(this::disAppearAzkar, 1500);
            } else {
                Log.i(TAG, "initRunnable: screen off");
                disAppearAzkar();
            }
        };
    }

    private void prepareAzkarTextView() {

        int currentNum = mUserData.getCurrentNumberAzkar();

        if (currentNum >= mAzkarArray.size()) {
            currentNum = 0;
        }
        mAzkarTextView.setText(mAzkarArray.get(currentNum));

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pull_in_left);
        animation.setDuration(1500);

        mAzkarTextView.setAnimation(animation);

        currentNum++;

        if (currentNum == mAzkarArray.size()) {
            currentNum = 0;
        }
        mUserData.setCurrentNumberAzkar(currentNum);

    }

    private WindowManager.LayoutParams createLayoutParams() {
        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.START | Gravity.BOTTOM;

        return params;
    }

    public void appearAzkar() {

        if (mWindowManager == null || mAzkarView == null || mLayoutParams == null || mAzkarView.getWindowToken() != null) {
            initViews();
            return;
        }
        mWindowManager.addView(mAzkarView, mLayoutParams);
    }

    public void disAppearAzkar() {

        if (mWindowManager == null || mAzkarView == null || mLayoutParams == null || mAzkarView.getWindowToken() == null) {
            initViews();
            return;
        }
        mWindowManager.removeViewImmediate(mAzkarView);

        stopSelf();
    }


    private boolean isScreenOn() {
        // If you use API20 or more:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {

            DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
            if (dm != null) {
                for (Display display : dm.getDisplays()) {
                    if (display.getState() != Display.STATE_OFF) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            // If you use less than API20:
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            return powerManager != null && powerManager.isScreenOn();
        }
    }

}
