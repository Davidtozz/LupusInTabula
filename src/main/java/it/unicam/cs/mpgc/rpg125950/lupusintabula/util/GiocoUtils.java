package it.unicam.cs.mpgc.rpg125950.lupusintabula.util;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.GiocatoreAi;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

@Log
public final class GiocoUtils {
    public static List<GiocatoreAi> generaGiocatoriAi(int numero, RuoloGiocatore ruoloGiocatore){
        List<GiocatoreAi> giocatoriAi = new ArrayList<>();
        int totale = 1 + numero; // '1' è il giocatore umano
        int maxLupi = (totale > 5) ? 2 : 1;
        int lupiConsentiti = (ruoloGiocatore == RuoloGiocatore.LUPO) ? maxLupi - 1 : maxLupi;
        int veggentiConsentiti = (ruoloGiocatore == RuoloGiocatore.VEGGENTE) ? 0 : 1;

        for (int i = 0; i < numero; i++) {
            RuoloGiocatore ruolo = RuoloGiocatore.getRandomRole();

            if (ruolo == RuoloGiocatore.LUPO) {
                if (lupiConsentiti > 0) {
                    lupiConsentiti--;
                } else {
                    ruolo = RuoloGiocatore.CONTADINO;
                }
            } else if (ruolo == RuoloGiocatore.VEGGENTE) {
                if (veggentiConsentiti > 0) {
                    veggentiConsentiti--;
                } else {
                    ruolo = RuoloGiocatore.CONTADINO;
                }
            }

            giocatoriAi.add(new GiocatoreAi("Giocatore " + (i + 1), ruolo));
            log.info("Giocatore AI " + (i + 1) + " creato con ruolo " + ruolo);
        }
        return giocatoriAi;
    }
}