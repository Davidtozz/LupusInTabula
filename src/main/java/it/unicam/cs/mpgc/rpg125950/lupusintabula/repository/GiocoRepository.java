package it.unicam.cs.mpgc.rpg125950.lupusintabula.repository;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Gioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.service.GiocoService;

interface IGiocoRepository {
    void salvaDatiPartita(Gioco gioco);
}

public class GiocoRepository implements IGiocoRepository {
    private GiocoService giocoService;

    public GiocoRepository(GiocoService giocoService) {
        this.giocoService = giocoService;
    }

    @Override
    public void salvaDatiPartita(Gioco gioco) {
        // codice per salvare la partita con i dati del gioco
        // usando XPath.
    }
}
