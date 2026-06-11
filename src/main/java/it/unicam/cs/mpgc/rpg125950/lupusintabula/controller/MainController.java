package it.unicam.cs.mpgc.rpg125950.lupusintabula.controller;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Game;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.GamePhase;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.PlayerRole;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Player;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.util.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ResourceBundle;

@Log
public class MainController implements Initializable {
    @FXML public Button inspectButton;
    @FXML public Button voteButton;
    @FXML public Button wolvesAttackButton;
    @FXML public Button createGameButton;
    @FXML public Button advancePhaseButton;
    @FXML public Label roleLabel;
    @FXML public Label phaseLabel;
    @FXML public Label statusLabel;
    @FXML public Label playerNameLabel;
    @FXML public ListView<Player> playersList;
    @FXML public ListView<String> logList;
    @FXML public TextField playerNameField;
    @FXML public HBox playerActions;
    private Game game;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new Game();
        playersList.setItems(game.getPlayers());
        phaseLabel.textProperty().bind(
            game.getCurrentPhase().asString("Fase attuale: %s")
        );
        logList.setItems(game.getPlayerActionHistory());
    }

    public void onCreateGameClick(ActionEvent actionEvent) {
        game.startGame(playerNameField.getText());

        var playerRole = game.getHumanPlayer().getRole();
        game.logAction("Game started. You are a " + playerRole + ".");
        roleLabel.setText("Il tuo ruolo: " + playerRole.toString());

        setupPhaseChangeListener();
        showGameLayout();
        showPlayerActions(playerRole);
    }

    private void showGameLayout() {
        ControllerUtils.showElements(phaseLabel, playersList, logList, playerActions);
        ControllerUtils.hideElements(createGameButton);
    }

    private void setupPhaseChangeListener() {
        Player player = game.getHumanPlayer();

        game.getCurrentPhase().subscribe((phase) -> {
            boolean isNightWolvesPhase = phase == GamePhase.NIGHT_WOLVES;
            boolean isNightSeerPhase = phase == GamePhase.NIGHT_SEER;
            boolean isDayVotingPhase = phase == GamePhase.DAY_VOTING;

            wolvesAttackButton.setDisable(!player.isWolf() || !isNightWolvesPhase);
            inspectButton.setDisable(!player.isOverseer() || !isNightSeerPhase);
            voteButton.setDisable(!isDayVotingPhase);
        });
    }

    public void onVoteClick(ActionEvent actionEvent) {
    }
    
    public void onInspectClick(ActionEvent actionEvent) {
    }

    public void onWolvesAttackClick(ActionEvent actionEvent) {
        var player = game.getHumanPlayer();
        if(player.isWolf()) {
            log.info("Player " + player.getName() + " is attacking...");
            // Qui dovresti implementare la logica per selezionare un bersaglio e attaccarlo
            var target = playersList.getSelectionModel().getSelectedItem();
            if(target != null && target.isAlive()) {
                target.setAlive(false);
                game.getPlayerActionHistory().add("Player " + target.getName() + " has been attacked and is now dead.");
            }
        } else {
            log.warning("Player " + player.getName() + " is not a wolf and cannot attack.");
        }
    }

    public void onAdvancePhaseClick(ActionEvent actionEvent) {
        log.info("Advancing game phase..." + phaseLabel.getText());
        game.advancePhase();
        log.info("Current game phase: " + game.getCurrentPhase().toString());
    }

    private void showPlayerActions(PlayerRole role) {
        switch (role) {
            case PlayerRole.WOLF, PlayerRole.FARMER -> ControllerUtils.hideElements(inspectButton);
            case PlayerRole.OVERSEER -> ControllerUtils.hideElements(wolvesAttackButton);
        }
    }
}