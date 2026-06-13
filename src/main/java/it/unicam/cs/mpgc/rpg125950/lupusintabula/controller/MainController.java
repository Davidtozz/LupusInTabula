package it.unicam.cs.mpgc.rpg125950.lupusintabula.controller;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.CondizioneVittoria;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Giocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Gioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.FaseGioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RisultatoVittoria;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.util.ControllerUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML public Button avviaGiocoButton;
    @FXML public Button avanzaFaseButton;
    @FXML public Label roleLabel;
    @FXML public Label phaseLabel;
    @FXML public Label statusLabel;
    @FXML public Label playerNameLabel;
    @FXML public ListView<Giocatore> playersList;
    @FXML public ListView<String> logList;
    @FXML public TextField playerNameField;
    @FXML public HBox playerActions;
    @FXML public HBox startPanel;
    private Gioco gioco;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gioco = new Gioco();
        playersList.setItems(gioco.getGiocatori());
        phaseLabel.textProperty().bind(
            gioco.getFaseAttuale().asString("Fase attuale: %s")
        );
        logList.setItems(gioco.getStoricoAzioniGioco());

        CondizioneVittoria.getRisultatoVittoria().addListener((_, _, result) -> {
            if (result != RisultatoVittoria.NON_SODDISFATTO) {
                String msg = result == RisultatoVittoria.VITTORIA_CONTADINI
                    ? "I Contadini vincono!" : "I Lupi vincono!";
                ControllerUtils.disabilitaTuttiEccetto(playerActions, avanzaFaseButton);
                ControllerUtils.mostraAlertInformazione(msg);
                Platform.runLater(this::resetToLobby);
            }
        });
    }

    private void resetToLobby() {
        CondizioneVittoria.getRisultatoVittoria().set(RisultatoVittoria.NON_SODDISFATTO);
        gioco = new Gioco();
        playersList.setItems(gioco.getGiocatori());
        phaseLabel.textProperty().bind(
            gioco.getFaseAttuale().asString("Fase attuale: %s")
        );
        logList.setItems(gioco.getStoricoAzioniGioco());
        ControllerUtils.nascondiElementi(phaseLabel, playersList, logList, playerActions);
        ControllerUtils.mostraElementi(startPanel);
        roleLabel.setText("");
        statusLabel.setText("");
        playerNameField.setText("Tu");
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
                ControllerUtils.disabilitaTuttiEccetto(playerActions, avanzaFaseButton);
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
        if(ControllerUtils.nessunGiocatoreSelezionato(playersList)) {
            ControllerUtils.mostraAlertErrore("Seleziona un giocatore da votare");
            return;
        }

        var bersaglioDelGiocatore = playersList.getSelectionModel().getSelectedItem();
        gioco.avviaVotazione(bersaglioDelGiocatore);
        playersList.refresh();
    }
    
    public void onIspezionaClick(ActionEvent evento) {
        if(ControllerUtils.nessunGiocatoreSelezionato(playersList)) {
            ControllerUtils.mostraAlertErrore("Seleziona un giocatore da ispezionare");
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
        if(ControllerUtils.nessunGiocatoreSelezionato(playersList)) {
            ControllerUtils.mostraAlertErrore("Seleziona un giocatore da attaccare");
            return;
        }
        var giocatore = gioco.getGiocatoreUmano();
        var giocatoreBersaglio = playersList.getSelectionModel().getSelectedItem();

        if (giocatoreBersaglio == null || !giocatoreBersaglio.isVivo()) {
            ControllerUtils.mostraAlertErrore("Seleziona un bersaglio valido");
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
        ControllerUtils.nascondiElementi(startPanel);
    }

    private void mostraAzioniGiocatore(RuoloGiocatore role) {
        ControllerUtils.mostraElementi(voteButton, buttonAttacca, inspectButton);
        switch (role) {
            case RuoloGiocatore.LUPO, RuoloGiocatore.CONTADINO -> ControllerUtils.nascondiERimuoviElementi(inspectButton);
            case RuoloGiocatore.VEGGENTE -> ControllerUtils.nascondiERimuoviElementi(buttonAttacca);
        }
    }

}