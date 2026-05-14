package it.unicam.cs.mpgc.rpg125950.lupusintabula.models;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.*;
import lombok.Getter;
import java.util.*;
import java.util.stream.Collectors;

public final class Game {
    @Getter
    private final List<Player> players;
    @Getter
    private GamePhase currentPhase;
    private Player lastPlayerKilledByWolves;

    public Game(List<String> playerNames) {
        Objects.requireNonNull(playerNames);
        this.players = new ArrayList<>(playerNames.size());
        for (String playerName : playerNames)
            players.add(new Player(playerName));
        this.currentPhase = GamePhase.NIGHT_WOLVES;
    }

    public List<Player> getLivingWolves() {
        return this.players.stream()
                .filter(p -> p.getRole() == PlayerRole.LUPO)
                .collect(Collectors.toList());
    }

    public Optional<Player> getLivingOverseer(){
        return this.players.stream()
                .filter(p -> p.getRole() == PlayerRole.OVERSEER)
                .findFirst();
    }

    public List<Player> getLivingVillagers() {
        return this.players.stream()
                .filter(p -> p.getRole() == PlayerRole.CONTADINO)
                .toList();
    }

    public VictoryResult checkForWinCondition() {
        int wolvesCount = getLivingWolves().size();
        int villagersCount = this.players.size() - wolvesCount;

        if (wolvesCount == 0) return VictoryResult.VILLAGERS_WIN;
        if (wolvesCount >= villagersCount) return VictoryResult.WOLVES_WIN;
        return VictoryResult.NOT_SATISFIED;
    }
}