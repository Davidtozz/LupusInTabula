package it.unicam.cs.mpgc.rpg125950.lupusintabula.service;

import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.java.Log;

interface IStoricoAzioniService {
    void registraAzione(String azione);
}

@Log
public class StoricoAzioniService implements IStoricoAzioniService{
    private final ObservableList<String> azioniGioco;

    public StoricoAzioniService(ObservableList<String> azioniGioco) {
        this.azioniGioco = azioniGioco;
    }

    /**
     * @param azione l'azione da registrare nello storico delle azioni
     */
    @Override
    public void registraAzione(String azione) {
        log.info("Log azione: " + azione);
        azioniGioco.add(azione);
    }
}
