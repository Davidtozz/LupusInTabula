package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.PlayerRole;
import javafx.event.ActionEvent;
import lombok.extern.java.Log;

@FunctionalInterface
interface TargetPlayer {
    void targetPlayer(ActionEvent actionEvent);
}

@Log
public final class PlayerAi extends Player implements TargetPlayer {
    public PlayerAi(String name, PlayerRole role) {
        super(name, role);
        log.info("Player AI " + name + " created with role " + role);
    }

    /**
     * @param actionEvent
     */
    @Override
    public void targetPlayer(ActionEvent actionEvent) {
         log.info("Player AI " + getName() + " is targeting a player...");
    }
}
