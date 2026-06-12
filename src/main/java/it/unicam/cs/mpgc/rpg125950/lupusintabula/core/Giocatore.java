package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Objects;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;


@Log
public sealed class Giocatore permits GiocatoreAi {
    @Getter
    private final String nome;
    @Getter
    private final RuoloGiocatore ruolo;
    // Use a JavaFX property so callers can listen to changes in the alive state
    private final SimpleBooleanProperty isVivo = new SimpleBooleanProperty(true);

    public Giocatore(String nome, RuoloGiocatore ruolo) {
        this.nome = nome;
        this.ruolo = ruolo;
        // everyone starts alive (property already initialized to true)

        log.info("Giocatore " + nome + " creato.");
    }

    public boolean isLupo(){
        return this.ruolo == RuoloGiocatore.LUPO;
    }

    public boolean isVeggente(){
        return this.ruolo == RuoloGiocatore.VEGGENTE;
    }


    @Override
    public String toString() {
        String aliveStatus = isVivo() ? "in vita" : "morto";
        return nome + " (" + ruolo + "), " + aliveStatus;
    }

    /**
     * Permette a un Veggente di ispezionare un altro giocatore, verificando se quest'ultimo è un Lupo
     * @param bersaglio il giocatore da ispezionare
     * @return true se il giocatore target è un Lupo, false altrimenti (o se Player non è un Veggente)
     **/
    public Optional<RuoloGiocatore> ispezionaGiocatore(Giocatore bersaglio) throws UnsupportedOperationException {
        Objects.requireNonNull(bersaglio, "Il bersaglio non può essere null");
        if(this.ruolo == RuoloGiocatore.VEGGENTE) {
            return Optional.of(bersaglio.getRuolo());
        }
        return Optional.empty();
    }

    /**
     * Esegue un attacco su un giocatore, uccidendolo.
     * @param bersaglio Il giocatore bersaglio dell'attacco
     * @return true se l'attacco è andato a buon fine, false altrimenti
     **/
    public Optional<Boolean> attaccaGiocatore(Giocatore bersaglio){
        Objects.requireNonNull(bersaglio, "Il bersaglio non può essere null");
        if(this.getRuolo() == RuoloGiocatore.LUPO){
            if(!bersaglio.isVivo()) return Optional.of(false); // non puoi attaccare un giocatore già morto
            bersaglio.setVivo(false);
            return Optional.of(true);
        }
        return Optional.empty();
    }

    /**
     * Compatibility methods for existing code: keep the same method names used across the project
     * but backed by a JavaFX property so other classes can listen for changes.
     */
    public boolean isVivo() {
        return isVivo.get();
    }

    public void setVivo(boolean vivo) {
        this.isVivo.set(vivo);
    }

    public SimpleBooleanProperty isVivoProperty() {
        return isVivo;
    }
}