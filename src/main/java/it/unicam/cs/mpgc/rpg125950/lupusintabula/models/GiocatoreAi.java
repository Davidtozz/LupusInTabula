package it.unicam.cs.mpgc.rpg125950.lupusintabula.models;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.FaseGioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

@Log
public final class GiocatoreAi extends Giocatore {
    private final Set<String> giaIspezionati = new HashSet<>();
    private final Set<String> giaAttaccati = new HashSet<>();

    public GiocatoreAi(String nome, RuoloGiocatore ruolo) {
        super(nome, ruolo);
        log.info("Giocatore AI " + nome + " creato con ruolo " + ruolo);
    }

    public String eseguiAzione(List<Giocatore> bersagliPossibili, FaseGioco faseAttuale) {
        if (faseAttuale == FaseGioco.NOTTE_LUPI && this.isLupo()) {
            Giocatore bersaglio = ottieniBersaglio(bersagliPossibili,
                g -> !g.isLupo() && !giaAttaccati.contains(g.getNome()));
            if (bersaglio == null)
                bersaglio = ottieniBersaglio(bersagliPossibili, g -> !g.isLupo());
            if (bersaglio == null || !bersaglio.isVivo())
                return "";
            this.attaccaGiocatore(bersaglio);
            giaAttaccati.add(bersaglio.getNome());
            return bersaglio.getNome() + " è morto";
        } else if (faseAttuale == FaseGioco.NOTTE_VEGGENTE && this.isVeggente()) {
            Giocatore bersaglio = ottieniBersaglio(bersagliPossibili,
                g -> !giaIspezionati.contains(g.getNome()));
            if (bersaglio == null)
                bersaglio = ottieniBersaglio(bersagliPossibili);
            if (bersaglio == null) return "";
            this.ispezionaGiocatore(bersaglio);
            giaIspezionati.add(bersaglio.getNome());
            return this.getNome() + " sostiene che " + bersaglio.getNome() + " è un "+ bersaglio.getRuolo();
        }
        return "";
    }

    public Giocatore vota(List<Giocatore> bersagliPossibili) {
        Predicate<Giocatore> extraFilter = this.isLupo()
            ? g -> !g.isLupo()
            : Objects::nonNull;
        Giocatore bersaglio = ottieniBersaglio(bersagliPossibili, extraFilter);
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

    private Giocatore ottieniBersaglio(List<Giocatore> bersagliPossibili, Predicate<Giocatore> filtro) {
        var bersagliValidi = bersagliPossibili.stream()
                .filter(Giocatore::isVivo)
                .filter(g -> !g.equals(this))
                .filter(filtro)
                .toList();
        if (bersagliValidi.isEmpty()) return null;
        return bersagliValidi.get((int) (Math.random() * bersagliValidi.size()));
    }
}
