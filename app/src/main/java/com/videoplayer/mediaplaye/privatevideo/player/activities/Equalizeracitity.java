package com.videoplayer.mediaplaye.privatevideo.player.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.google.gson.Gson;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.MainFragment;

import java.util.ArrayList;

public class Equalizeracitity extends BaseActivity {
    public static final String ARG_AUDIO_SESSIOIN_ID = "audio_session_id";

    ImageView backBtn;
    TextView fragTitle;
    SwitchCompat equalizerSwitch;
    public static final String PREF_KEY = "equalizer";

    LineSet dataset;
    LineChartView chart;
    Paint paint;
    float[] points;
    Paint textPaint, circlePaint, circlePaint2, linePaint;
    int y = 0;
    ImageView spinnerDropDownIcon;
    short numberOfFrequencyBands;
    LinearLayout mLinearLayout;

    SeekBar[] seekBarFinal = new SeekBar[5];

    AnalogController bassController, reverbController;
    Spinner presetSpinner;
    FrameLayout equalizerBlocker;
    Context ctx;
    public Equalizer mEqualizer;
    int audiosession;
    public BassBoost bassBoost;
    public PresetReverb presetReverb;

    //static int themeColor = Color.parseColor("#B24242");
    static boolean showBackButton = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizeracitity);
        loadEqualizerSettings(Equalizeracitity.this);
        EqSettings.isEditing = true;
        //Mainutils.getInstance().videoBackendService.getVideoPlayer().getAudioSessionId();
        //   AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // int audioSessionId = audioManager.generateAudioSessionId();

        if(Mainutils.getInstance().videoBackendService.getVideoPlayer().getAudioSessionId()==0){

            audiosession=1;

        }else{


            audiosession= Mainutils.getInstance().videoBackendService.getVideoPlayer().getAudioSessionId();
        }


        if (EqSettings.equalizerModel == null){
            EqSettings.equalizerModel = new EqualizerModel();
            EqSettings.equalizerModel.setReverbPreset(PresetReverb.PRESET_NONE);
            EqSettings.equalizerModel.setBassStrength((short) (1000 / 19));
        }


        mEqualizer = new Equalizer(0, audiosession);
        bassBoost = new BassBoost(0, audiosession);
        bassBoost.setEnabled(EqSettings.isEqualizerEnabled);
        BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
        BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
        bassBoostSetting.strength = EqSettings.equalizerModel.getBassStrength();
        bassBoost.setProperties(bassBoostSetting);
        presetReverb = new PresetReverb(0, audiosession);
        presetReverb.setPreset(EqSettings.equalizerModel.getReverbPreset());
        presetReverb.setEnabled(EqSettings.isEqualizerEnabled);
        mEqualizer.setEnabled(EqSettings.isEqualizerEnabled);
        if (EqSettings.presetPos == 0){
            for (short bandIdx = 0; bandIdx < mEqualizer.getNumberOfBands(); bandIdx++) {
                mEqualizer.setBandLevel(bandIdx, (short) EqSettings.seekbarpos[bandIdx]);
            }
        }
        else {
            mEqualizer.usePreset((short) EqSettings.presetPos);
        }

        backBtn = findViewById(R.id.equalizer_back_btn);
        backBtn.setVisibility(showBackButton ? View.VISIBLE : View.GONE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Equalizeracitity.this != null) {
                    onBackPressed();
                }
            }
        });

        fragTitle = findViewById(R.id.equalizer_fragment_title);


        equalizerSwitch = findViewById(R.id.equalizer_switch);
        equalizerSwitch.setChecked(EqSettings.isEqualizerEnabled);
        equalizerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEqualizer.setEnabled(isChecked);
                bassBoost.setEnabled(isChecked);
                presetReverb.setEnabled(isChecked);
                EqSettings.isEqualizerEnabled = isChecked;
                EqSettings.equalizerModel.setEqualizerEnabled(isChecked);
            }
        });

        spinnerDropDownIcon = findViewById(R.id.spinner_dropdown_icon);
        spinnerDropDownIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetSpinner.performClick();
            }
        });

        presetSpinner = findViewById(R.id.equalizer_preset_spinner);
        equalizerBlocker = findViewById(R.id.equalizerBlocker);
        chart = findViewById(R.id.lineChart);
        paint = new Paint();
        dataset = new LineSet();
        bassController = findViewById(R.id.controllerBass);
        reverbController = findViewById(R.id.controller3D);
        bassController.setLabel("BASS");
        reverbController.setLabel("3D");
        bassController.circlePaint2.setColor(Color.parseColor("#FF4081"));
        bassController.linePaint.setColor(Color.parseColor("#FF4081"));
        bassController.invalidate();
        reverbController.circlePaint2.setColor(Color.parseColor("#FF4081"));
        bassController.linePaint.setColor(Color.parseColor("#FF4081"));
        reverbController.invalidate();

        if (!EqSettings.isEqualizerReloaded) {
            int x = 0;
            if (bassBoost != null) {
                try {
                    x = ((bassBoost.getRoundedStrength() * 19) / 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (presetReverb != null) {
                try {
                    y = (presetReverb.getPreset() * 19) / 6;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        } else {
            int x = ((EqSettings.bassStrength * 19) / 1000);
            y = (EqSettings.reverbPreset * 19) / 6;
            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        }

        bassController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                EqSettings.bassStrength = (short) (((float) 1000 / 19) * (progress));
                try {
                    bassBoost.setStrength(EqSettings.bassStrength);
                    EqSettings.equalizerModel.setBassStrength(EqSettings.bassStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reverbController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                EqSettings.reverbPreset = (short) ((progress * 6) / 19);
                EqSettings.equalizerModel.setReverbPreset(EqSettings.reverbPreset);
                try {
                    presetReverb.setPreset(EqSettings.reverbPreset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                y = progress;
            }
        });

        mLinearLayout = findViewById(R.id.equalizerContainer);

        TextView equalizerHeading = new TextView(Equalizeracitity.this);
        equalizerHeading.setText(R.string.eq);
        equalizerHeading.setTextSize(20);
        equalizerHeading.setGravity(Gravity.CENTER_HORIZONTAL);

        numberOfFrequencyBands = 5;

        points = new float[numberOfFrequencyBands];

        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
        final short upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];

        for (short i = 0; i < numberOfFrequencyBands; i++) {
            final short equalizerBandIndex = i;
            final TextView frequencyHeaderTextView = new TextView(Equalizeracitity.this);
            frequencyHeaderTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            frequencyHeaderTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            frequencyHeaderTextView.setTextColor(Color.parseColor("#FFFFFF"));
            frequencyHeaderTextView.setText((mEqualizer.getCenterFreq(equalizerBandIndex) / 1000) + "Hz");

            LinearLayout seekBarRowLayout = new LinearLayout(Equalizeracitity.this);
            seekBarRowLayout.setOrientation(LinearLayout.VERTICAL);

            TextView lowerEqualizerBandLevelTextView = new TextView(Equalizeracitity.this);
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            lowerEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            lowerEqualizerBandLevelTextView.setText((lowerEqualizerBandLevel / 100) + "dB");

            TextView upperEqualizerBandLevelTextView = new TextView(Equalizeracitity.this);
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            upperEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            upperEqualizerBandLevelTextView.setText((upperEqualizerBandLevel / 100) + "dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.weight = 1;

            SeekBar seekBar = new SeekBar(Equalizeracitity.this);
            TextView textView = new TextView(Equalizeracitity.this);
            switch (i) {
                case 0:
                    seekBar = findViewById(R.id.seekBar1);
                    textView = findViewById(R.id.textView1);
                    break;
                case 1:
                    seekBar = findViewById(R.id.seekBar2);
                    textView = findViewById(R.id.textView2);
                    break;
                case 2:
                    seekBar = findViewById(R.id.seekBar3);
                    textView = findViewById(R.id.textView3);
                    break;
                case 3:
                    seekBar = findViewById(R.id.seekBar4);
                    textView =findViewById(R.id.textView4);
                    break;
                case 4:
                    seekBar = findViewById(R.id.seekBar5);
                    textView = findViewById(R.id.textView5);
                    break;
            }
            seekBarFinal[i] = seekBar;
            seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN));
            seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.SRC_IN));
            seekBar.setId(i);
//            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);

            textView.setText(frequencyHeaderTextView.getText());
            textView.setTextColor(Color.WHITE);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if (EqSettings.isEqualizerReloaded) {
                points[i] = EqSettings.seekbarpos[i] - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(EqSettings.seekbarpos[i] - lowerEqualizerBandLevel);
            } else {
                points[i] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
                EqSettings.seekbarpos[i] = mEqualizer.getBandLevel(equalizerBandIndex);
                EqSettings.isEqualizerReloaded = true;
            }

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mEqualizer.setBandLevel(equalizerBandIndex, (short) (progress + lowerEqualizerBandLevel));
                    points[seekBar.getId()] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                    EqSettings.seekbarpos[seekBar.getId()] = (short)(progress + lowerEqualizerBandLevel);
                    EqSettings.equalizerModel.getSeekbarpos()[seekBar.getId()] =(short) (progress + lowerEqualizerBandLevel);
                    dataset.updateValues(points);
                    chart.notifyDataUpdate();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    presetSpinner.setSelection(0);
                    EqSettings.presetPos = 0;
                    EqSettings.equalizerModel.setPresetPos(0);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        equalizeSound();

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setStrokeWidth((float) (1.10 * EqSettings.ratio));

        dataset.setColor(Color.parseColor("#FF4081"));
        dataset.setSmooth(true);
        dataset.setThickness(5);

        chart.setXAxis(false);
        chart.setYAxis(false);

        chart.setYLabels(AxisController.LabelPosition.NONE);
        chart.setXLabels(AxisController.LabelPosition.NONE);
        chart.setGrid(ChartView.GridType.NONE, 7, 10, paint);

        chart.setAxisBorderValues(-300, 3300);

        chart.addData(dataset);
        chart.show();

        Button mEndButton = new Button(Equalizeracitity.this);
        mEndButton.setBackgroundColor(Color.parseColor("#FF4081"));
        mEndButton.setTextColor(Color.WHITE);


    }

    public void equalizeSound() {
        ArrayList<String> equalizerPresetNames = new ArrayList<>();
        ArrayAdapter<String> equalizerPresetSpinnerAdapter = new ArrayAdapter<>(Equalizeracitity.this,
                R.layout.spinner_item,
                equalizerPresetNames);
        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equalizerPresetNames.add("Custom");

        for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
            equalizerPresetNames.add(mEqualizer.getPresetName(i));
        }

        presetSpinner.setAdapter(equalizerPresetSpinnerAdapter);
        //presetSpinner.setDropDownWidth((Settings.screen_width * 3) / 4);
        if (EqSettings.isEqualizerReloaded && EqSettings.presetPos != 0) {
//            correctPosition = false;
            presetSpinner.setSelection(EqSettings.presetPos);
        }

        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position != 0) {
                        mEqualizer.usePreset((short) (position - 1));
                        EqSettings.presetPos = position;
                        short numberOfFreqBands = 5;

                        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];

                        for (short i = 0; i < numberOfFreqBands; i++) {
                            seekBarFinal[i].setProgress(mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel);
                            points[i] = mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel;
                            EqSettings.seekbarpos[i] = mEqualizer.getBandLevel(i);
                            EqSettings.equalizerModel.getSeekbarpos()[i] = mEqualizer.getBandLevel(i);
                        }
                        dataset.updateValues(points);
                        chart.notifyDataUpdate();
                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, "Error while updating Equalizer", Toast.LENGTH_SHORT).show();
                }
                EqSettings.equalizerModel.setPresetPos(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEqualizer != null){
            mEqualizer.release();
        }

        if (bassBoost != null){
            bassBoost.release();
        }

        if (presetReverb != null){
            presetReverb.release();
        }

        EqSettings.isEditing = false;
    }

    /*
    public static class Builder {
        private int id = -1;

        public EqualizerFragment.Builder setAudioSessionId(int id) {
            this.id = id;
            return this;
        }

        public EqualizerFragment.Builder setAccentColor(int color) {
            themeColor = color;
            return this;
        }

        public Builder setShowBackButton(boolean show){
            showBackButton = show;
            return this;
        }*/
    private void saveEqualizerSettings(){
        if (EqSettings.equalizerModel != null){
            EqualizerSettings settings = new EqualizerSettings();
            settings.bassStrength = EqSettings.equalizerModel.getBassStrength();
            settings.presetPos = EqSettings.equalizerModel.getPresetPos();
            settings.reverbPreset = EqSettings.equalizerModel.getReverbPreset();
            settings.seekbarpos = EqSettings.equalizerModel.getSeekbarpos();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            Gson gson = new Gson();
            preferences.edit()
                    .putString(PREF_KEY, gson.toJson(settings))
                    .apply();

            short[] level=new short[mEqualizer.getNumberOfBands()];
            for (short bandIdx = 0; bandIdx < mEqualizer.getNumberOfBands(); bandIdx++) {
                level[bandIdx] = mEqualizer.getBandLevel(bandIdx);
            }
            Equalizer.Settings settings1 = Mainutils.getInstance().getSettings1();
          //  BassBoost.Settings bass = Mainutils.getInstance().getBass1();



            settings1.bandLevels = level;
            settings1.curPreset = (short) mEqualizer.getCurrentPreset();
            settings1.numBands = (short) mEqualizer.getNumberOfBands();
          //  bass.strength=  Mainutils.getInstance().getBass1().strength;

        }
    }

    public void loadEqualizerSettings(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        EqualizerSettings settings = gson.fromJson(preferences.getString(PREF_KEY, "{}"), EqualizerSettings.class);
        EqualizerModel model = new EqualizerModel();
        model.setBassStrength(settings.bassStrength);
        model.setPresetPos(settings.presetPos);
        model.setReverbPreset(settings.reverbPreset);
        model.setSeekbarpos(settings.seekbarpos);
        EqSettings.isEqualizerEnabled = true;
        EqSettings.isEqualizerReloaded = true;
        EqSettings.bassStrength = settings.bassStrength;
        EqSettings.presetPos = settings.presetPos;
        EqSettings.reverbPreset = settings.reverbPreset;
        EqSettings.seekbarpos = settings.seekbarpos;
        EqSettings.equalizerModel = model;

        Equalizer.Settings settings1 = Mainutils.getInstance().getSettings1();
        settings1.bandLevels = settings.seekbarpos;
        settings1.curPreset = (short) settings.presetPos;
        settings1.numBands = (short) settings.seekbarpos.length;

    }
    @Override
    protected void onPause() {
        try {
            saveEqualizerSettings();

            //  mediaPlayer.pause();
        } catch (Exception ex) {
            //ignore
        }
        super.onPause();
    }
}


