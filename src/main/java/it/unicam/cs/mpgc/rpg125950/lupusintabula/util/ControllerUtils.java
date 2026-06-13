package it.unicam.cs.mpgc.rpg125950.lupusintabula.util;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public final class ControllerUtils {
    public static void nascondiElementi(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(false);
        }
    }

    public static void nascondiERimuoviElementi(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }

    public static void mostraElementi(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(true);
            node.setManaged(true);
        }
    }

    public static boolean nessunGiocatoreSelezionato(ListView<?> listView) {
        return listView.getSelectionModel().isEmpty();
    }

    public static void mostraAlertErrore(String messaggio) {
        new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK).showAndWait();
    }

    public static void mostraAlertInformazione(String messaggio) {
        new Alert(Alert.AlertType.INFORMATION, messaggio, ButtonType.OK).showAndWait();
    }

    public static void disabilitaTutti(Pane container) {
        impostaStatoTutti(container);
    }

    public static void disabilitaTuttiEccetto(Pane container, Node nodoPermesso) {
        for (Node node : container.getChildren()) {
            node.setDisable(!node.equals(nodoPermesso));
        }
    }

    private static void impostaStatoTutti(Pane container) {
        for (Node node : container.getChildren()) {
            node.setDisable(true);
        }
    }
}
