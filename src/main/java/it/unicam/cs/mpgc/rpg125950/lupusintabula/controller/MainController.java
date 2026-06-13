package it.unicam.cs.mpgc.rpg125950.lupusintabula.controller;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.CondizioneVittoria;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Giocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Gioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.FaseGioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RisultatoVittoria;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.util.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Log
public class MainController implements Initializable {
    @FXML public Button inspectButton;
    @FXML public Button voteButton;
    @FXML public Button buttonAttacca;
    @FXML public Button createGameButton;
    @FXML public Button advancePhaseButton;
    @FXML public Label roleLabel;
    @FXML public Label phaseLabel;
    @FXML public Label statusLabel;
    @FXML public Label playerNameLabel;
    @FXML public ListView<Giocatore> playersList;
    @FXML public ListView<String> logList;
    @FXML public TextField playerNameField;
    @FXML public HBox playerActions;
    private Gioco gioco;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gioco = new Gioco();
        playersList.setItems(gioco.getGiocatori());
        phaseLabel.textProperty().bind(
            gioco.getFaseAttuale().asString("Fase attuale: %s")
        );
        logList.setItems(gioco.getStoricoAzioniGioco());
    }

    private void configuraCambioFase() {
        Giocatore giocatore = gioco.getGiocatoreUmano();

        gioco.getFaseAttuale().subscribe((phase) -> {
            boolean isNightWolvesPhase = phase == FaseGioco.NOTTE_LUPI;
            boolean isNightSeerPhase = phase == FaseGioco.NOTTE_VEGGENTE;
            boolean isDayVotingPhase = phase == FaseGioco.VOTAZIONE_GIORNO;

            buttonAttacca.setDisable(!giocatore.isLupo() || !isNightWolvesPhase);
            inspectButton.setDisable(!giocatore.isVeggente() || !isNightSeerPhase);
            voteButton.setDisable(!isDayVotingPhase);
        });
    }

    public void onCreaPartitaClick(ActionEvent evento) {
        gioco.iniziaPartita(playerNameField.getText());

        // Ottieni il giocatore umano e registra un listener sulla sua proprietà isVivo
        Giocatore human = gioco.getGiocatoreUmano();
        human.isVivoProperty().addListener((_, _, newVivo) -> {
            if (newVivo != null && !newVivo) {
                statusLabel.setText("Sei morto.");
                for(Node button : playerActions.getChildren()){
                    button.setDisable(!button.equals(advancePhaseButton));
                }
            }
        });

        CondizioneVittoria.getRisultatoVittoria().addListener((_, _, risultatoVittoria) -> {
            if (risultatoVittoria != RisultatoVittoria.NON_SODDISFATTO) {
                statusLabel.setText("Partita finita: " + risultatoVittoria.toString());
                for (Node button : playerActions.getChildren()) {
                    button.setDisable(true);
                }
                new Alert(Alert.AlertType.INFORMATION, risultatoVittoria.toString(), ButtonType.OK).showAndWait();
            }
        });

        var ruoloGiocatore = human.getRuolo();
        gioco.logAction("Gioco avviato. Sei un " + ruoloGiocatore + ".");
        roleLabel.setText("Il tuo ruolo: " + ruoloGiocatore.toString());

        configuraCambioFase();
        mostraLayoutDiGioco();
        mostraAzioniGiocatore(ruoloGiocatore);
    }

    public void onVotaClick(ActionEvent evento) {
        if(nessunGiocatoreSelezionato()) {
            new Alert(Alert.AlertType.ERROR, "Seleziona un giocatore da votare", ButtonType.OK).showAndWait();
            return;
        }

        var bersaglioDelGiocatore = playersList.getSelectionModel().getSelectedItem();
        gioco.avviaVotazione(bersaglioDelGiocatore);
        playersList.refresh();
    }
    
    public void onIspezionaClick(ActionEvent evento) {
        if(nessunGiocatoreSelezionato()) {
            new Alert(Alert.AlertType.ERROR, "Seleziona un giocatore) da ispezionare", ButtonType.OK).showAndWait();
            return;
        }

        var bersaglio = playersList.getSelectionModel().getSelectedItem();

        if(bersaglio != null) {
            gioco.getGiocatoreUmano()
                    .ispezionaGiocatore(bersaglio)
                    .ifPresentOrElse(
                            // Se sono un veggente:
                            ruolo -> gioco.logAction("(Visibile solo a te) " + bersaglio.getNome()+ " è un " + ruolo),
                            // Altrimenti:
                            () -> gioco.logAction("Non puoi ispezionare questo giocatore.")
                    );
        }

        gioco.eseguiAzioniAi();
        gioco.avanzaFase();
        playersList.refresh();
    }

    public void onAttaccoClick(ActionEvent evento) {
        if(nessunGiocatoreSelezionato()) {
            new Alert(Alert.AlertType.ERROR, "Seleziona un giocatore da attaccare", ButtonType.OK).showAndWait();
            return;
        }
        var giocatore = gioco.getGiocatoreUmano();
        var giocatoreBersaglio = playersList.getSelectionModel().getSelectedItem();

        if (giocatoreBersaglio == null || !giocatoreBersaglio.isVivo()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Seleziona un bersaglio valido", ButtonType.YES);
            alert.showAndWait();
            return;
        }

        Optional<Boolean> esito = giocatore.attaccaGiocatore(giocatoreBersaglio);
        if(esito.isPresent()) {
            gioco.logAction(giocatoreBersaglio.getNome() + " è stato ucciso da te.");

            gioco.eseguiAzioniAi();
            playersList.getSelectionModel().clearSelection();
            playersList.refresh();
            gioco.avanzaFase();
        }
    }

    public void onAvanzaFaseClick(ActionEvent evento) {
        gioco.eseguiAzioniAi();
        gioco.avanzaFase();
        log.info("Fase di gioco attuale: " + gioco.getFaseAttuale().toString());
        playersList.refresh();

    }

     private void mostraLayoutDiGioco() {
        ControllerUtils.mostraElementi(phaseLabel, playersList, logList, playerActions);
        ControllerUtils.nascondiElementi(createGameButton);
    }

    private void mostraAzioniGiocatore(RuoloGiocatore role) {
        switch (role) {
            case RuoloGiocatore.LUPO, RuoloGiocatore.CONTADINO -> ControllerUtils.nascondiElementi(inspectButton);
            case RuoloGiocatore.VEGGENTE -> ControllerUtils.nascondiElementi(buttonAttacca);
        }
    }

    /**
     * @return true se non è stato selezionato alcun giocatore nella lista, false altrimenti
     * */
    private boolean nessunGiocatoreSelezionato() {
        return playersList.getSelectionModel().isEmpty();
    }

}