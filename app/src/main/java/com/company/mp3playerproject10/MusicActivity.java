package com.company.mp3playerproject10;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    SeekBar seekBarvol, seekBarcontrol;
    TextView filename, progress, total;
    Button previous, next, play;

    String filepath, filetitle;
    int position;
    ArrayList<String > list = new ArrayList<>();

    private MediaPlayer mediaPlayer;
    Runnable runnable; Handler handler;
    int totalTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        seekBarcontrol = findViewById(R.id.seekbarcontrol);
        seekBarvol = findViewById(R.id.seekbarvol);
        filename = findViewById(R.id.textfilename);
        progress = findViewById(R.id.textviewprogessstart);
        total = findViewById(R.id.textviewprogessend);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        play = findViewById(R.id.play);

        filetitle = getIntent().getStringExtra("filename");
        filepath= getIntent().getStringExtra("path");
        position = getIntent().getIntExtra("position",0);
        list = getIntent().getStringArrayListExtra("list");


        filename.setText(filetitle);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
         previous.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
               mediaPlayer.reset();
               if(position==0){
                   position = list.size()-1;

               }else{
                   position--;
               }
                 String newfilepath = list.get(position);
                 try {
                     mediaPlayer.setDataSource(newfilepath);
                     mediaPlayer.prepare();
                     mediaPlayer.start();
                     play.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                     String title = newfilepath.substring(newfilepath.lastIndexOf("/"));

                     filename.setText(title);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }


             }
         });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                }else{
                    mediaPlayer.start();
                    play.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                if(position==list.size()-1){
                    position = 0;

                }else{
                    position++;
                }
                String newfilepath = list.get(position);
                try {
                    mediaPlayer.setDataSource(newfilepath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    play.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    String title = newfilepath.substring(newfilepath.lastIndexOf("/"));

                    filename.setText(title);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        seekBarvol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    seekBarvol.setProgress(i);
                    float volumelevel = i/100f;  // why division by hundred

                    mediaPlayer.setVolume(volumelevel,volumelevel);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarcontrol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);  // to go to the time based on the seekbar
                    seekBarcontrol.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // used to change the seekbar based on the music playing with every second
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                totalTime = mediaPlayer.getDuration();
                seekBarcontrol.setMax(totalTime);
                int currentpos=mediaPlayer.getCurrentPosition();
                seekBarcontrol.setProgress(currentpos);

                handler.postDelayed(runnable,1000);

                String elapsedtime= getTime(currentpos);
                String totaltimme= getTime(totalTime);

                progress.setText(elapsedtime);
                total.setText(totaltimme);

                if(elapsedtime.equals(totaltimme)){
                    mediaPlayer.reset();
                    if(position==list.size()-1){
                        position = 0;

                    }else{
                        position++;
                    }
                    String newfilepath = list.get(position);
                    try {
                        mediaPlayer.setDataSource(newfilepath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        play.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                        String title = newfilepath.substring(newfilepath.lastIndexOf("/"));

                        filename.setText(title);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        handler.post(runnable);
    }

    public String getTime(int currentposition){

        String timelabel;
        int minute,second;

        minute = currentposition/1000/60;
        second = currentposition/1000%60;

        if(second<10){
            timelabel = minute+ ":0" +second;

        }else{
            timelabel = minute+":" +second;
        }
        return timelabel;
    }

}