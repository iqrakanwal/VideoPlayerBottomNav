package com.videoplayer.mediaplaye.privatevideo.player.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.SongDao;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class SongsRepository {

    private SongDao mRepos;
    private LiveData<List<Songs>> mAllNotes;
private LiveData<List<Songs>>  allsongplaylist;
    public SongsRepository(Application application) {
        PlaylistDatabase db = PlaylistDatabase.getInstance(application);
        ;
        mRepos = db.getSongs();
        mAllNotes = mRepos.getAllRepos();
    }

    public LiveData<List<Songs>> getAllNotes() {
        return mAllNotes;
    }

    public LiveData<List<Songs>> getAllSongfromplaylist(int playlist) {


        return mRepos.findRepositoriesForUser(playlist);
    }

    public Songs getNote(int noteId) throws ExecutionException, InterruptedException {
        return new getNoteAsync(mRepos).execute(noteId).get();
    }

    public void insertNote(Songs note) {
        new insertNotesAsync(mRepos).execute(note);
    }

    public void updateNote(Songs note) {
        new updateNotesAsync(mRepos).execute(note);
    }

    public void deleteNote(Songs note) {
        new deleteNotesAsync(mRepos).execute(note);
    }

    public void deleteAllNotes() {
        new deleteAllNotesAsync(mRepos).execute();
    }
//    public AsyncTask<Integer, Void, LiveData<List<Songs>>> getSongsFromPlaylist(int playlist) {
//      return  new getSongFromPlaylist(mRepos).execute(playlist);
//    }


    private static class getNoteAsync extends AsyncTask<Integer, Void, Songs> {

        private SongDao mNoteDaoAsync;

        getNoteAsync(SongDao animalDao) {
            mNoteDaoAsync = animalDao;
        }

        @Override
        protected Songs doInBackground(Integer... ids) {
            return mNoteDaoAsync.getNoteById(ids[0]);
        }
    }

    private static class insertNotesAsync extends AsyncTask<Songs, Void, Long> {

        private SongDao mNoteDaoAsync;

        insertNotesAsync(SongDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Long doInBackground(Songs... notes) {
            long id = mNoteDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateNotesAsync extends AsyncTask<Songs, Void, Void> {

        private SongDao mNoteDaoAsync;

        updateNotesAsync(SongDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Void doInBackground(Songs... notes) {
            mNoteDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteNotesAsync extends AsyncTask<Songs, Void, Void> {

        private SongDao mNoteDaoAsync;

        deleteNotesAsync(SongDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Void doInBackground(Songs... notes) {
            mNoteDaoAsync.delete(notes[0]);
            return null;
        }
    }


    private static class deleteAllNotesAsync extends AsyncTask<Songs, Void, Void> {

        private SongDao mNoteDaoAsync;

        deleteAllNotesAsync(SongDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Void doInBackground(Songs... notes) {
            mNoteDaoAsync.deleteAll();
            return null;
        }
    }

//    private static class getSongFromPlaylist extends AsyncTask<Integer, Void, LiveData<List<Songs>>> {
//
//        private SongDao mNoteDaoAsync;
//
//        getSongFromPlaylist(SongDao noteDao) {
//            mNoteDaoAsync = noteDao;
//        }
//
////        @Override
////        protected List doInBackground(Songs... notes) {
////            mNoteDaoAsync.deleteAll();
////            return null;
////        }
//
//        @Override
//        protected LiveData<List<Songs>> doInBackground(Integer... integers) {
//
//
//            mNoteDaoAsync.findRepositoriesForUser(integers[0]);
//
//            return null;
//        }
//    }
}
