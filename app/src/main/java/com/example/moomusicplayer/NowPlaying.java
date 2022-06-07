package com.example.moomusicplayer;


import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class NowPlaying extends AppCompatActivity {
    private static MediaPlayer mediaPlayer;
    private boolean isPlaying = true;
    private TextView title;
    private TextView duration;
    private TextView progress;
    private String location;
    private SeekBar seekBar;
    private final Handler handler = new Handler();
    public static boolean alreadyPlaying;
    private static int previousIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        if (previousIndex != MainActivity.ind) {
            previousIndex = MainActivity.ind;
        } else {
            alreadyPlaying = true;
        }
        title = findViewById(R.id.scroll_title);
        title.setSelected(true);
        title.setText(MainActivity.songsList.get(MainActivity.ind).getTitle());
        if (!alreadyPlaying) {
            alreadyPlaying = true;
            releaseMediaPlayer();
            location = MainActivity.songsList.get(MainActivity.ind).getLocation();
            mediaPlayer = MediaPlayer.create(this, Uri.parse(location));
        }
        seekBar = findViewById(R.id.seek_bar);
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        duration = findViewById(R.id.duration);
        progress = findViewById(R.id.progress);
        int iMin = (mediaPlayer.getDuration() / 1000) / 60;
        int iSec = (mediaPlayer.getDuration() / 1000) % 60;
        duration.setText(getString(R.string.time_format, formatTime(iMin), formatTime(iSec)));
        NowPlaying.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    int iMin = (mediaPlayer.getCurrentPosition() / 1000) / 60;
                    int iSec = (mediaPlayer.getCurrentPosition() / 1000) % 60;
                    progress.setText(getString(R.string.time_format, formatTime(iMin), formatTime(iSec)));
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mediaPlayer.start();
        ImageView play_pause = findViewById(R.id.play_pause);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    isPlaying = false;
                    play_pause.setImageResource(R.drawable.outline_play_circle_black_48);
                    mediaPlayer.pause();
                } else {
                    isPlaying = true;
                    play_pause.setImageResource(R.drawable.outline_pause_circle_black_48);
                    mediaPlayer.start();
                }
            }
        });
        ImageView next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.ind + 1 == MainActivity.songsList.size())
                    MainActivity.ind = 0;
                else
                    ++MainActivity.ind;
                update(MainActivity.ind);
            }
        });
        mediaPlayer.setOnCompletionListener(myOnCompletionListener());
        ImageView previous = findViewById(R.id.back);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.ind == 0)
                    MainActivity.ind = MainActivity.songsList.size() - 1;
                else
                    --MainActivity.ind;
                update(MainActivity.ind);
            }
        });
    }

    private MediaPlayer.OnCompletionListener myOnCompletionListener() {
        return new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (MainActivity.ind + 1 == MainActivity.songsList.size())
                    MainActivity.ind = 0;
                else
                    ++MainActivity.ind;
                update(MainActivity.ind);
            }
        };
    }

    public static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void update(int ind) {
        releaseMediaPlayer();
        title.setText(MainActivity.songsList.get(ind).getTitle());
        isPlaying = true;
        String location = MainActivity.songsList.get(ind).getLocation();
        mediaPlayer = MediaPlayer.create(NowPlaying.this, Uri.parse(location));
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        int iMin = (mediaPlayer.getDuration() / 1000) / 60;
        int iSec = (mediaPlayer.getDuration() / 1000) % 60;
        duration.setText(getString(R.string.time_format, formatTime(iMin), formatTime(iSec)));
        NowPlaying.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });
        mediaPlayer.setOnCompletionListener(myOnCompletionListener());
        mediaPlayer.start();
    }

    private String formatTime(int iTime) {
        if (iTime < 10)
            return "0" + iTime;
        else
            return Integer.toString(iTime);
    }
}