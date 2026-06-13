@SuppressWarnings("module")
module it.unicam.cs.mpgc.rpg125950.lupusintabula {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires java.xml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires java.logging;
    requires annotations;
    requires kotlin.stdlib;

    opens it.unicam.cs.mpgc.rpg125950.lupusintabula to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg125950.lupusintabula.controller to javafx.fxml;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.controller;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.enums;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.models;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.service;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.repository;
    opens it.unicam.cs.mpgc.rpg125950.lupusintabula.repository to javafx.fxml;
}