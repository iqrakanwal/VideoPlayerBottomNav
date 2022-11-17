package com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.R;


public class dialogg extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd);
      //  setTitle("Please let us know about your problem");
      //  setTitle("");

    }

    public void close(View view)
    {
        finish();
    }

    public void send(View view)
    {
        Toast.makeText(this, "Thanks for Your feedback!", Toast.LENGTH_SHORT).show();
    }



}
