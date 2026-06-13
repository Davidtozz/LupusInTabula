package it.unicam.cs.mpgc.rpg125950.lupusintabula.controller;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Giocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Gioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.FaseGioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RisultatoVittoria;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.service.GiocoService;
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
    private GiocoService giocoService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLobby();
    }

    private void initLobby() {
        this.giocoService = new GiocoService(new Gioco());
        var gioco = this.giocoService.getGioco();
        playersList.setItems(gioco.getGiocatori());
        phaseLabel.textProperty().bind(
            gioco.getFaseAttuale().asString("Fase attuale: %s")
        );
        logList.setItems(gioco.getStoricoAzioniGioco());

        this.giocoService.getCondizioneVittoriaService().getRisultatoVittoria().addListener((_, _, result) -> {
            if (result != RisultatoVittoria.NON_SODDISFATTO) {
                String msg = result == RisultatoVittoria.VITTORIA_CONTADINI
                    ? "I Contadini vincono!" : "I Lupi vincono!";
                ControllerUtils.disabilitaTuttiEccetto(playerActions, avanzaFaseButton);
                ControllerUtils.mostraAlertInformazione(msg);
                Platform.runLater(this::initLobby);
            }
        });

        ControllerUtils.nascondiElementi(phaseLabel, playersList, logList, playerActions);
        ControllerUtils.mostraElementi(startPanel);
        roleLabel.setText("");
        statusLabel.setText("");
        playerNameField.setText("Tu");
    }

    private void configuraCambioFase() {
        var gioco = this.giocoService.getGioco();
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
        giocoService.iniziaPartita(playerNameField.getText());
        var gioco = this.giocoService.getGioco();

        Giocatore human = gioco.getGiocatoreUmano();
        human.isVivoProperty().addListener((_, _, newVivo) -> {
            if (newVivo != null && !newVivo) {
                statusLabel.setText("Sei morto.");
                ControllerUtils.disabilitaTuttiEccetto(playerActions, avanzaFaseButton);
            }
        });

        var ruoloGiocatore = human.getRuolo();
        this.giocoService.getStoricoAzioniService().registraAzione("Gioco avviato. Sei un " + ruoloGiocatore + ".");
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
        this.giocoService.getVotazioneService().avviaVotazione(bersaglioDelGiocatore);
        playersList.refresh();
    }

    public void onIspezionaClick(ActionEvent evento) {
        if(ControllerUtils.nessunGiocatoreSelezionato(playersList)) {
            ControllerUtils.mostraAlertErrore("Seleziona un giocatore da ispezionare");
            return;
        }

        var bersaglio = playersList.getSelectionModel().getSelectedItem();
        var gioco =  this.giocoService.getGioco();

        if(bersaglio != null) {
            gioco.getGiocatoreUmano()
                    .ispezionaGiocatore(bersaglio)
                    .ifPresentOrElse(
                            ruolo -> this.giocoService.getStoricoAzioniService().registraAzione("(Visibile solo a te) " + bersaglio.getNome()+ " è un " + ruolo),
                            () -> this.giocoService.getStoricoAzioniService().registraAzione("Non puoi ispezionare questo giocatore.")
                    );
        }

        this.giocoService.eseguiAzioniAi();
        this.giocoService.avanzaFase();
        playersList.refresh();
    }

    public void onAttaccoClick(ActionEvent evento) {
        if(ControllerUtils.nessunGiocatoreSelezionato(playersList)) {
            ControllerUtils.mostraAlertErrore("Seleziona un giocatore da attaccare");
            return;
        }
        var gioco = this.giocoService.getGioco();

        var giocatore = gioco.getGiocatoreUmano();
        var giocatoreBersaglio = playersList.getSelectionModel().getSelectedItem();

        if (giocatoreBersaglio == null || !giocatoreBersaglio.isVivo()) {
            ControllerUtils.mostraAlertErrore("Seleziona un bersaglio valido");
            return;
        }

        Optional<Boolean> esito = giocatore.attaccaGiocatore(giocatoreBersaglio);
        if(esito.isPresent()) {
            this.giocoService.getStoricoAzioniService().registraAzione(giocatoreBersaglio.getNome() + " è stato ucciso da te.");

            this.giocoService.eseguiAzioniAi();
            playersList.getSelectionModel().clearSelection();
            playersList.refresh();
            this.giocoService.avanzaFase();
        }
    }

    public void onAvanzaFaseClick(ActionEvent evento) {
        this.giocoService.eseguiAzioniAi();
        this.giocoService.avanzaFase();
        log.info("Fase di gioco attuale: " + this.giocoService.getGioco().getFaseAttuale().toString());
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
