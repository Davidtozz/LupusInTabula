package it.unicam.cs.mpgc.rpg125950.lupusintabula.models;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.PlayerRole;
import lombok.extern.java.Log;

@Log
public final class PlayerAi extends Player{
    public PlayerAi(String name, PlayerRole role) {
        super(name, role);
        log.info("Player AI " + name + " created with role " + role);
    }
}
