package it.unicam.cs.mpgc.rpg125950.lupusintabula.models;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.PlayerRole;
import lombok.Data;
import lombok.Getter;
import lombok.extern.java.Log;


@Log
public sealed class Player permits PlayerAi {
    @Getter
    private final String name;
    @Getter
    private final PlayerRole role;
    @Getter
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

    public void performAction(Player target) {
        if(!target.isAlive()) {
            throw new IllegalStateException("Player " + name + " is not alive.");
        }
        switch(this.role) {
            case PlayerRole.WOLF -> {
                target.isAlive = false; // Wolves kill the target
                log.info(name + " (Lupo) ha ucciso " + target.getName());
            }
            case PlayerRole.OVERSEER
                    -> log.info(name + " (Veggente) ha ispezionato " + target.getName() + " e ha scoperto che è un " + target.getRole());
            default -> {}
        }
    }

    @Override
    public String toString() {
        String aliveStatus = isAlive ? "in vita" : "morto";
        return name + " (" + role + "), " + aliveStatus;
    }
}