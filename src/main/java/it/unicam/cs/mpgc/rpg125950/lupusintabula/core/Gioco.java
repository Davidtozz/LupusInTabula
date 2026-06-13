package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.*;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.util.GiocoUtils;
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

    public void iniziaPartita(String nomeGiocatoreUmano) {
        final Giocatore giocatoreUmano = new Giocatore(nomeGiocatoreUmano, RuoloGiocatore.getRandomRole());
        this.giocatori.add(giocatoreUmano);

        final int PLAYER_AI_COUNT = 5;
        this.giocatori.addAll(GiocoUtils.generaGiocatoriAi(PLAYER_AI_COUNT, giocatoreUmano.getRuolo()));
        this.faseAttuale.set(FaseGioco.NOTTE_LUPI);
    }

    public void avanzaFase() {
        faseAttuale.set(faseAttuale.get().next());
    }

    public void logAction(String action) {
        log.info("Log azione: " + action);
        this.getStoricoAzioniGioco().add(action);
    }

    public void eseguiAzioniAi() {
        var giocatoriRimasti = giocatori.stream().filter(Giocatore::isVivo).toList();
        for(Giocatore g : giocatoriRimasti) {
            if(!(g instanceof GiocatoreAi ai)) continue;
            String logAzioneEseguita = ai.eseguiAzione(giocatoriRimasti, this.faseAttuale.get());
            if (!logAzioneEseguita.isEmpty()) {
                this.logAction(logAzioneEseguita);
            }
        }
        CondizioneVittoria.controllaCondizione(giocatoriRimasti);
    }

    public void avviaVotazione(Giocatore bersaglioGiocatoreUmano) {
        var giocatoriVivi = giocatori.stream().filter(Giocatore::isVivo).toList();
        Votazione votazione = new Votazione(giocatoriVivi);

        votazione.registraVoto(bersaglioGiocatoreUmano);

        giocatoriVivi.stream()
            .filter(g -> g instanceof GiocatoreAi)
            .forEach(ai -> {
                Giocatore bersaglio = ((GiocatoreAi)ai).vota(giocatoriVivi);
                if (bersaglio != null) {
                    votazione.registraVoto(bersaglio);
                }
            });
        Giocatore giocatoreOggettoVotazione = votazione.mostraEsito();
        giocatoreOggettoVotazione.setVivo(false);

        this.logAction("Il villaggio ha deciso: " + giocatoreOggettoVotazione.getNome() + " è stato linciato!");
        CondizioneVittoria.controllaCondizione(giocatoriVivi);
        this.avanzaFase();
    }

    public Giocatore getGiocatoreUmano(){
        return this.getGiocatori().getFirst();
    }
}