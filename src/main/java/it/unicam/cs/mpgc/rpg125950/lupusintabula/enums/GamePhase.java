package it.unicam.cs.mpgc.rpg125950.lupusintabula.enums;

public enum GamePhase {
    /**
     * Indica la Fase notte: i lupi scelgono il loro bersaglio
     * */
    NIGHT_WOLVES,
    /**
     * Indica la Fase notte: il veggenti indica un lupo o un contadino
     * */
    NIGHT_SEER,
    /**
     * Indica la Fase giorno: Tutti i giocatori discutono il presunto lupo in base ai fatti accaduti la notte precedente
     * */
    DAY_DISCUSSION,
    /**
     * Indica la Fase giorno: Tutti i giocatori votano il presunto lupo in base ai fatti accaduti la notte precedente
     * */
    DAY_VOTING;

    /**
     * Restituisce la fase successiva in modo ciclico (dopo DAY_VOTING torna a NIGHT_WOLVES)
     * */
    public GamePhase next() {
        GamePhase[] phases = values();
        int nextOrdinal = (this.ordinal() + 1) % phases.length;
        return phases[nextOrdinal];
    }
}