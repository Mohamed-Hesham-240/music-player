package com.example.moomusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    public SongAdapter(Context context, List<Song> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song currentSong = getItem(position);
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView title = listItemView.findViewById(R.id.title);
        title.setText(currentSong.getTitle());
        TextView artist = listItemView.findViewById(R.id.artist);
        artist.setText(currentSong.getArtist());
        return listItemView;
    }
}
