package it.unicam.cs.mpgc.rpg125950.lupusintabula.controller;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Game;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.PlayerRole;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.Player;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.PlayerAi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ResourceBundle;

@Log
public class MainController implements Initializable {
    @FXML public Button inspectButton;
    @FXML public Button advanceAiButton;
    @FXML public Button voteButton;
    @FXML public Button wolvesAttackButton;
    @FXML public ListView<Player> playersList;
    @FXML public Label roleLabel;
    @FXML public Label phaseLabel;
    @FXML public TextField playerNameField;
    @FXML public ListView<String> logList;
    @FXML public Label statusLabel;
    @FXML public Button createGameButton;
    @FXML public Button advancePhaseButton;
    private Game game;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new Game();
        playersList.setItems(game.getPlayers());
        phaseLabel.textProperty().bind(
            game.getCurrentPhase().asString("Fase attuale: %s")
        );

    }

    public void onCreateGameClick(ActionEvent actionEvent) {
        Player humanPlayer = new Player(playerNameField.getText(), PlayerRole.getRandomRole());

        // Utilizziamo una variabile ausiliare per evitare un loop infinito,
        // dato che stiamo aggiungendo elementi alla lista durante l'iterazione
        final int aiPlayersCount = 5;
        for (int i = 0; i < aiPlayersCount; i++) {
            var role = PlayerRole.getRandomRole();
            if (role == PlayerRole.WOLF && humanPlayer.getRole() != PlayerRole.WOLF) {
                game.getPlayers().add(new PlayerAi("Giocatore " + (i + 1), role));
            } else if (role == PlayerRole.OVERSEER && humanPlayer.getRole() != PlayerRole.OVERSEER) {
                game.getPlayers().add(new PlayerAi("Giocatore " + (i + 1), role));
            } else {
                game.getPlayers().add(new PlayerAi("Giocatore " + (i + 1), role));
            }
            log.info("Player AI " + (i + 1) + " created with role " + role);
        }
        createGameButton.setDisable(true);
    }

    public void onVoteClick(ActionEvent actionEvent) {
    }

    public void onAdvanceAiClick(ActionEvent actionEvent) {
    }

    public void onInspectClick(ActionEvent actionEvent) {
    }

    public void onWolvesAttackClick(ActionEvent actionEvent) {
    }

    public void onAdvancePhaseClick(ActionEvent actionEvent) {
        log.info("Advancing game phase..." + phaseLabel.getText());
        game.advancePhase();
        log.info("Current game phase: " + game.getCurrentPhase().toString());
    }
}
