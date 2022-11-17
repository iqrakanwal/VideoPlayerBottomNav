package com.videoplayer.mediaplaye.privatevideo.player.EqualizerPlayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Equalizer;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.activities.EqSettings;

public class EqualizedExoPlayer implements ExoPlayer {

    private static final int NO_AUDIO_SESSION_ID = 0;

    private Context mContext;

    private SimpleExoPlayer mExoPlayer;
    private Equalizer mEqualizer;

    private boolean mEqualizerEnabled;
    private Equalizer.Settings mEqualizerSettings;

    public EqualizedExoPlayer(Context context, SimpleExoPlayer delegate) {
        mContext = context;
        mExoPlayer = delegate;
        mExoPlayer.addAudioDebugListener(new EqualizerEventListener());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            setEqualizerSettings(EqSettings.isEqualizerEnabled, Mainutils.getInstance().getSettings1());

        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void setEqualizerSettings(boolean enabled, Equalizer.Settings settings) {
        boolean invalidate = mEqualizerEnabled != enabled || mEqualizerEnabled;
        boolean wasSystem = isUsingSystemEqualizer();

        mEqualizerEnabled = enabled;
        mEqualizerSettings = settings;

        if (invalidate) {
            updateEqualizerPrefs(enabled, wasSystem);
        }
    }

    private void updateEqualizerPrefs(boolean useCustom, boolean wasSystem) {
        int audioSessionId = mExoPlayer.getAudioSessionId();

        if (audioSessionId == NO_AUDIO_SESSION_ID) {
            // No equalizer is currently bound. Nothing to do.
            return;
        }

        if (useCustom) {
            if (wasSystem || mEqualizer == null) {
                // System -> custom
                unbindSystemEqualizer(audioSessionId);
                bindCustomEqualizer(audioSessionId);
            } else {
                // Custom -> custom
                try{

                    mEqualizer.setProperties(mEqualizerSettings);

                }catch (Exception e){

                    Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            if (!wasSystem) {
                // Custom -> system
                unbindCustomEqualizer();
                bindSystemEqualizer(audioSessionId);
            }
            // Nothing to do for system -> system
        }
    }

    private boolean isUsingSystemEqualizer() {
        return mEqualizerSettings == null || !mEqualizerEnabled;
    }

    private void onBindEqualizer(int newAudioSessionId) {
        if (isUsingSystemEqualizer()) {
            bindSystemEqualizer(newAudioSessionId);
        } else {
            bindCustomEqualizer(newAudioSessionId);
        }
    }

    private void bindSystemEqualizer(int audioSessionId) {
        Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId);
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, mContext.getPackageName());
        mContext.sendBroadcast(intent);
    }

    private void bindCustomEqualizer(int audioSessionId) {
        mEqualizer = new Equalizer(0, audioSessionId);
        mEqualizer.setProperties(mEqualizerSettings);

        mEqualizer.setEnabled(true);
    }

    private void onUnbindEqualizer(int oldAudioSessionId) {
        if (isUsingSystemEqualizer()) {
            unbindSystemEqualizer(oldAudioSessionId);
        } else {
            unbindCustomEqualizer();
        }
    }

    private void unbindSystemEqualizer(int audioSessionId) {
        Intent intent = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId);
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, mContext.getPackageName());
        mContext.sendBroadcast(intent);
    }

    private void unbindCustomEqualizer() {
        if (mEqualizer != null) {
            mEqualizer.setEnabled(false);
            mEqualizer.release();
            mEqualizer = null;
        }
    }

    private class EqualizerEventListener implements AudioRendererEventListener {

        private int lastAudioSessionId = NO_AUDIO_SESSION_ID;

        @Override
        public void onAudioSessionId(int audioSessionId) {
            if (audioSessionId != NO_AUDIO_SESSION_ID) {
                onBindEqualizer(audioSessionId);
                lastAudioSessionId = audioSessionId;
            }
        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {
            if (lastAudioSessionId != NO_AUDIO_SESSION_ID) {
                onUnbindEqualizer(lastAudioSessionId);
            }
        }

        @Override
        public void onAudioEnabled(DecoderCounters counters) {
        }

        @Override
        public void onAudioDecoderInitialized(
                String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        }

        @Override
        public void onAudioInputFormatChanged(Format format) {
        }

        @Override
        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs,
                                        long elapsedSinceLastFeedMs) {
        }
    }

    // region DELEGATED METHODS
    public void setVolume(float volume) {
        mExoPlayer.setVolume(volume);
    }

    @Nullable
    @Override
    public VideoComponent getVideoComponent() {
        return null;
    }

    @Nullable
    @Override
    public TextComponent getTextComponent() {
        return null;
    }

    @Override
    public void addListener(Player.EventListener listener) {
        mExoPlayer.addListener(listener);
    }

    @Override
    public void removeListener(Player.EventListener listener) {
        mExoPlayer.removeListener(listener);
    }

    @Override
    public int getPlaybackState() {
        return mExoPlayer.getPlaybackState();
    }

    @Nullable
    @Override
    public ExoPlaybackException getPlaybackError() {
        return null;
    }

    @Override
    public Looper getPlaybackLooper() {
        return mExoPlayer.getPlaybackLooper();
    }

    @Override
    public void prepare(MediaSource mediaSource) {
        mExoPlayer.prepare(mediaSource);
    }

    @Override
    public void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetTimeline) {
        mExoPlayer.prepare(mediaSource, resetPosition, resetTimeline);
    }

    @Override
    public PlayerMessage createMessage(PlayerMessage.Target target) {
        return null;
    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {
        mExoPlayer.setPlayWhenReady(playWhenReady);
    }

    @Override
    public boolean getPlayWhenReady() {
        return mExoPlayer.getPlayWhenReady();
    }

    @Override
    public void setRepeatMode(int repeatMode) {
        mExoPlayer.setRepeatMode(repeatMode);
    }

    @Override
    public int getRepeatMode() {
        return mExoPlayer.getRepeatMode();
    }

    @Override
    public void setShuffleModeEnabled(boolean shuffleModeEnabled) {
        mExoPlayer.setShuffleModeEnabled(shuffleModeEnabled);
    }

    @Override
    public boolean getShuffleModeEnabled() {
        return mExoPlayer.getShuffleModeEnabled();
    }

    @Override
    public boolean isLoading() {
        return mExoPlayer.isLoading();
    }

    @Override
    public void seekToDefaultPosition() {
        mExoPlayer.seekToDefaultPosition();
    }

    @Override
    public void seekToDefaultPosition(int windowIndex) {
        mExoPlayer.seekToDefaultPosition(windowIndex);
    }

    @Override
    public void seekTo(long windowPositionMs) {
        mExoPlayer.seekTo(windowPositionMs);
    }

    @Override
    public void seekTo(int windowIndex, long windowPositionMs) {
        mExoPlayer.seekTo(windowIndex, windowPositionMs);
    }

    @Override
    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) {
        mExoPlayer.setPlaybackParameters(playbackParameters);
    }

    @Override
    public PlaybackParameters getPlaybackParameters() {
        return mExoPlayer.getPlaybackParameters();
    }

    @Override
    public void stop() {
        mExoPlayer.stop();
    }

    @Override
    public void stop(boolean reset) {

    }

    @Override
    public void release() {
        mExoPlayer.release();
    }

    @Override
    public void sendMessages(ExoPlayerMessage... messages) {
        mExoPlayer.sendMessages(messages);
    }

    @Override
    public void blockingSendMessages(ExoPlayerMessage... messages) {
        mExoPlayer.blockingSendMessages(messages);
    }

    @Override
    public void setSeekParameters(@Nullable SeekParameters seekParameters) {

    }

    @Override
    public int getRendererCount() {
        return mExoPlayer.getRendererCount();
    }

    @Override
    public int getRendererType(int index) {
        return mExoPlayer.getRendererType(index);
    }

    @Override
    public TrackGroupArray getCurrentTrackGroups() {
        return mExoPlayer.getCurrentTrackGroups();
    }

    @Override
    public TrackSelectionArray getCurrentTrackSelections() {
        return mExoPlayer.getCurrentTrackSelections();
    }

    @Override
    public Object getCurrentManifest() {
        return mExoPlayer.getCurrentManifest();
    }

    @Override
    public Timeline getCurrentTimeline() {
        return mExoPlayer.getCurrentTimeline();
    }

    @Override
    public int getCurrentPeriodIndex() {
        return mExoPlayer.getCurrentPeriodIndex();
    }

    @Override
    public int getCurrentWindowIndex() {
        return mExoPlayer.getCurrentWindowIndex();
    }

    @Override
    public int getNextWindowIndex() {
        return mExoPlayer.getNextWindowIndex();
    }

    @Override
    public int getPreviousWindowIndex() {
        return mExoPlayer.getPreviousWindowIndex();
    }

    @Nullable
    @Override
    public Object getCurrentTag() {
        return null;
    }

    @Override
    public long getDuration() {
        return mExoPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return mExoPlayer.getCurrentPosition();
    }

    @Override
    public long getBufferedPosition() {
        return mExoPlayer.getBufferedPosition();
    }

    @Override
    public int getBufferedPercentage() {
        return mExoPlayer.getBufferedPercentage();
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return mExoPlayer.isCurrentWindowDynamic();
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return mExoPlayer.isCurrentWindowSeekable();
    }

    @Override
    public boolean isPlayingAd() {
        return mExoPlayer.isPlayingAd();
    }

    @Override
    public int getCurrentAdGroupIndex() {
        return mExoPlayer.getCurrentAdGroupIndex();
    }

    @Override
    public int getCurrentAdIndexInAdGroup() {
        return mExoPlayer.getCurrentAdIndexInAdGroup();
    }

    @Override
    public long getContentPosition() {
        return mExoPlayer.getContentPosition();
    }
    // endregion DELEGATED METHODS
}
