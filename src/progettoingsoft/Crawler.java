package progettoingsoft;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler implements Runnable {

    String URL;
    String name;

    /**
     * Passiamo i parametri al crawler.
     *
     * @param URL
     */
    
    public Crawler(String URL) {
        this.URL = URL;
    }

    /**
     * Metodo run, per l'esecuzione del crawler.
     * @param URL è l'URL della pagina da analizzare.
     */
    
    @Override
    public void run() {

        /**
         * Il thread crawler nasce.
         */
        
        // Stampe di controllo
        // Thread t = Thread.currentThread();
        // String name = t.getName();
        // System.out.println();
        // System.out.println("Thread "+name+" appena nato.");
        try {
            /**
             * Ci connettiamo alla pagina per controllare lo status che ci
             * ritorna e il tipo di documento.
             */
            
            Connection.Response response = Jsoup.connect(URL).timeout(0).execute();
            int statusCode = response.statusCode();
            String contentType = response.contentType();
            // Se la pagina è disponibile.
            if (statusCode == 200) {
                try {
                    // Filtriamo il risultato dell'interrogazione sul tipo di contenuto.
                    // (escludiamo per esempio il tipo di codifica della pagina - es.: UTF-8)
                    String[] contentTypeClean = contentType.split(";");
                    contentType = contentTypeClean[0];
                    // Se non è una pagina contenente testo non ci interessa.
                    if ("text/html".equals(contentType)
                            || "text/plain".equals(contentType)
                            || "text/css".equals(contentType)
                            || "text/asp".equals(contentType)
                            || "text/xml".equals(contentType)
                            || "text/uri-list".equals(contentType)
                            || "text/richtext".equals(contentType)) {
                        Document doc = Jsoup.connect(URL).timeout(0).get();
                        parsing(doc);
                        // Stampa di controllo.
                        // System.out.println(name+" "+statusCode+" "+contentType);
                    } else {
                        // Stampa di controllo.
                        // System.out.println("La pagina "+URL+" non è stata processata perchè aveva questo formato: "+contentType);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE,
                            null, ex);
                }
            } else {
                // Stampa di controllo.
                // System.out.println("La pagina "+URL+" ha dato questo errore: "+statusCode);
            }
        } catch (IOException ex) {
            // Stampa di controllo.
            // Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            // System.out.println("404 ERROR");
        }

        // Stampa di controllo.
        // System.out.print("Il thread "+name);

        /**
         * Rilascio il token precedentemente acquisito.
         */
        
        Manager.releaseToken();

        /**
         * Il thread crawler termina.
         */
        
        // System.out.println("Thread "+name+" in fase di terminazione.");
        // System.out.println("Thread attivi prima di questa terminazione: "+Thread.activeCount());
    }

    /**
     * Questo metodo effettua il parsing della pagina.
     *
     * @param doc è il documento passato come risultato della connessione
     * all'URL.
     */
    
    protected void parsing(Document doc) {
        // Stampa di controllo.
        // System.out.println(" deve lavorare su questo indirizzo: "+URL);
        // Document doc = Jsoup.connect(URL).timeout(0).get();
        // System.out.println(doc);
        findLinksEmails(doc);

    }

    /**
     * Questo metodo effettua la ricerca di links ed indirizzi email nella
     * pagina parsata.
     *
     * @param doc è il documento passato come risultato del parsing.
     */
    
    protected void findLinksEmails(Document doc) {
        Elements links = doc.select("a[href]");
        // System.out.println("\nLinks: (%d)", links.size());
        for (Element link : links) {
            // print(" * a: <%s>  (%s)", link.attr("abs:href"),
            // trim(link.text(), 35));
            String content = link.attr("abs:href").toString();
            String emailPre = "mailto:";
            if (content.contains(emailPre)) {
                String newStr;
                newStr = link.attr("abs:href").replace(emailPre, "");
                newStr = newStr.replaceAll(" ", "");
                String[] newStrClean = newStr.split("\\?");
                newStr = newStrClean[0];
                Manager.checkEmail(newStr);
                // System.out.println("Ho trovato questa email: "+newStr);
            } else {
                Manager.checkURL(link.attr("abs:href"));
            }
        }
    }
    
    /**
     * Metodi non più usati: Questi metodi erano usati durante lo sviluppo del
     * software. Nelle ultime versioni non sono più usati, ma sono stati
     * mantenuti per comodità.
     *
     * Questo metodo serve per aggiungere l'email trovata alla lista.
     *
     * @param doc è il documento parsato.
     *
     * protected void findEmails(Document doc) { Elements emails =
     * doc.select("a[href]"); print("\nEmails: (%d)", emails.size()); for
     * (Element email : emails) { Manager.addEmail(email.attr("")); } }
     *
     * Questo metodo era usato per abbreviare la stampa in fase di stesura del
     * codice.
     * @param msg
     * @param args
     *
     * private static void print(String msg, Object... args) {
     * System.out.println(String.format(msg, args)); }
     *
     *
     * Questa funzione serviva per manipolare gli URL.
     *
     * @param s
     * @param width
     * @return
     *
     * private static String trim(String s, int width) { if (s.length() >
     * width){ return s.substring(0, width-1) + "."; } else { return s; } }
     */
}