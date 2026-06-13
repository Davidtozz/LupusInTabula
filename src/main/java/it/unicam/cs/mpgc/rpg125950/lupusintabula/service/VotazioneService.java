package it.unicam.cs.mpgc.rpg125950.lupusintabula.service;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Giocatore;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.GiocatoreAi;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.core.Votazione;

interface IVotazioneService {
    void avviaVotazione(Giocatore bersaglioGiocatoreUmano);

    void votaEspulsione(Giocatore bersaglio);

    Giocatore mostraEsito();
}

public class VotazioneService implements IVotazioneService {
    private final GiocoService giocoService;
    private final StoricoAzioniService storicoAzioniService;
    private Votazione votazioneCorrente;

    public VotazioneService(GiocoService giocoService, StoricoAzioniService storicoAzioniService) {
        this.giocoService = giocoService;
        this.storicoAzioniService = storicoAzioniService;
    }

    @Override
    public void avviaVotazione(Giocatore bersaglioGiocatoreUmano) {
        var giocatoriVivi = giocoService
                                .getGioco()
                                .getGiocatori()
                                .stream()
                                .filter(Giocatore::isVivo).toList();

        Votazione votazione = new Votazione(giocatoriVivi);

        votazione.registraVoto(bersaglioGiocatoreUmano);

        giocatoriVivi.stream()
                .filter(g -> g instanceof GiocatoreAi)
                .forEach(ai -> {
                    Giocatore bersaglio = ((GiocatoreAi)ai).vota(giocatoriVivi);
                    if (bersaglio != null) {
                        votazione.registraVoto(bersaglio);
                    }
                });
        Giocatore giocatoreOggettoVotazione = votazione.mostraEsito();
        giocatoreOggettoVotazione.setVivo(false);

        storicoAzioniService.registraAzione("Il villaggio ha deciso: " + giocatoreOggettoVotazione.getNome() + " è stato linciato!");
        giocoService.getCondizioneVittoriaService().controllaCondizione(giocatoriVivi);
        giocoService.avanzaFase();
    }

    @Override
    public void votaEspulsione(Giocatore bersaglio) {
        if (votazioneCorrente == null) {
            var giocatoriVivi = giocoService.getGioco().getGiocatori().stream()
                .filter(Giocatore::isVivo).toList();
            votazioneCorrente = new Votazione(giocatoriVivi);
        }
        votazioneCorrente.registraVoto(bersaglio);
    }

    @Override
    public Giocatore mostraEsito() {
        if (votazioneCorrente == null) return null;

        Giocatore linciato = votazioneCorrente.mostraEsito();
        linciato.setVivo(false);

        storicoAzioniService.registraAzione(
            "Il villaggio ha deciso: " + linciato.getNome() + " è stato linciato!"
        );

        var giocatoriVivi = giocoService.getGioco().getGiocatori().stream()
            .filter(Giocatore::isVivo).toList();
        giocoService.getCondizioneVittoriaService().controllaCondizione(giocatoriVivi);

        votazioneCorrente = null;
        giocoService.avanzaFase();
        return linciato;
    }
}
