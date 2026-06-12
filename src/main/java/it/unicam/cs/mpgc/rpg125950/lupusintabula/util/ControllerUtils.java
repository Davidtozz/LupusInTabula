package it.unicam.cs.mpgc.rpg125950.lupusintabula.util;

import javafx.scene.Node;

public final class ControllerUtils {
    public static void nascondiElementi(Node... nodes) {
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
}
