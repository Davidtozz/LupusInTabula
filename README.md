# Lupus in Tabula

Progetto che prende grande ispirazione dal gioco di deduzione sociale "Lupus in Tabula". I giocatori umani interpretano un ruolo (Contadino, Lupo o Veggente) e 
interagiscono con avversari controllati dall'AI in un ciclo notte/giorno: la notte i Lupi uccidono, il Veggente indaga; 
il giorno si vota per eliminare un giocatore sospettato di essere un Lupo.

---

## Come eseguire il progetto

### Prerequisiti
- Java 25 (LTS)
- Gradle (via Gradle Wrapper)

### Esecuzione del progetto
```bash
# Clona il repository
git clone https://github.com/Davidtozz/LupusInTabula.git
cd LupusInTabula

# Per la build del progetto:
./gradlew build

# Esecuzione su bash (Linux / MacOS)
./gradlew run 

# oppure (Windows)
.\gradlew.bat run  
```

---

## Struttura del progetto

```
src/main/java/
├── Main.java                          
├── MainApplication.java               
├── module-info.java                   
├── controller/
│   └── MainController.java            # Gestione interfaccia grafica e interazione utente
├── enums/
│   ├── FaseGioco.java                 # Gestisce le fasi del gioco
│   ├── RisultatoVittoria.java         # Determina chi ha vinto la partita e/o se è finita
│   └── RuoloGiocatore.java            # Definisce i ruoli che i giocatori possono assumere
├── models/
│   ├── DatiPartita.java               # Record per i metadati dei salvataggi
│   ├── Gioco.java                     # Stato della partita (giocatori, fase, storico)
│   ├── Giocatore.java                 # Protagonista umano
│   ├── GiocatoreAi.java               # Avversario AI controllato da un semplice algoritmo pseudocasuale
│   └── Votazione.java                 # Traccia la votazione della fase giorno
├── repository/
│   └── GiocoRepository.java           # Persiste le partite svolte utilizzando XPath
├── service/ 
│   ├── CondizioneVittoriaService.java # Verifica condizioni di fine partita
│   ├── GiocoService.java              # Orchestrazione del gioco
│   ├── StoricoAzioniService.java      # Registrazione azioni dei giocatori compiuti in partita
│   └── VotazioneService.java          # Gestione votazione 
└── util/
    ├── ControllerUtils.java           # Contiene alcuni utility per ridurre la complessità del controller
    └── GiocoUtils.java                # Contiene utility a supporto del gioco, come la generazione di Ai
```

---

## Uso di strumenti di AI

Durante lo sviluppo sono stati impiegati strumenti di AI per i seguenti scopi:

- Revisione del codice
- Validazione idee e flussi di gioco
- Styling dell'interfaccia utente
- Debugging 

Ogni risposta generata dagli strumenti AI è stata compresa e accettata verificandone la correttezza.