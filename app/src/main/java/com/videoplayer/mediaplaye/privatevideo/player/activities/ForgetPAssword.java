package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.PreferencesSettings;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.util.ArrayList;

public class ForgetPAssword extends BaseActivity {
    Button save;
    ArrayList<String> questions;
    EditText editText;
    Spinner spinner;
    PreferencesUtility preferencesUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_p_assword);

        preferencesUtility = new PreferencesUtility(ForgetPAssword.this);
      //  Toast.makeText(this, "" + preferencesUtility.getQuestion(), Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this, "" + preferencesUtility.getAnswer(), Toast.LENGTH_SHORT).show();
        PreferencesSettings.saveToPref(ForgetPAssword.this, "");
        questions = new ArrayList<>();
        editText = findViewById(R.id.answer);
        spinner = findViewById(R.id.questionslistforget);
        addquestions();

        save = findViewById(R.id.retrivepin);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (ForgetPAssword.this, android.R.layout.simple_spinner_item,
                        questions);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText != null) {

                    if (spinner.getSelectedItem().equals(preferencesUtility.getQuestion())) {

                        if (editText.getText().toString().equals(preferencesUtility.getAnswer())) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgetPAssword.this);
                            alertDialogBuilder.setTitle("PlaylistName");
                            final TextView input = new TextView(ForgetPAssword.this);
                            input.setTextColor(Color.BLACK);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            input.setText("your password is " + preferencesUtility.getPassword());
                            alertDialogBuilder.setView(input);
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            startActivity(new Intent(ForgetPAssword.this, PasswordCheck.class));


                                        }
                                    });


                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    }else{
                        Toast.makeText(ForgetPAssword.this, "Enter right question answer", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


    }

    public void addquestions() {
        questions.add("what is your best teacher name?");
        questions.add("what was your first school's name?");
        questions.add("what was your first pet's name?");
        questions.add("write your own questions?");


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
            startActivity(new Intent(ForgetPAssword.this, FingerPrintActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}