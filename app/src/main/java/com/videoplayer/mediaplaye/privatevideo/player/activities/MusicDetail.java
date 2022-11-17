package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.AudioAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.VideoAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentGridLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.AllFolderList;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.folderdetailslistener;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MusicDetail extends BaseActivity implements folderdetailslistener {
    RecyclerView recyclerView;
    public static int flagforlayout = 0;
    public boolean is_in_action_mode = false;
    TextView counter_text;
    AudioAdapter videoAdapter;
    Toolbar toolbarforvideo;
    LinearLayout sortby, search, privatefolder, selectall, deleteselected;

    EditText editTextSearch;
    LinearLayout bottomLinearLayout;
    LinearLayout listsortsearch;
    public boolean isSelectedall = true;
    ImageView option;
    ImageView imageView;
    ArrayList<FileItem> videoItems = new ArrayList<>();
    public static ArrayList<FileItem> sellection_list = new ArrayList<>();
    int counter = 0;
    public int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        toolbarforvideo = findViewById(R.id.toolbarforvideo);
        toolbarforvideo.setTitle("");
        setSupportActionBar(toolbarforvideo);
        counter_text = findViewById(R.id.toolbartext);
        imageView = findViewById(R.id.backbuttom);
        recyclerView = findViewById(R.id.recyclerView);
     //   viewtext = findViewById(R.id.view);
        editTextSearch = findViewById(R.id.editTextSearch);
       // viewicon = findViewById(R.id.viewicon);
      //  listview = findViewById(R.id.listviewvideo);
        sortby = findViewById(R.id.sortbyvideo);
        bottomLinearLayout = findViewById(R.id.bottomLinearLayoutvideo);
        listsortsearch = findViewById(R.id.listsortsearch);
        search = findViewById(R.id.searchvideo);
        privatefolder = findViewById(R.id.privatefolder);
        option = findViewById(R.id.option);
//        if (PreferencesUtility.getInstance(MusicDetail.this).isAlbumsInGrid()) {
//            viewtext.setText("List View ");
//            viewicon.setBackgroundResource(R.drawable.ic_listicon);
//
//            GridLayoutManager gridManager = new GridLayoutManager(MusicDetail.this, 3);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
//                    if (videoAdapter.is_add(position))
//                        return 3;
//                    else
//                        return 1;
//                }
//            });
//            recyclerView.setLayoutManager(gridManager);
//        } else {
//            viewtext.setText("GridView");
//            viewicon.setBackgroundResource(R.drawable.ic_grid);


            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(MusicDetail.this, LinearLayoutManager.VERTICAL, false));
       // }

//        if (PreferencesUtility.getInstance(FolderDetails.this).isAlbumsInGrid()) {
//            viewtext.setText("List View");
//            viewicon.setBackgroundResource(R.drawable.ic_listicon);
//            GridLayoutManager gridManager = new GridLayoutManager(FolderDetails.this, 3);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
//                    if (videoAdapter.is_add(position))
//                        return 3;
//                    else
//                        return 1;
//                }
//            });
//            recyclerView.setLayoutManager(gridManager);
//        } else {
//            viewtext.setText("GridView");
//            viewicon.setBackgroundResource(R.drawable.ic_grid);
//            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(FolderDetails.this, LinearLayoutManager.VERTICAL, false));
//        }
        videoItems.clear();
        recyclerView.hasFixedSize();
        videoItems = Mainutils.getInstance().folderItem.getVideoItems();
        videoAdapter = new AudioAdapter(this, videoItems);
        recyclerView.setAdapter(videoAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
        // videoAdapter.updateData(videoItems);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearActionMode();
            }

        });

        selectall = findViewById(R.id.seletcall);
        deleteselected = findViewById(R.id.deleteselected);
        deleteselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteselected();
            }
        });


//        listview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (PreferencesUtility.getInstance(MusicDetail.this).isAlbumsInGrid()) {
//                    PreferencesUtility.getInstance(MusicDetail.this).setAlbumsInGrid(false);
//                    setLayoutManager();
//                } else {
//                    PreferencesUtility.getInstance(MusicDetail.this).setAlbumsInGrid(true);
//                    setLayoutManager();
//                }
//
//
//            }
//        });
        sortby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomsortClass cdd = new CustomsortClass(MusicDetail.this);
                cdd.show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flagforlayout = 1;
                editTextSearch.setVisibility(View.VISIBLE);
              //  listview.setVisibility(View.GONE);
                sortby.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
            }
        });
        privatefolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
                if ((des.mkdir())) {
                    Toast.makeText(MusicDetail.this, "created", Toast.LENGTH_SHORT).show();
                }


                for (int i = 0; i < videoItems.size(); i++) {
                    File privatefolder = new File(videoItems.get(i).getPath());
                    Encryptor.getEncrypter(true).encrypt(privatefolder, des);
                    videoItems.remove(videoItems.get(i));
                }
                Toast.makeText(MusicDetail.this, "true", Toast.LENGTH_SHORT).show();


                is_in_action_mode = false;
                isSelectedall = false;
                imageView.setVisibility(View.GONE);
                counter_text.setText(getString(R.string.app_name));
                counter = 0;
                videoAdapter.notifyDataSetChanged();
            }
        });
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectedall) {

                    isSelectedall = false;
                    for (int i = 0; i < videoItems.size(); i++) {
                        videoItems.get(i).setSelected(true);

                        counter++;
                    }
                    videoAdapter.notifyDataSetChanged();
                    updateCounter(counter);
                } else {
                    isSelectedall = true;
                    for (int i = 0; i < videoItems.size(); i++) {
                        videoItems.get(i).setSelected(false);
                        counter--;


                    }

                    videoAdapter.notifyDataSetChanged();
                    updateCounter(counter);

                }

                Toast.makeText(MusicDetail.this, "" + sellection_list.toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        deleteselected.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                for(int i = 0 ; i <selection.size(); i ++){
//                    File file= new File(selection.get(i).getPath());
//                    file.delete();
//                    Toast.makeText(MusicDetail.this, "delete", Toast.LENGTH_SHORT).show();
//
//
//                }
//
//                is_in_action_mode = false;
//                toolbarforvideo.setVisibility(View.GONE);
//                selection.clear();
//                isSelectedall=false;
//                imageView.setVisibility(View.GONE);
//                counter_text.setText("0 item selected");
//                counter = 0;
//                videoAdapter.notifyDataSetChanged();
//
//                //    Toast.makeText(MusicDetail.this, ""+videoAdapter.getTotalVideoSelected(), Toast.LENGTH_SHORT).show();;
//             //   videoAdapter.deleteListVideoSelected();
//
//
//
//
//
//            //    videoAdapter.notifyDataSetChanged();
//               /* if (sellection_list.size() != 0) {
//                    for (int i = 0; i < sellection_list.size(); i++) {
//                        File file = new File(sellection_list.get(i).getPath());
//                        file.delete();
//                        Toast.makeText(MusicDetail.this, "true", Toast.LENGTH_SHORT).show();
//                    }
//*/
//                  //  videoAdapter.notifyDataSetChanged();
//
//               // }
//            //    Toast.makeText(MusicDetail.this, "" + sellection_list.toString(), Toast.LENGTH_SHORT).show();
//
//
//                is_in_action_mode=false;
//
//            }
//        });

    }
    public class CustomsortClass extends Dialog {

        public Activity c;
        public Dialog d;
        RadioGroup radiogroup;
        RadioButton nameradio;


        TextView ok, cancel;


        public CustomsortClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.content_sort_info);
            radiogroup = findViewById(R.id.radiogroup);

            nameradio = findViewById(R.id.nameradio);


            ok = findViewById(R.id.ok);
            cancel = findViewById(R.id.cancel);


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int selectedId = radiogroup.getCheckedRadioButtonId();
                    nameradio = (RadioButton) findViewById(selectedId);


                    if (nameradio.getText() == "Name") {
                        Toast.makeText(c, ""+nameradio.getText().toString(), Toast.LENGTH_SHORT).show();
                        sortAZ();

                    } else if (nameradio.getText() == "Date") {


                    } else if (nameradio.getText() == "Size") {


                    } else if (nameradio.getText() == "Lenght") {

                    }


                    dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();

                }
            });
        }
    }

    private void deleteselected() {
        for (int i = 0; i < videoItems.size(); i++) {

            if (videoItems.get(i).isSelected()) {
                File file = new File(videoItems.get(i).getPath());
                file.delete();
                Toast.makeText(this, "dleete", Toast.LENGTH_SHORT).show();
                videoItems.remove(i);
            }

        }
        is_in_action_mode = false;
        isSelectedall = false;
        imageView.setVisibility(View.GONE);
        counter_text.setText(getString(R.string.app_name));
        counter = 0;
        videoAdapter.notifyDataSetChanged();
//for(int i = 0;i<videoItems.size();i++){
//
//    videoItems.get(i).setSelected(false);
//
//}


    }


    public int getTotalSelected() {
        if (videoAdapter == null)
            return 0;
        return videoAdapter.getTotalVideoSelected();
    }

    private void clearActionMode() {
        is_in_action_mode = false;
        imageView.setVisibility(View.GONE);
        counter_text.setText(getString(R.string.app_name));
        counter = 0;
        videoAdapter.notifyDataSetChanged();
        bottomLinearLayout.setVisibility(View.GONE);

    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<FileItem> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (FileItem s : videoItems) {
            //if the existing elements contains the search input
            if (s.getFileTitle().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        videoAdapter.filterList(filterdNames);
    }

    public void startselection(int position) {
        if (!is_in_action_mode) {
            is_in_action_mode = true;
            bottomLinearLayout.setVisibility(View.VISIBLE);
            videoItems.get(position).setSelected(true);
            counter++;
            updatetoolbar(counter);
            position = position;
            Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
            toolbarforvideo.setVisibility(View.VISIBLE);
            videoAdapter.notifyDataSetChanged();
        }


    }

    private void updatetoolbar(int counter) {

        if (counter == 0) {
            counter_text.setText("0 item selected");
        }
        if (counter == 1) {
            counter_text.setText("1 item selected");
        } else {
            counter_text.setText(counter + " item selected");
        }


    }

    public void check(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            videoItems.get(position).setSelected(true);
            counter++;
            updatetoolbar(counter);

        } else {
            videoItems.get(position).setSelected(false);
            counter--;
            updatetoolbar(counter);


        }
    }

    public void playItemSelected() {
        ArrayList<FileItem> videoItems = videoAdapter.getVideoItemsSelected();
        if (videoItems.size() > 0 && MusicDetail.this != null) {
            Mainutils.getInstance().videoItemsPlaylist = videoItems;
            Mainutils.getInstance().playingVideo = videoItems.get(0);
            if (!Mainutils.getInstance().isPlayingAsPopup()) {
                Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);
                Intent intent = new Intent(MusicDetail.this, PlayVideoActivity.class);
                MusicDetail.this.startActivity(intent);
                if (Mainutils.getInstance().videoBackendService != null)
                    Mainutils.getInstance().videoBackendService.releasePlayerView();
            } else {
                ((BaseActivity) this).showFloatingView(this, true);
            }
        }
    }

    public void sortAZ() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortVideoAZ(videoItems);
            videoAdapter.updateData(videoItems);
        }


    }

    public void sortZA() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortVideoZA(videoItems);
            videoAdapter.updateData(videoItems);
        }
    }

    public void sortSize() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortSongSize();
            videoAdapter.updateData(videoItems);
        }
    }

    public void shareSelected() {
        if (videoAdapter == null || MusicDetail.this == null) return;
        ArrayList<FileItem> videoItems = videoAdapter.getVideoItemsSelected();
        AppsUtils.shareMultiVideo(MusicDetail.this, videoItems);
    }

//    public void deleteSelected() {
//        videoAdapter.deleteListVideoSelected();
//    }

    public void updateVideoList(ArrayList<FileItem> videoItems) {
        if (videoItems == null) return;
        this.videoItems = videoItems;
        Mainutils.getInstance().folderItem.setVideoItems(videoItems);
        Mainutils.getInstance().isNeedRefreshFolder = true;
    }

    public void releaseUI() {
        for (FileItem videoItem : videoItems) {
            videoItem.setSelected(false);
        }
        videoAdapter.updateData(videoItems);
    }


    private void doLayoutChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (PreferencesUtility.getInstance(MusicDetail.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(MusicDetail.this, 3));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(MusicDetail.this, LinearLayoutManager.VERTICAL, false));
            }
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (PreferencesUtility.getInstance(MusicDetail.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(MusicDetail.this, 2));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(MusicDetail.this, LinearLayoutManager.VERTICAL, false));
            }
        }
        videoAdapter.updateData(videoItems);
    }

    private int getCurrentOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void setLayoutManager(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (PreferencesUtility.getInstance(MusicDetail.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(MusicDetail.this, 4));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(MusicDetail.this, LinearLayoutManager.VERTICAL, false));
            }
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (PreferencesUtility.getInstance(MusicDetail.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(MusicDetail.this, 2));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(MusicDetail.this, LinearLayoutManager.VERTICAL, false));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (flagforlayout == 1) {
            editTextSearch.setVisibility(View.GONE);
            //listview.setVisibility(View.VISIBLE);
            sortby.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            editTextSearch.setText("");
            flagforlayout = 0;
        } else {
            startActivity(new Intent(MusicDetail.this, MainClass.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
    }

    private ArrayList<FileItem> sortVideoAZ(ArrayList<FileItem> videoItems) {
        ArrayList<FileItem> m_videos = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < videoItems.size(); i++) {
            names.add(videoItems.get(i).getFolderName() + "_" + videoItems.get(i).getPath());
        }
        Collections.sort(names, String::compareToIgnoreCase);

        for (int i = 0; i < names.size(); i++) {
            String path = names.get(i);
            for (int j = 0; j < videoItems.size(); j++) {
                if (path.equals(videoItems.get(j).getFolderName() + "_" + videoItems.get(j).getPath())) {
                    m_videos.add(videoItems.get(j));
                }
            }
        }


        return m_videos;
    }

    private ArrayList<FileItem> sortVideoZA(ArrayList<FileItem> videoItems) {
        ArrayList<FileItem> m_videos = sortVideoAZ(videoItems);
        Collections.reverse(m_videos);

        return m_videos;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void setLayoutManager() {


        if (PreferencesUtility.getInstance(MusicDetail.this).isAlbumsInGrid()) {
            GridLayoutManager gridManager = new GridLayoutManager(MusicDetail.this, 3);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    if (videoAdapter.is_add(position))
                        return 3;
                    else
                        return 1;
                }
            });
            if (recyclerView == null) {

            } else {
                recyclerView.setLayoutManager(gridManager);
            }
        } else {

            if (recyclerView == null) {

            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(MusicDetail.this, LinearLayoutManager.VERTICAL, false));
            }
        }
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();

        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    private ArrayList<FileItem> sortSongSize() throws NumberFormatException {
        ArrayList<FileItem> m_videos = videoItems;
        for (int i = 0; i < m_videos.size() - 1; i++) {
            for (int j = 0; j < m_videos.size() - 1 - i; j++) {

                if (m_videos.get(j).getFileSizeAsFloat() < m_videos.get(j + 1).getFileSizeAsFloat()) {
                    Collections.swap(m_videos, j, j + 1);
                }
            }
        }

        return m_videos;

    }


    @Override
    protected void onResume() {
        super.onResume();
        //   startActivity(new Intent(MusicDetail.this, MainClass.class));
    }

//    @Override
//    public boolean onLongClick(View view) {
//        is_in_action_mode = true;
//        toolbarforvideo.setVisibility(View.VISIBLE);
//        counter_text.setVisibility(View.VISIBLE);
//        bottomLinearLayout.setVisibility(View.VISIBLE);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        videoAdapter.notifyDataSetChanged();
//        return true;
//    }


    public void preperaSelection(View view, int position) {


    }


    public void updateCounter(int counter) {


        if (counter == 0) {


            counter_text.setText("0 item selected");
        } else {

            counter_text.setText(counter + " item selected");

        }
    }

    @Override
    public void getfolder(ArrayList<FileItem> arrayList) {

    }

    @Override
    public void privatefolder(ArrayList<FileItem> arrayList) {

    }

    @Override
    public void selectall() {

    }

    @Override
    public void shareall(ArrayList<FileItem> arrayList) {

    }

    @Override
    public void deleteall(ArrayList<FileItem> arrayList) {

    }}