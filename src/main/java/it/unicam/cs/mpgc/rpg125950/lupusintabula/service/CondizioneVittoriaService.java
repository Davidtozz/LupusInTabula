package it.unicam.cs.mpgc.rpg125950.lupusintabula.service;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Giocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RisultatoVittoria;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.List;

interface ICondizioneVittoriaService {
    void controllaCondizione(List<Giocatore> giocatori);
}

@Log
public final class CondizioneVittoriaService implements ICondizioneVittoriaService {
    @Getter
    private final SimpleObjectProperty<RisultatoVittoria> risultatoVittoria = new SimpleObjectProperty<>(RisultatoVittoria.NON_SODDISFATTO);

    @Override
    public void controllaCondizione(List<Giocatore> giocatori) {
        long lupiVivi = giocatori.stream().filter(g -> g.isVivo() && g.isLupo()).count();
        long viviTotali = giocatori.stream().filter(Giocatore::isVivo).count();

        if (lupiVivi == 0) {
            risultatoVittoria.set(RisultatoVittoria.VITTORIA_CONTADINI);
            log.info("I contadini vincono.");
        } else if (lupiVivi >= viviTotali - lupiVivi) {
            risultatoVittoria.set(RisultatoVittoria.VITTORIA_LUPI);
            log.info("I lupi vincono.");
        }
    }
}
