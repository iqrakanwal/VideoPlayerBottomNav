package com.videoplayer.mediaplaye.privatevideo.player.repository;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class SongsViewModel extends AndroidViewModel {
    private SongsRepository mRepository;
    private LiveData<List<Songs>> notes;

    public SongsViewModel(@NonNull Application application) {
        super(application);

        mRepository = new SongsRepository(application);
    }

    public LiveData<List<Songs>> getNotes() {
        if (notes == null) {
            notes = mRepository.getAllNotes();
        }

        return notes;
    }
    public LiveData<List<Songs>> getSongsFromPlaylist(int playlist) {
        if (notes == null) {
            notes = mRepository.getAllSongfromplaylist(playlist);
        }

        return notes;
    }

    public Songs getNote(int id) throws ExecutionException, InterruptedException {
        return mRepository.getNote(id);
    }

    public void insertNote(Songs note) {
        mRepository.insertNote(note);
    }

    public void updateNote(Songs note) {
        mRepository.updateNote(note);
    }

    public void deleteNote(Songs note) {
        mRepository.deleteNote(note);
    }

    public void deleteAllNotes() {
        mRepository.deleteAllNotes();
    }
//    public LiveData<List<Songs>> getsongfromplaylsit(int playlist) {
//        mRepository.getSongsFromPlaylist(playlist);
//    }
}
