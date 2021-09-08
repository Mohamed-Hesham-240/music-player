package com.example.moomusicplayer;

public class Song {
    private String title;
    private String location;
    private String artist;

    public Song(String title, String artist, String location) {
        this.title = title;
        this.artist = artist;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getArtist() {
        return artist;
    }
}
