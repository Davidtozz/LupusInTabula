package it.unicam.cs.mpgc.rpg125950.lupusintabula.enums;

public enum FaseGioco {
    /**
     * Indica la Fase notte: i lupi scelgono il loro bersaglio
     * */
    NOTTE_LUPI,
    /**
     * Indica la Fase notte: il veggenti indica un lupo o un contadino
     * */
    NOTTE_VEGGENTE,
    /**
     * Indica la Fase giorno: Tutti i giocatori votano il presunto lupo in base ai fatti accaduti la notte precedente
     * */
    VOTAZIONE_GIORNO;

    /**
     * Restituisce la fase successiva in modo ciclico (dopo DAY_VOTING torna a NIGHT_WOLVES)
     * */
    public FaseGioco next() {
        FaseGioco[] phases = values();
        int nextOrdinal = (this.ordinal() + 1) % phases.length;
        return phases[nextOrdinal];
    }

    @Override
    public String toString() {
        return switch (this) {
            case NOTTE_LUPI -> "Notte dei Lupi";
            case NOTTE_VEGGENTE -> "Notte del Veggente";
            case VOTAZIONE_GIORNO -> "Votazione Giorno";
        };
    }
}