@SuppressWarnings("module")
module it.unicam.cs.mpgc.rpg125950.lupusintabula {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires java.logging;
    requires java.sql;
    requires com.h2database;
    requires annotations;
    requires kotlin.stdlib;

    opens it.unicam.cs.mpgc.rpg125950.lupusintabula to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg125950.lupusintabula.controller to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg125950.lupusintabula.data to javafx.fxml;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.controller;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.enums;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.data;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.core;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.service;
    exports it.unicam.cs.mpgc.rpg125950.lupusintabula.repository;
    opens it.unicam.cs.mpgc.rpg125950.lupusintabula.repository to javafx.fxml;
}