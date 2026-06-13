package it.unicam.cs.mpgc.rpg125950.lupusintabula.service;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.DatiPartita;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.Giocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.Gioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.FaseGioco;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RuoloGiocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.repository.GiocoRepository;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.util.GiocoUtils;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.GiocatoreAi;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.RisultatoVittoria;
import lombok.Getter;

import java.util.List;

interface IGiocoService {
    void iniziaPartita(String nomeGiocatoreUmano);
    void avanzaFase();
}

public final class GiocoService implements IGiocoService {
    public void salvaPartita(String vincitore) {
        giocoRepository.salvaDatiPartita(gioco, vincitore);
    }

    public static String calcolaVincitore(Gioco gioco) {
        long lupiVivi = gioco.getGiocatori().stream()
                .filter(Giocatore::isVivo).filter(Giocatore::isLupo).count();
        long viviTotali = gioco.getGiocatori().stream()
                .filter(Giocatore::isVivo).count();
        if (lupiVivi == 0) return "Contadini";
        if (lupiVivi >= viviTotali - lupiVivi) return "Lupi";
        return "In corso";
    }

    public List<DatiPartita> listaSalvataggi() {
        return giocoRepository.listaSalvataggi();
    }

    public String dettaglioSalvataggio(String nomeFile) {
        return giocoRepository.dettaglioSalvataggio(nomeFile);
    }

    @Getter
    private Gioco gioco;
    @Getter
    private final CondizioneVittoriaService condizioneVittoriaService = new CondizioneVittoriaService();
    @Getter
    private final VotazioneService votazioneService;
    @Getter
    private final StoricoAzioniService storicoAzioniService;
    private final GiocoRepository giocoRepository;

    public GiocoService(Gioco gioco) {
        this.gioco = gioco;
        this.storicoAzioniService = new StoricoAzioniService(gioco.getStoricoAzioniGioco());
        this.votazioneService = new VotazioneService(this, this.storicoAzioniService);
        this.giocoRepository = new GiocoRepository();
    }
    @Override
    public void iniziaPartita(String nomeGiocatoreUmano) {
        this.gioco.getGiocatori().clear();
        this.gioco.getStoricoAzioniGioco().clear();
        this.gioco.getFaseAttuale().set(null);

        final Giocatore giocatoreUmano = new Giocatore(nomeGiocatoreUmano, RuoloGiocatore.getRandomRole());
        this.gioco.getGiocatori().add(giocatoreUmano);

        final int GIOCATORI_AI_CONSENTITI = 9;
        this.gioco.getGiocatori().addAll(GiocoUtils.generaGiocatoriAi(GIOCATORI_AI_CONSENTITI, giocatoreUmano.getRuolo()));
        this.gioco.getFaseAttuale().set(FaseGioco.NOTTE_LUPI);
    }

    @Override
    public void avanzaFase() {
        var faseAttuale = this.gioco.getFaseAttuale();
        faseAttuale.set(faseAttuale.get().next());
    }

    public void eseguiAzioniAi() {
        var giocatoriRimasti = gioco.getGiocatori().stream().filter(Giocatore::isVivo).toList();
        for (Giocatore g : giocatoriRimasti) {
            if (!(g instanceof GiocatoreAi ai)) continue;
            String logAzione = ai.eseguiAzione(gioco.getGiocatori(), gioco.getFaseAttuale().get());
            if (!logAzione.isEmpty()) {
                storicoAzioniService.registraAzione(logAzione);
            }
        }

        // Controllo condizione di vittoria dopo le azioni AI
        condizioneVittoriaService.controllaCondizione(giocatoriRimasti);
    }

}