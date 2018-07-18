package com.example.android.sutechteam;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MediaPlayer extends AppCompatActivity {

    private static final String TAG = "Media Player";
     static final int RUNTIME_PERMISSION_CODE = 7;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        lv = findViewById(R.id.lvPlaylist);
        AndroidRuntimePermission();
        if(isStoragePermissionGranted()){
            final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
            String[] items = new String[mySongs.size()];
            for (int i = 0; i < mySongs.size(); i++) {
                items[i] = mySongs.get(i).getName().toString().replace(".mp3", "");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startActivity(new Intent(getApplicationContext(), MusicActivity.class).putExtra("pos", i).putExtra("songlist", mySongs));
                }
            });
        }
    }

    public ArrayList<File> findSong (File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] file = root.listFiles();
        for (File singleFile : file) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findSong(singleFile));
            }
            else {
                if (singleFile.getName().endsWith(".mp3")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

    // Creating Runtime permission function.
    public void AndroidRuntimePermission(){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                AlertDialog.Builder alert_builder = new AlertDialog.Builder(MediaPlayer.this);
                alert_builder.setMessage("External Storage Permission is Required.");
                alert_builder.setTitle("Please Grant Permission.");
                alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                MediaPlayer.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                RUNTIME_PERMISSION_CODE
                        );
                    }
                });

                alert_builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = alert_builder.create();
                dialog.show();
            }
            else {
                ActivityCompat.requestPermissions(
                        MediaPlayer.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        RUNTIME_PERMISSION_CODE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case RUNTIME_PERMISSION_CODE:{
                final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
                String[] items = new String[mySongs.size()];
                for (int i = 0; i < mySongs.size(); i++) {
                    items[i] = mySongs.get(i).getName().toString().replace(".mp3", "");
                }

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            startActivity(new Intent(getApplicationContext(), MusicActivity.class).putExtra("pos", i).putExtra("songlist", mySongs));
                        }
                    });
                }
                else {
                    Toast.makeText(MediaPlayer.this,"Agar nahi diya to gand maraa",Toast.LENGTH_LONG);
                }
            }
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}