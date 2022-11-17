package com.videoplayer.mediaplaye.privatevideo.player;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.activities.MainClass;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.DataModel;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.ThemeAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.models.ThemeChoiceItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.util.ArrayList;

public class ThemeActivity extends BaseActivity {
    PreferencesUtility preferencesUtility;
    int currentTheme;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        arrayList = new ArrayList();

        preferencesUtility = PreferencesUtility.getInstance(this);
        currentTheme = preferencesUtility.getThemeSettings();
        recyclerView = findViewById(R.id.recyclerView);
        ThemeAdapter adapter = new ThemeAdapter( this);
        recyclerView.setAdapter(adapter);


        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/

        /*AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView.setLayoutManager(layoutManager);*/


        /**
         Simple GridLayoutManager that spans two columns
         **/
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        adapter.updateData(setupListData());
    }


    @Override
    public void onBackPressed() {
        if (preferencesUtility.getThemeSettings() == currentTheme)
            super.onBackPressed();
        else {
            Intent intent = new Intent(ThemeActivity.this, MainClass.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<DataModel> setupListData() {
        arrayList = new ArrayList();
        arrayList.add(new DataModel("Item 1", R.drawable.custom_background, "#09A9FF", 0));
        arrayList.add(new DataModel("Item 2", R.drawable.custom_background1, "#3E51B1", 1));
        arrayList.add(new DataModel("Item 3", R.drawable.custom_background2, "#673BB7",2));
        arrayList.add(new DataModel("Item 4", R.drawable.custom_background3, "#4BAA50",3));
        arrayList.add(new DataModel("Item 5", R.drawable.custom_background4, "#F94336",4));
        arrayList.add(new DataModel("Item 6", R.drawable.custom_background5, "#0A9B88",5));


        return arrayList;
    }

}
