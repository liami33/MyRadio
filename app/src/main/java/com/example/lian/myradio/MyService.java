package com.example.lian.myradio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;

public class MyService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private MediaPlayer player = new MediaPlayer();
    private final IBinder musicBind = new MusicBinder();

    @Override
    public void onPrepared (MediaPlayer mp) {
    }

    @Override
    public boolean onError (MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
    }



    public class MusicBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }


    public void play(String url) {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(url);
            player.prepare(); // might take long! (for buffering, etc)
        }
        catch (IOException e) {
            // do nothing
        }
        player.start();
    }

    public void stop() {
        player.stop();
        player.reset();
        player.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        stop();
        return false;
    }
 //   @Override
//    public int onStartCommand(Intent intent, int flags, int startID) {
//        thread.start();
//        return START_STICKY;
//    }

    @Override
    public void onDestroy() {
       stop();

    }
}
