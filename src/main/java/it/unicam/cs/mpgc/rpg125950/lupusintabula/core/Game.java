package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.*;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
public final class Game {
    @Getter
    private final ObservableList<Player> players;
    @Getter
    private SimpleObjectProperty<GamePhase> currentPhase;
    private WinConditionChecker checker;
    @Getter
    private ObservableList<String> playerActionHistory;

    public Game() {
        this.players = FXCollections.observableArrayList();
        this.currentPhase = new SimpleObjectProperty<>();
        this.checker = new WinConditionChecker(players);
        this.playerActionHistory = FXCollections.observableArrayList();

        log.info("Game started. Players: " + players.size());
    }

    public void startGame(String humanPlayerName) {
        final Player humanPlayer = new Player(humanPlayerName, PlayerRole.getRandomRole());
        this.players.add(humanPlayer);

        // Tengo traccia delle figure speciali
        int allowedWolves = (humanPlayer.getRole() == PlayerRole.WOLF) ? 1 : 2;
        int allowedOverseers = (humanPlayer.getRole() == PlayerRole.OVERSEER) ? 0 : 1;


        final int PLAYER_AI_COUNT = 5;
        for (int i = 0; i < PLAYER_AI_COUNT; i++) {
            PlayerRole role = PlayerRole.getRandomRole();

            if (role == PlayerRole.WOLF) {
                if (allowedWolves > 0) {
                    allowedWolves--;
                } else {
                    role = PlayerRole.FARMER;
                }
            } else if (role == PlayerRole.OVERSEER) {
                if (allowedOverseers > 0) {
                    allowedOverseers--;
                } else {
                    role = PlayerRole.FARMER;
                }
            }

            this.getPlayers().add(new PlayerAi("Giocatore " + (i + 1), role));
            log.info("Player AI " + (i + 1) + " created with role " + role);
        }
        this.currentPhase.set(GamePhase.NIGHT_WOLVES);
    }

    public void advancePhase() {
        currentPhase.set(currentPhase.get().next());
    }

    public void logAction(String action) {
        log.info("Logging action: " + action);
        this.getPlayerActionHistory().add(action);
    }

    public Player getHumanPlayer(){
        return this.getPlayers().getFirst();
    }
}