package it.unicam.cs.mpgc.rpg125950.lupusintabula.repository;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.models.*;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.enums.*;
import it.unicam.cs.mpgc.rpg125950.lupusintabula.service.GiocoService;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

interface IGiocoRepository {
    void salvaDatiPartita(Gioco gioco, String vincitore);
    Gioco caricaDatiPartita(String nomeFile);
    List<DatiPartita> listaSalvataggi();
    String dettaglioSalvataggio(String nomeFile);
}

public class GiocoRepository implements IGiocoRepository {
    private static final Path CARTELLA_SALVATAGGI = Path.of("partite-salvate");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");

    @Override
    public void salvaDatiPartita(Gioco gioco, String vincitore) {
        try {
            Files.createDirectories(CARTELLA_SALVATAGGI);

            Document doc = nuovoDocumento();
            Element radice = doc.createElement("partita");
            doc.appendChild(radice);

            radice.appendChild(creaMeta(doc));
            radice.appendChild(creaElementoTesto(doc, "faseAttuale", gioco.getFaseAttuale().get().name()));
            radice.appendChild(creaGiocatoriXml(doc, gioco));
            radice.appendChild(creaElementoTesto(doc, "vincitore", vincitore));
            radice.appendChild(creaStoricoXml(doc, gioco));

            String nomeFile = "partita_" + LocalDateTime.now().format(FORMATO_DATA) + ".xml";
            scriviXml(doc, CARTELLA_SALVATAGGI.resolve(nomeFile).toFile());

        } catch (ParserConfigurationException | TransformerException | IOException e) {
            throw new RuntimeException("Errore durante il salvataggio della partita", e);
        }
    }

    @Override
    public Gioco caricaDatiPartita(String nomeFile) {
        try {
            File file = CARTELLA_SALVATAGGI.resolve(nomeFile).toFile();
            Document doc = leggiXml(file);
            XPath xpath = XPathFactory.newInstance().newXPath();

            Gioco gioco = new Gioco();

            String faseStr = xpath.evaluate("/partita/faseAttuale", doc);
            gioco.getFaseAttuale().set(FaseGioco.valueOf(faseStr));

            NodeList nodiGiocatori = (NodeList) xpath.evaluate(
                    "/partita/giocatori/giocatore", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodiGiocatori.getLength(); i++) {
                Element el = (Element) nodiGiocatori.item(i);
                String nome = xpath.evaluate("nome", el);
                String ruolo = xpath.evaluate("ruolo", el);
                String vivo = xpath.evaluate("vivo", el);
                String tipo = el.getAttribute("tipo");

                Giocatore g = "AI".equals(tipo)
                        ? new GiocatoreAi(nome, RuoloGiocatore.valueOf(ruolo))
                        : new Giocatore(nome, RuoloGiocatore.valueOf(ruolo));
                g.setVivo(Boolean.parseBoolean(vivo));
                gioco.getGiocatori().add(g);
            }

            NodeList nodiAzioni = (NodeList) xpath.evaluate(
                    "/partita/storico/azione", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodiAzioni.getLength(); i++) {
                gioco.getStoricoAzioniGioco().add(nodiAzioni.item(i).getTextContent());
            }

            return gioco;

        } catch (ParserConfigurationException | SAXException | IOException
                | XPathExpressionException e) {
            throw new RuntimeException("Errore durante il caricamento della partita", e);
        }
    }

    @Override
    public List<DatiPartita> listaSalvataggi() {
        List<DatiPartita> salvataggi = new ArrayList<>();
        if (!Files.exists(CARTELLA_SALVATAGGI)) return salvataggi;

        File[] files = CARTELLA_SALVATAGGI.toFile()
                .listFiles((_, name) -> name.endsWith(".xml"));
        if (files == null) return salvataggi;

        XPath xpath = XPathFactory.newInstance().newXPath();

        for (File f : files) {
            try {
                Document doc = leggiXml(f);
                String data = xpath.evaluate("/partita/meta/dataSalvataggio", doc);
                String fase = xpath.evaluate("/partita/faseAttuale", doc);
                Number num = (Number) xpath.evaluate(
                        "count(/partita/giocatori/giocatore)", doc, XPathConstants.NUMBER);
                String vincitore = xpath.evaluate("/partita/vincitore", doc);
                if (vincitore.isBlank()) vincitore = "Sconosciuto";
                salvataggi.add(new DatiPartita(f.getName(), data, fase, num.intValue(), vincitore));
            } catch (Exception e) {
                salvataggi.add(new DatiPartita(f.getName(), "—", "corrotto", 0, "—"));
            }
        }

        return salvataggi;
    }

    @Override
    public String dettaglioSalvataggio(String nomeFile) {
        try {
            Gioco gioco = caricaDatiPartita(nomeFile);

            File file = CARTELLA_SALVATAGGI.resolve(nomeFile).toFile();
            Document doc = leggiXml(file);
            String vincitore = XPathFactory.newInstance().newXPath()
                    .evaluate("/partita/vincitore", doc);
            if (vincitore.isBlank()) vincitore = "Sconosciuto";

            StringBuilder sb = new StringBuilder();
            sb.append("Salvataggio: ").append(nomeFile).append("\n");
            sb.append("Fase: ").append(gioco.getFaseAttuale().get()).append("\n");
            sb.append("Vincitore: ").append(vincitore).append("\n");
            sb.append("Giocatori:\n");
            for (Giocatore g : gioco.getGiocatori()) {
                sb.append("  - ").append(g.getNome())
                  .append(" (").append(g.getRuolo()).append(")")
                  .append(g.isVivo() ? " — vivo" : " — morto")
                  .append("\n");
            }
            sb.append("\nCronaca:\n");
            for (String azione : gioco.getStoricoAzioniGioco()) {
                sb.append("  - ").append(azione).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Impossibile leggere il salvataggio: " + nomeFile;
        }
    }

    private static Document nuovoDocumento() throws ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    private static Document leggiXml(File file)
            throws ParserConfigurationException, SAXException, IOException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    }

    private static Element creaMeta(Document doc) {
        Element meta = doc.createElement("meta");
        meta.appendChild(creaElementoTesto(doc, "dataSalvataggio", LocalDateTime.now().toString()));
        return meta;
    }

    private static Element creaGiocatoriXml(Document doc, Gioco gioco) {
        Element contenitore = doc.createElement("giocatori");
        for (Giocatore g : gioco.getGiocatori()) {
            Element el = doc.createElement("giocatore");
            el.setAttribute("tipo", g instanceof GiocatoreAi ? "AI" : "UMANO");
            el.appendChild(creaElementoTesto(doc, "nome", g.getNome()));
            el.appendChild(creaElementoTesto(doc, "ruolo", g.getRuolo().name()));
            el.appendChild(creaElementoTesto(doc, "vivo", String.valueOf(g.isVivo())));
            contenitore.appendChild(el);
        }
        return contenitore;
    }

    private static Element creaStoricoXml(Document doc, Gioco gioco) {
        Element contenitore = doc.createElement("storico");
        for (String azione : gioco.getStoricoAzioniGioco()) {
            contenitore.appendChild(creaElementoTesto(doc, "azione", azione));
        }
        return contenitore;
    }

    private static Element creaElementoTesto(Document doc, String nome, String valore) {
        Element el = doc.createElement(nome);
        el.setTextContent(valore);
        return el;
    }

    private static void scriviXml(Document doc, File file) throws TransformerException {
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        tr.transform(new DOMSource(doc), new StreamResult(file));
    }
}
