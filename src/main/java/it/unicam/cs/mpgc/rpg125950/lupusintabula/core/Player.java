package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.PlayerRole;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;


@Log
public sealed class Player permits PlayerAi {
    @Getter
    private final String name;
    @Getter
    private final PlayerRole role;
    @Getter
    @Setter
    private boolean isAlive;

    public Player(String name, PlayerRole role) {
        this.name = name;
        this.role = role;
        this.isAlive = true; // Everyone starts alive

        log.info("Player " + name + " created.");
    }

    public boolean isWolf(){
        return this.role == PlayerRole.WOLF;
    }

    public boolean isOverseer(){
        return this.role == PlayerRole.OVERSEER;
    }


    @Override
    public String toString() {
        String aliveStatus = isAlive ? "in vita" : "morto";
        return name + " (" + role + "), " + aliveStatus;
    }
}