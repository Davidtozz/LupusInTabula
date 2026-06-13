package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
public final class Gioco {
    @Getter
    private final ObservableList<Giocatore> giocatori;
    @Getter
    private SimpleObjectProperty<FaseGioco> faseAttuale;
    @Getter
    private ObservableList<String> storicoAzioniGioco;

    public Gioco() {
        this.giocatori = FXCollections.observableArrayList();
        this.faseAttuale = new SimpleObjectProperty<>();
        this.storicoAzioniGioco = FXCollections.observableArrayList();

        log.info("Gioco avviato. Giocatori: " + giocatori.size());
    }

    public Giocatore getGiocatoreUmano(){
        return this.giocatori.getFirst();
    }
}