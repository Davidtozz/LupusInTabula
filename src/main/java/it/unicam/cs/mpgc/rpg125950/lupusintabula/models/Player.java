package it.unicam.cs.mpgc.rpg125950.lupusintabula.models;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.PlayerRole;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.util.IEntityMarker;
import lombok.Data;

@Data
public class Player implements IEntityMarker {
    private final String name;
    private PlayerRole role;
    private boolean isAlive;

    public Player(String name) {
        this.name = name;
        this.isAlive = true; // Everyone starts alive
    }
}