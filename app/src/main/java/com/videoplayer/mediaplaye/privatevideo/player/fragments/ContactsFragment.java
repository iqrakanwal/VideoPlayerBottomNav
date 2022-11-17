package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FileRecyclarView;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContactsFragment extends Fragment {
    RecyclerView recyclerView;
    Cursor fileCursor;
    static String filename1 = "";
    EditText userInput;
    StringBuffer ab = null;
    //  String filename1 = "";
    String[] arrayString;
    LinearLayout linearLayout;
    // ImageView imageView, extracted;

    private ArrayList<File> fileList;
    FileRecyclarView fileAdaptor;
    ArrayList<FileModel> items;
    ImageView imageView, extracted;
    String inputPath = Environment.getExternalStorageDirectory().getPath() + "/";
    ArrayList<String> uri = new ArrayList<String>();
    ArrayList<String> filename = new ArrayList<String>();

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.contacts_fragment, container, false);

        recyclerView= v.findViewById(R.id.file_recyclar_view);
        items = new ArrayList<FileModel>();
        fileList = new ArrayList<>();
        ab = new StringBuffer();

        new LoadApplications().execute();

        File dff = new File(Environment.getExternalStorageDirectory().getPath() + "/");
        if(!dff.exists()){
            dff.mkdir();

        }
        File dir = new File(inputPath);
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileAdaptor = new FileRecyclarView(getContext(), items);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".mp4")) {
                        fileList.add(listFile[i]);
                    }
                }

            }
        }
        return fileList;
    }


    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            /*applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadaptor = new ApplicationAdapter(AllAppsActivity.this,
                    R.layout.snippet_list_row, applist);
*/


            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            getfile(root);



            for (int i = 0; i < fileList.size(); i++) {

                String s = fileList.get(i).getName();
                double fileSizeInBytes = fileList.get(i).length();
                double appSizeInMb = fileSizeInBytes / (1024 * 1024);
                String appSize = String.format("%.2f", appSizeInMb);
                FileModel file= new FileModel();



              /*  if (file.exists()) //Extra check, Just to validate the given path
                {
                    Date lastModDate = new Date(file.lastModified());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy , hh:mm a");
                    String datestr = dateFormat.format(lastModDate);
                    i.setDate(datestr);
                    Log.i("Dated : ", "" + datestr);//Dispaly lastModDate. You can do/use it your own way
                }*/
                file.setFilePath(fileList.get(i).getPath());
                file.setFile(fileList.get(i));
                File file1 = new File(fileList.get(i).getPath());
                if (file1.exists()) //Extra check, Just to validate the given path
                {
                    Date lastModDate = new Date(file1.lastModified());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy , hh:mm a");
                    String datestr = dateFormat.format(lastModDate);
                    file.setDate(datestr);
                    Log.i("Dated : ", "" + datestr);//Dispaly lastModDate. You can do/use it your own way
                }

                file.setSize(appSize.toString());
                file.setFileCreationdate(String.valueOf(fileList.get(i).lastModified()));
                file.setSelected(false);
                //  file.setFileDisplayName(s.substring(0, s.lastIndexOf(".")));
                file.setType( s.substring(s.lastIndexOf(".")));




           /* String name = ;

            int size = Integer.parseInt(String.valueOf(fileList.get(i).length() / 1024));
            String path = ;
*/
                String fullFileName="";
                String ext =s.substring(s.lastIndexOf("."));
                int img;
                switch (ext) {
                    case ".mp4":
                       // img = R.drawable.doc;
                        fullFileName=s.substring(0, s.lastIndexOf("."))+".mp4";
                        break;
                                       default:
                      //  img = R.drawable.ic_android_black_24dp;
                        fullFileName=s.substring(0, s.lastIndexOf("."));
                        break;
                }
                file.setFileDisplayName(fullFileName);
             //   file.setThumbnail(img);

                items.add(file);}


            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fileAdaptor);

            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null,
                    "Loading application info...");



            super.onPreExecute();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}