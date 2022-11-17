package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.repository.PlaylistRepository;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PlaylistListViewModel extends AndroidViewModel {
    private PlaylistRepository mRepository;
    private LiveData<List<Playlist>> notes;

    public PlaylistListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new PlaylistRepository(application);
    }

    public LiveData<List<Playlist>> getNotes() {
        if (notes == null) {
            notes = mRepository.getAllNotes();
        }

        return notes;
    }

    public Playlist getNote(int id) throws ExecutionException, InterruptedException {
        return mRepository.getNote(id);
    }

    public void insertNote(Playlist note) {
        mRepository.insertNote(note);
    }

    public void updateNote(Playlist note) {
        mRepository.updateNote(note);
    }

    public void deleteNote(Playlist note) {
        mRepository.deleteNote(note);
    }

    public void deleteAllNotes() {
        mRepository.deleteAllNotes();
    }
    public int getidbytext(String text) {
        try {
            return mRepository.getidbytext(text);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
