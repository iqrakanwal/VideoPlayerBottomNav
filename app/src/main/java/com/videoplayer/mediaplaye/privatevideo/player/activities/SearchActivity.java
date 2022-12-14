package com.videoplayer.mediaplaye.privatevideo.player.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.VideoAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnTouchListener {

    private InputMethodManager mImm;
    private String queryString;

    private SearchView mSearchView;
    private VideoAdapter adapter;
    private ArrayList<FileItem> searchResultList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //adapter = new VideoAdapter(this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("search");
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);

        mSearchView.setMaxWidth( Integer.MAX_VALUE );

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });


        menu.findItem(R.id.action_search).expandActionView();
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        hideInputManager();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equals(queryString)) {
            return true;
        }
        queryString = newText;
        if (queryString.trim().equals("")) {
            searchResultList.clear();
            adapter.updateData(searchResultList);
        } else {
            searchVideo(queryString);
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideInputManager();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();
        }
    }
    private void searchVideo(String queryString){
        String regex = "(?i)(bea).*".replace("bea",queryString);
        searchResultList = new ArrayList<>();
        if(Mainutils.getInstance().allfileItems == null || Mainutils.getInstance().allfileItems.size() == 0) return;
        for (FileItem video : Mainutils.getInstance().allfileItems) {

            // higher search result
            if(video.getFileTitle().matches(regex)){
               searchResultList.add(video);
            }
        }
        for (FileItem video : Mainutils.getInstance().allfileItems) {
            if(video.getFileTitle().contains(queryString)){
                if(!isExist(video.getPath()))
                    searchResultList.add(video);
            }
        }

        adapter.updateData(searchResultList);

    }
    private boolean isExist(String path){
        if(searchResultList == null) return false;
        if(searchResultList.size() == 0) return false;
        for (FileItem videoItem :searchResultList){
            if(videoItem.getPath().contains(path)) return true;
        }

        return false;
    }
}
