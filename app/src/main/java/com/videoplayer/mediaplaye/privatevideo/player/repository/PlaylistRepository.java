package com.videoplayer.mediaplaye.privatevideo.player.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.playlistDao;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class PlaylistRepository {

    private playlistDao mNoteDao;
    private LiveData<List<Playlist>> mAllNotes;

    public PlaylistRepository(Application application) {
        PlaylistDatabase db = PlaylistDatabase.getInstance(application);
        mNoteDao = db.getplaylist();
        mAllNotes = mNoteDao.getAllNotes();
    }

    public LiveData<List<Playlist>> getAllNotes() {
        return mAllNotes;
    }

    public Playlist getNote(int noteId) throws ExecutionException, InterruptedException {
        return new getNoteAsync(mNoteDao).execute(noteId).get();
    }

    public void insertNote(Playlist note) {
        new insertNotesAsync(mNoteDao).execute(note);
    }

    public void updateNote(Playlist note) {
        new updateNotesAsync(mNoteDao).execute(note);
    }

    public void deleteNote(Playlist note) {
        new deleteNotesAsync(mNoteDao).execute(note);
    }

    public void deleteAllNotes() {
        new deleteAllNotesAsync(mNoteDao).execute();
    }

    public int getidbytext(String text) throws ExecutionException, InterruptedException {
        int id = new getidbytext(mNoteDao).execute(text).get();

        return id;
    }


    private static class getNoteAsync extends AsyncTask<Integer, Void, Playlist> {

        private playlistDao mNoteDaoAsync;

        getNoteAsync(playlistDao animalDao) {
            mNoteDaoAsync = animalDao;
        }

        @Override
        protected Playlist doInBackground(Integer... ids) {
            return mNoteDaoAsync.getNoteById(ids[0]);
        }
    }

    private static class insertNotesAsync extends AsyncTask<Playlist, Void, Long> {

        private playlistDao mNoteDaoAsync;

        insertNotesAsync(playlistDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Long doInBackground(Playlist... notes) {
            long id = mNoteDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateNotesAsync extends AsyncTask<Playlist, Void, Void> {

        private playlistDao mNoteDaoAsync;

        updateNotesAsync(playlistDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Void doInBackground(Playlist... notes) {
            mNoteDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteNotesAsync extends AsyncTask<Playlist, Void, Void> {

        private playlistDao mNoteDaoAsync;

        deleteNotesAsync(playlistDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Void doInBackground(Playlist... notes) {
            mNoteDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllNotesAsync extends AsyncTask<Playlist, Void, Void> {

        private playlistDao mNoteDaoAsync;

        deleteAllNotesAsync(playlistDao noteDao) {
            mNoteDaoAsync = noteDao;
        }

        @Override
        protected Void doInBackground(Playlist... notes) {
            mNoteDaoAsync.deleteAll();
            return null;
        }
    }

//    private static class getidbytext extends AsyncTask<String, Void, Integer> {
//
//        private playlistDao mNoteDaoAsync;
//String text;
//        getidbytext(playlistDao noteDao, String text) {
//            mNoteDaoAsync = noteDao;
//            this.text= text;
//        }
//
////        @Override
////        protected Void doInBackground(Playlist... notes) {
////            mNoteDaoAsync.deleteAll();
////            return null;
////        }
//
//        @Override
//        protected Integer doInBackground(String... strings) {
//            int id = mNoteDaoAsync.getIdByText(strings[0]);
//            return id;        }
//    }


    private static class getidbytext extends AsyncTask<String, Void, Integer> {

        private playlistDao mNoteDaoAsync;

        //playlistDao mdf;
        getidbytext(playlistDao text) {
            mNoteDaoAsync = text;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int id = mNoteDaoAsync.getIdByText(strings[0]);
            return id;
        }
    }
}
