package com.pao.laboratory05.playlist;
import java.util.*;

public class Playlist {
    private String name;
    private Song[] songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new Song[0];
    }

    public String getName() {
        return this.name;
    }

    // private static int[] addElement(int[] arr, int value) {
    //     int[] tmp = new int[arr.length + 1];
    //     System.arraycopy(arr, 0, tmp, 0, arr.length);
    //     tmp[tmp.length - 1] = value;
    //     return tmp;
    // }
    void addSong(Song song) {
        Song[] tmp = new Song[this.songs.length + 1];
        System.arraycopy(this.songs, 0, tmp, 0, this.songs.length);
        tmp[tmp.length - 1] = song;
        this.songs = tmp;
    }

    void printSortedByTitle() {
        Song[] tmp = new Song[this.songs.length];
        System.arraycopy(this.songs, 0, tmp, 0, this.songs.length);
        Arrays.sort(tmp);
        System.out.println(Arrays.toString(tmp));
    }

    void printSortedByDuration() {
        Song[] tmp = new Song[this.songs.length];
        System.arraycopy(this.songs, 0, tmp, 0, this.songs.length);
        Arrays.sort(tmp, new SongDurationComparator()); 
        System.out.println(Arrays.toString(tmp));       
    }

    int getTotalDuration() {
        int suma = 0;
        for (Song s : this.songs) {
            suma += s.durationSeconds();
        }
        return suma;
    }
}
