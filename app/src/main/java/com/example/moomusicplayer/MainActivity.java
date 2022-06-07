package com.example.moomusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Song> songsList = new ArrayList<>();
    private static SongAdapter songAdapter;
    public static int ind = 0;
    private static boolean firstTime = true;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private void getSongList() {
        ContentResolver resolver = this.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int typeMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);
            do {
                try {
                    String title = cursor.getString(titleColumn);
                    String location = cursor.getString(dataColumn);
                    String artist = cursor.getString(artistColumn);
                    String isMusic = cursor.getString(typeMusic);
                    if (!title.startsWith("AUD") && isMusic.equalsIgnoreCase("1")) {
                        songsList.add(new Song(title, artist, location));
                    }
                } catch (Exception e) {
                    //do nothing (skip)
                }
            }
            while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        if (firstTime) {
            firstTime = false;
            getSongList();
        }
        Intent intent = new Intent(MainActivity.this, NowPlaying.class);
        songAdapter = new SongAdapter(this, songsList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NowPlaying.alreadyPlaying = false;
                ind = i;
                startActivity(intent);
            }
        });
        Button nowPlayingButton = findViewById(R.id.now_playing_button);
        nowPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

}