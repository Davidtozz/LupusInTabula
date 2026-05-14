package it.unicam.cs.mpgc.rpg125950.lupusintabula;

import javafx.application.Application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Application.launch(MainApplication.class, args);
    }
}
