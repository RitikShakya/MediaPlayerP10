package com.company.mp3playerproject10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    private final static String Media_path= Environment.getExternalStorageDirectory().getPath()+"/";

    private ArrayList<String> songlist= new ArrayList<>();

    MusicAdapter musicAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{

            getAllAudiofiles() ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1 && permissions.length>0  && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
            getAllAudiofiles();

        }
    }

    public void getAllAudiofiles(){

        if(Media_path!=null){
            File musicfile = new File(Media_path);

            File[] listfile = musicfile.listFiles();

            for(File file: listfile){
                //Log.e("file", file.toString());

                if(file.isDirectory()){
                    scanDirectory(file); // if th efile is a directory , scan the direcory to reach the bottom files


                }else{
                    String path = file.getAbsolutePath();
                    if(path.endsWith(".mp3")){
                        songlist.add(path);
                        musicAdapter.notifyDataSetChanged();

                    }
                }
            }
        }

        musicAdapter = new MusicAdapter(songlist,MainActivity.this);
        recyclerView.setAdapter(musicAdapter);

    }

    public void scanDirectory(File filedirectory){

        if(filedirectory!=null){
            File[] listfile = filedirectory.listFiles();

            for(File file: listfile){
                Log.e("file", file.toString());

                if(file.isDirectory()){
                    scanDirectory(file); // if th efile is a directory , scan the direcory to reach the bottom files


                }else{
                    String path = file.getAbsolutePath();
                    if(path.endsWith(".mp3")){
                        songlist.add(path);

                    }
                }
            }

        }

    }


}