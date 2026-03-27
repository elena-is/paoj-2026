package com.pao.laboratory05.biblioteca;
import java.util.*;

public class CarteAutorComparator implements Comparator<Carte> {
    
    @Override
    public int compare(Carte c1, Carte c2) {
        return c1.getTitlu().compareTo(c2.getTitlu());
    }
}
