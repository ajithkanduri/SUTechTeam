package com.example.android.sutechteam;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    android.media.MediaPlayer mp;
    int position;
    ArrayList<File> mySongs;
    Uri uri;
    ImageButton play;
    Thread UpdateSeekBar;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song);
        final ImageButton play = (ImageButton) findViewById(R.id.button);
        ImageButton prev = (ImageButton) findViewById(R.id.button2);
        ImageButton next = (ImageButton) findViewById(R.id.button3);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        UpdateSeekBar = new Thread(){
            public void run(){
                int TotalDuration = mp.getDuration();
                int CurrentPosition=0;
                while (CurrentPosition<TotalDuration){
                    try {
                        sleep(500);
                        CurrentPosition = mp.getCurrentPosition();
                        seekBar.setProgress(CurrentPosition);

                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };



        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songlist");
        position = bundle.getInt("pos", 0);
        uri = Uri.parse(mySongs.get(position).toString());
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        mp = android.media.MediaPlayer.create(getApplicationContext(), uri);

        mp.start();
        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                    play.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                } else {
                    mp.start();
                    play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                mp.release();
                uri = Uri.parse(mySongs.get(position + 1).toString());
                mp = android.media.MediaPlayer.create(getApplicationContext(), uri);
                mp.start();
                position =position+1;

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                mp.release();
                if (position - 1 < 0) {
                    position = mySongs.size() - 1;
                } else {
                    position = position - 1 % mySongs.size();
                }
                uri = Uri.parse(mySongs.get(position - 1).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();


            }
        });
        UpdateSeekBar.start();
        seekBar.setMax(mp.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

    }
  @Override
  protected void onStop(){
        super.onStop();
        mp.stop();
  }
    @Override
    protected  void onRestart(){
        super.onRestart();
        mp.stop();
        mp.release();
    }
}
