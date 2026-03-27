package com.pao.laboratory05.biblioteca;

import java.util.*;

import com.pao.laboratory05.playlist.Song;
import com.pao.laboratory05.playlist.SongDurationComparator;

public class BibliotecaService {
    
    private Carte[] carti;

    private BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class BibliotecaServiceHolder {
        private static final BibliotecaService instance = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return BibliotecaServiceHolder.instance;
    }

    void addCarte(Carte carte) {
        Carte[] tmp = new Carte[this.carti.length + 1];
        System.arraycopy(this.carti, 0, tmp, 0, this.carti.length);
        tmp[tmp.length - 1] = carte;
        this.carti = tmp;
    }

    void listSortedByRating() {
        Carte[] tmp = new Carte[this.carti.length];
        System.arraycopy(this.carti, 0, tmp, 0, this.carti.length);
        Arrays.sort(tmp); 
        System.out.println(Arrays.toString(tmp)); 
    }

    void listSortedBy(Comparator<Carte> comparator) {
        Carte[] tmp = new Carte[this.carti.length];
        System.arraycopy(this.carti, 0, tmp, 0, this.carti.length);
        Arrays.sort(tmp, comparator);
        System.out.println(Arrays.toString(tmp)); 
    }

}
