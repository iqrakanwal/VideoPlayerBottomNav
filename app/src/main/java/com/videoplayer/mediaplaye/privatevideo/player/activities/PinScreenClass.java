package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

public class PinScreenClass extends BaseActivity {
    public static final String TAG = "PinLockView";
    Toolbar toolbar;
    Button button;
    public static int flagforpassword = 0;
    TextView textView;
    IndicatorDots mIndicatorDots;
    PreferencesUtility preferencesUtility;
    private PinLockView mPinLockView;
    String firsttimepin;
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (flagforpassword == 0) {
                firsttimepin = pin;
                Toast.makeText(PinScreenClass.this, "Enter PAssowrd Again", Toast.LENGTH_SHORT).show();
                textView.setText("Confirm PIN");
                mPinLockView.resetPinLockView();
                flagforpassword = 1;
            } else if (flagforpassword == 1) {
                if (firsttimepin.equals(pin)) {
                    Toast.makeText(PinScreenClass.this, "pin corrected save pin", Toast.LENGTH_SHORT).show();
                    preferencesUtility.setPassword(pin);
                } else {
                    mPinLockView.resetPinLockView();
                    Toast.makeText(PinScreenClass.this, "Pin did nto match enter agian", Toast.LENGTH_SHORT).show();
                }
            }

        }


        @Override
        public void onEmpty() {
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_screen_class);
        toolbar = findViewById(R.id.toolbarforpin);
        toolbar.setTitle("Set Pin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
        preferencesUtility = new PreferencesUtility(PinScreenClass.this);
        button = findViewById(R.id.savepin);
        mPinLockView = findViewById(R.id.pin_lock_viewfor_setscreen);
        textView = findViewById(R.id.texttile);
        mIndicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PinScreenClass.this, Hiddencabinet.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fingerprintoption, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.fingerprint) {

            startActivity(new Intent(PinScreenClass.this, FingerPrintActivity.class));


            return true;


        }


        return super.onOptionsItemSelected(item);
    }
}
