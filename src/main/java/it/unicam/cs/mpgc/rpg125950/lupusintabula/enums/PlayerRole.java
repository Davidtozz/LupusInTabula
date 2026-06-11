package it.unicam.cs.mpgc.rpg125950.lupusintabula.enums;

import java.util.*;

/**
 * Enumerazione dei ruoli dei giocatori in Lupus in Tabula.
 * Include una piccola utility per generare un ruolo casuale.
 * */
public enum PlayerRole {
    FARMER,
    WOLF,
    OVERSEER;

    private static final List<PlayerRole> values = List.of(PlayerRole.values());
    private static final Random r = new Random();

    public static PlayerRole getRandomRole()  {
        return values.get(r.nextInt(values.size()));
    }

    @Override
    public String toString() {
        return switch (this) {
            case FARMER -> "Farmer";
            case WOLF -> "Wolf";
            case OVERSEER -> "Overseer";
        };
    }
}