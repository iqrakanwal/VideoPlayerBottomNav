package com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import androidx.appcompat.app.AppCompatActivity;


public class rate_us_dialog extends AppCompatActivity {
    String accountName;
    String pass;
    float rate=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us);
      accountName=getIntent().getStringExtra("sender");
      setTitle("");


        ScaleRatingBar ratingBar = findViewById(R.id.simpleRatingBar);


        ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser)
            {

                rate=rating;
                if(rating>3)
                {
                    ((TextView)findViewById(R.id.button)).setText("Rate us on Play store");
                }
                else
                {
                    ((TextView)findViewById(R.id.button)).setText("Feedback");

                }



            }
        });
    }

    public void switch_acc()
    {
        if(rate==0)
            finish();
       else if(rate>3)
        {
            SharedPreferences prefs = getSharedPreferences("apprater", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("rated", true);
            editor.commit();
            try
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));

            }catch (Exception e)
            {
            }

            finish();
        }
        else
        {

            Intent in=new Intent(rate_us_dialog.this, dialogg.class);
            startActivity(in);
            finish();

        }
    }


    public void rate_us(View view)
    {
        switch_acc();
    }



}
