package it.unicam.cs.mpgc.rpg125950.lupusintabula.enums;

public enum RisultatoVittoria {
    VITTORIA_CONTADINI,
    VITTORIA_LUPI,
    NON_SODDISFATTO;

    @Override
    public String toString() {
        return switch (this) {
            case VITTORIA_CONTADINI -> "I contadini hanno vinto!";
            case VITTORIA_LUPI -> "I lupi hanno vinto!";
            case NON_SODDISFATTO -> "Nessuna condizione di vittoria soddisfatta.";
        };
    }
}
