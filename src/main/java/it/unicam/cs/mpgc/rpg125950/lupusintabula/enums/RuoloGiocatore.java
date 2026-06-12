package it.unicam.cs.mpgc.rpg125950.lupusintabula.enums;

import java.util.*;

/**
 * Enumerazione dei ruoli dei giocatori in Lupus in Tabula.
 * Include una piccola utility per generare un ruolo casuale.
 * */
public enum RuoloGiocatore {
    CONTADINO,
    LUPO,
    VEGGENTE;

    private static final List<RuoloGiocatore> values = List.of(RuoloGiocatore.values());
    private static final Random r = new Random();

    public static RuoloGiocatore getRandomRole()  {
        return values.get(r.nextInt(values.size()));
    }

    @Override
    public String toString() {
        return switch (this) {
            case CONTADINO -> "Contadino";
            case LUPO -> "Lupo";
            case VEGGENTE -> "Veggente";
        };
    }
}