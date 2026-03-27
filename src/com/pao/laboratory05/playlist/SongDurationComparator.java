package com.pao.laboratory05.playlist;
import java.util.Comparator;

public class SongDurationComparator implements Comparator<Song> {
    // compare: sortare după durationSeconds crescător
    @Override
    public int compare (Song song1, Song song2) {
        return Integer.compare(song1.durationSeconds(), song2.durationSeconds());
    }
}
