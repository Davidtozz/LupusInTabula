package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.*;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.Player;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;

@Log
public final class Game {
    @Getter
    private final ObservableList<Player> players;
    @Getter
    private SimpleObjectProperty<GamePhase> currentPhase;
    private WinConditionChecker checker;

    public Game() {
        this.players = FXCollections.observableArrayList();
        this.currentPhase = new SimpleObjectProperty<>(GamePhase.NIGHT_WOLVES);
        this.checker = new WinConditionChecker(players);

        log.info("Game started. Players: " + players.size());
    }

    public Game(ObservableList<Player> playerNames) {
        Objects.requireNonNull(playerNames);
        this.players = FXCollections.observableArrayList();
        players.addAll(playerNames);

        log.info("Game initialized with players: " + players);
    }

    public void advancePhase() {
        currentPhase.set(currentPhase.get().next());
    }
}