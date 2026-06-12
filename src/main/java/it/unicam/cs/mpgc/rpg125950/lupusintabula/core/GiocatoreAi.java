package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.FaseGioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Log
public final class GiocatoreAi extends Giocatore {
    public GiocatoreAi(String nome, RuoloGiocatore ruolo) {
        super(nome, ruolo);
        log.info("Giocatore AI " + nome + " creato con ruolo " + ruolo);
    }

    public String eseguiAzione(ObservableList<Giocatore> bersagliPossibili, FaseGioco faseAttuale) {
        if (faseAttuale == FaseGioco.NOTTE_LUPI && this.isLupo()) {
            Giocatore bersaglio = ottieniBersaglio(bersagliPossibili, giocatore -> !giocatore.isLupo());
            if (bersaglio == null || !bersaglio.isVivo())
                return "";
            this.attaccaGiocatore(bersaglio);
            return bersaglio.getNome() + " è morto";
        } else if (faseAttuale == FaseGioco.NOTTE_VEGGENTE && this.isVeggente()) {
            Giocatore bersaglio = ottieniBersaglio(bersagliPossibili);
            if (bersaglio == null) return "";
            this.ispezionaGiocatore(bersaglio);
            return this.getNome() + " sostiene che " + bersaglio.getNome() + " è un "+ bersaglio.getRuolo();
        }
        return "";
    }

    public Giocatore vota(List<Giocatore> bersagliPossibili) {
        Giocatore bersaglio = ottieniBersaglio(bersagliPossibili);
        if (bersaglio == null) {
            log.warning(this.getNome() + " non può votare - nessun bersaglio valido");
            return null;
        }
        log.info(this.getNome() + " vota per eliminare " + bersaglio.getNome());
        return bersaglio;
    }

    private Giocatore ottieniBersaglio(List<Giocatore> bersagliPossibili) {
        return ottieniBersaglio(bersagliPossibili, Objects::nonNull);
    }

    private Giocatore ottieniBersaglio(List<Giocatore> bersagliPossibili, Predicate<Giocatore> extraFilter) {
        var bersagliValidi = bersagliPossibili.stream()
                .filter(g -> !g.equals(this))
                .filter(extraFilter)
                .toList();
        if (bersagliValidi.isEmpty()) return null;
        return bersagliValidi.get((int) (Math.random() * bersagliValidi.size()));
    }
}
