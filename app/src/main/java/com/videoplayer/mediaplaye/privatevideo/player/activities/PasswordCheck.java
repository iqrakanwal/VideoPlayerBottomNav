package com.videoplayer.mediaplaye.privatevideo.player.activities;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;

public class PasswordCheck extends BaseActivity {
    Toolbar toolbar;
    TextView textView;
    Button button;
    public static ArrayList<File> fileArrayList;
    ArrayList<File> fileList;
    TextView forgetpassword;
    IndicatorDots mIndicatorDots;
    PreferencesUtility preferencesUtility;
    private PinLockView mPinLockView;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            //Toast.makeText(PasswordCheck.this, ""+pin, Toast.LENGTH_SHORT).show();
          ///  preferencesUtility.setPassword(pin);


            if(preferencesUtility.getPassword().equals(pin)){
                startActivity(new Intent(PasswordCheck.this, PrivateFolder.class));
                finish();
            }else if(!preferencesUtility.getPassword().equals(pin)){
                Toast.makeText(PasswordCheck.this, "wrong", Toast.LENGTH_SHORT).show();
                mPinLockView.resetPinLockView();
            }
        }




        @Override
        public void onEmpty() {
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_check);
        toolbar = findViewById(R.id.toolbarforpass);
        toolbar.setTitle("Enter Pin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       fileArrayList =new ArrayList<>();
        fileList= new ArrayList<>();
     /*   if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
//        File src = new File(Environment.getExternalStorageDirectory().getPath() + "/Aaaa/");
//        File des = new File(Environment.getExternalStorageDirectory().getPath() + "/VideoPlayer/");
//        Decryptor.getDecrypter(false).decrypt(src, des);
//        fileArrayList.addAll(getfile(des));
//        for (int i = 0; i < fileList.size(); i++) {
//            fileArrayList.add(fileList.get(i));
//        }
        preferencesUtility=new PreferencesUtility(PasswordCheck.this);
        button = findViewById(R.id.savepin);
        textView = findViewById(R.id.setpass);
        forgetpassword=findViewById(R.id.forget);
        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(PasswordCheck.this, ForgetPAssword.class));
               finish();
            }
        });



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                   startActivity(new Intent(PasswordCheck.this, PrivateFolder.class));
//            }
//        });

    }
    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".mp3")
                            || listFile[i].getName().endsWith(".mp4") || listFile[i].getName().endsWith(".m4a")
                    ) {
                        fileList.add(listFile[i]);
                    }
                }

            }
        }
        return fileList;
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

            startActivity(new Intent(PasswordCheck.this, FingerPrintActivity.class));
            return true;


        }


        return super.onOptionsItemSelected(item);
    }
}