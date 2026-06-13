package it.unicam.cs.mpgc.rpg125950.lupusintabula.models;

public record DatiPartita(String nomeFile, String dataSalvataggio, String faseAttuale, int numeroGiocatori, String vincitore) {
    @Override
    public String toString() {
        return "%s — %s, %d giocatori — %s".formatted(
                dataSalvataggio.replace("T", " "), faseAttuale, numeroGiocatori, vincitore);
    }
}