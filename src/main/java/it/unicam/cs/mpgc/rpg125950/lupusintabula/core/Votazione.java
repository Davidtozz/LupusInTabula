package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
public final class Votazione {
    private final Map<Giocatore, Integer> voti;

    public Votazione(List<Giocatore> giocatoriVivi) {
        this.voti = new HashMap<>();
        for(Giocatore g :  giocatoriVivi){
            voti.put(g, 0);
        }
    }

    public void registraVoto(Giocatore bersaglio) {
        if (bersaglio != null && voti.containsKey(bersaglio)) {
            voti.put(bersaglio, voti.get(bersaglio) + 1);
            log.info("Voto registrato per " + bersaglio.getNome() + ". Voti totali: " + voti.get(bersaglio));
        }
    }

    public Giocatore mostraEsito() {
        return Collections.max(voti.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}