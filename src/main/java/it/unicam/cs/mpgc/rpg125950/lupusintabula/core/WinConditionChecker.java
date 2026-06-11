package it.unicam.cs.mpgc.rpg125950.lupusintabula.core;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.VictoryResult;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
public final class WinConditionChecker {
    private final FilteredList<Player> wolves;
    private final SimpleIntegerProperty alivePlayersCount;
    @Getter
    private SimpleObjectProperty<VictoryResult> victoryResult;

    public WinConditionChecker(ObservableList<Player> players) {
        this.wolves = new FilteredList<>(players, Player::isWolf);
        this.alivePlayersCount = new SimpleIntegerProperty();
        this.victoryResult = new SimpleObjectProperty<>(VictoryResult.NOT_SATISFIED);

        players.addListener((ListChangeListener<Player>) change -> {
            int count = 0;
            for (Player p : players) {
                if (p.isAlive()) count++;
            }
            alivePlayersCount.set(count);
            log.info("Checking victory results...");
            checkForCondition();
        });
    }

    public void checkForCondition() {
        int wolvesCount = wolves.size();
        if (wolvesCount == 0) {
            log.info("All wolves are dead. Villagers win");
            this.victoryResult.set(VictoryResult.VILLAGERS_WIN);
        }
        if (wolvesCount >= (alivePlayersCount.get() - wolvesCount)) {
            log.info("Wolves are equal or more than the rest of the players. Wolves win");
            this.victoryResult.set(VictoryResult.WOLVES_WIN);
        }
    }
}
