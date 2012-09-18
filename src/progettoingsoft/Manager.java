package progettoingsoft;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manager implements Runnable {

    static ArrayList<String> workload;
    static ArrayList<String> visited;
    static ArrayList<String> emails;
    static int numCrawlers;
    static int maxURLS;
    static Semaphore stopGenerator;

    public Manager(ArrayList<String> workload, ArrayList<String> visited, ArrayList<String> emails, int numCrawlers, int maxURLS) {
        Manager.workload = workload;
        Manager.visited = visited;
        Manager.emails = emails;
        Manager.numCrawlers = numCrawlers;
        Manager.maxURLS = maxURLS;
        // Questo semaforo limita il numero massimo di crawler che lavorano in contemporanea
        Manager.stopGenerator = new Semaphore(numCrawlers);
    }

    @Override
    public void run() {

        /**
         * Il thread manager nasce.
         */
        // Stampe di controllo.
        // Thread m = Thread.currentThread();
        // String name = m.getName();
        // System.out.println("Thread "+name+" appena nato.");
        // System.out.println("Thread iniziali attivi: "+Thread.activeCount());

        /**
         * Genero il primo crawler, partendo dal primo URL.
         */
        
        Runnable e = new Crawler(prendiURL(workload));
        Thread t = new Thread(e);
        getToken(); // Un token viene assegnato al crawler. Lo rilascerà in autonomia.
        t.start();
        t.setName("Crawler_start");
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null,
                    ex);
        }

        /**
         * Genero nuovi crawlers. Partendo dai nuovi URL nella workload.
         */
        
        int i = 1;
        int analyzedURLS = 1;
        // Se la lista degli URL da analizzare è vuota.
        if (workload.isEmpty()) {
            System.out.println("\nATTENZIONE:");
            System.out.println("Non ci sono URL da analizzare.");
            System.out.println("Questo può essere dovuto alle seguenti cause: ");
            System.out.println("- L'URL iniziale (o il comando) non era ben scritto. Si ricorda di usare la forma completa http(s)://www.sito.com");
            System.out.println("- L'URL iniziale era ben scritto ma la pagina non conteneva altri URL.");
            System.out.println("- Gli URL trovati nella pagina iniziale erano già contenuti nel file visited.csv, quindi sono già stati analizzati in una precedente sessione.");
            System.out.println("\nSe si crede che questo sia dovuto ad un problema, cancellare il file \"visited.csv\".");
            System.out.println("\n\nIl programma termina ora.\n");
        } else {
            // Fin quando non si è raggiunta la profondità massima dell'analisi.
            while (analyzedURLS != maxURLS) {
                if (stopGenerator.availablePermits() > 0 && workload.size() > 0) {
                    Runnable crawler1 = new Crawler(prendiURL(workload));
                    Thread t1 = new Thread(crawler1);
                    getToken();
                    t1.start();
                    t1.setName("Crawler" + i);
                    i++;
                    // Stampa di controllo.
                    // System.out.println("Numero stopGenerator: "+stopGenerator.availablePermits());
                    System.out.println("URL analizzati: " + analyzedURLS + " su un massimo di: " + maxURLS);
                    analyzedURLS++;
                    // Stampa di controllo.
                    // System.out.println("Ci sono " + workload.size() + " link in workload.");
                    // System.out.println("Ci sono " + visited.size() + " link in visited.");
                    // System.out.println("Ci sono " + Thread.activeCount() + " thread attivi.");
                    // printList(visited);
                }
            }
        }

        /**
         * Quando i crawlers sono terminati, terminiamo il thread manager.
         */
        
        while (stopGenerator.availablePermits() != numCrawlers) {
            // Attendo che tutti i thread abbiano rilasciato il token.
        }

        // Stampa di controllo
        // System.out.println("THREAD "+name+" in attesa di terminazione. ");
    }

    /**
     * Questo metodo serve per prendere il primo URL contenuto nell'ArrayList
     * workload, ovvero la lista che contiene gli URL da visitare.
     *
     * @param workload la lista con gli URL da visitare.
     * @return il primo URL nella lista di attesa.
     */
    
    public synchronized String prendiURL(ArrayList<String> workload) {
        String URL;
        // Retrieving values from list
        int i = 0;
        Object obj = (workload.get(i));
        URL = obj.toString();
        workload.remove(i);
        visited.add(URL);
        return URL;
    }

    /**
     * Questo metodo di controllo serve per stampare il contenuto di una lista.
     * E' usato come strumento di debug.
     *
     * @param list è la lista da stampare.
     */
    
    public static void printList(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            int j = i + 1;
            System.out.println("Elemento numero " + j + " : " + list.get(i));
        }
    }

    /**
     * Questo metodo serve per controllare se l'URL trovato dal crawler è già
     * stato analizzato o si trova già nella workload.
     *
     * @param URL è l'url trovato dal crawler.
     */
    
    public static void checkURL(String URL) {
        /* Cerco se l'elemento è già presente nella lista visited */
        if (visited.contains(URL) || workload.contains(URL)) {
            // System.out.println(URL+" già visitato.");
        } else {
            addURL(URL);
        }
    }

    /**
     * Questo metodo serve per controllare se l'email trovata dal crawler è già
     * stata inserita nella lista.
     *
     * @param email è la email trovata dal crawler.
     */
    
    public static void checkEmail(String email) {
        if (emails.contains(email)) {
            // System.out.println(URL+" già visitato.");
        } else {
            addEmail(email);
        }
    }

    /**
     * Questo metodo serve per inserire l'URL trovato dal crawler nella lista
     * degli URL in attesa di analisi. Viene invocato se il controllo sull'URL è
     * risultato nella non presenza dell'URL nelle liste workload o visited.
     *
     * @param URL è l'URL da inserire.
     */
    
    public static void addURL(String URL) {
        workload.add(URL);
    }

    /**
     * Questo metodo serve per inserire l'indirizzo email trovato dal crawler
     * nella lista delle emails trovate. Viene invocato se il controllo
     * sull'email è risultato nella non presenza dell'URL nella lista email.
     *
     * @param email è l'indirizzo email da inserire.
     */
    
    public static void addEmail(String email) {
        emails.add(email);
    }

    /**
     * Questo metodo serve per prendere un token dal semaforo e limitare così il
     * numero di thread contemporanei che vengono generati.
     */
    
    public static void getToken() {
        try {
            stopGenerator.acquire();
            // Stampa di controllo.
            // System.out.println("Token acquisito da un crawler: "+stopGenerator.availablePermits());
            // System.out.println("THREAD ATTIVI: "+Thread.activeCount());
        } catch (InterruptedException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    /**
     * Questo metodo serve per rilasciare un token dal semaforo e lasciare così
     * il posto ad un altro thread che può essere avviato in background.
     */
    
    public static void releaseToken() {
        stopGenerator.release();
        // Stampa di controllo.
        // System.out.println("Token rilasciato da un crawler: "+stopGenerator.availablePermits());
    }
}
