package progettoingsoft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgettoIngSoft {

    // user_input è lo Scanner usato per ricevere i comandi dall'utente
    private static Scanner user_input;

    /**
     * @param args
     */
    public static void main(String[] args) {

        /**
         * Il thread main nasce.
         */
        
        // System.out.println("Thread main partito.");

        /**
         * Apro il menu iniziale, con le diverse scelte a disposizione
         * dell'utente.
         */
        
        System.out.println("Progetto di Ingegneria del Software\n");
        System.out.println("Autore: Giovanni Dini");
        System.out.println("Matricola: 232274");
        System.out.println("Email: gioggi2002@gmail.com\n");

        System.out.println("Operazioni disponibili:");
        System.out.print("- Inserire l'URL da cui cominciare l'analisi \n- Digitare \"file\" se ");
        System.out.print("si desidera leggere la workload dal file apposito (workload.csv)");
        System.out.println("- Digitare \"restart\" se si desidera riprendere una vecchia sessione :");
        System.out.println("(digitare \"help\" per istruzioni o \"exit\" per uscire):");
        user_input = new Scanner(System.in);
        String first_url;
        first_url = user_input.next();

        /**
         * Sezione di help
         */
        
        while ("help".equals(first_url)) {
            System.out.print("\n\n*----------*\n\nCosa è e a cosa serve questo software?");
            System.out.print("\n\nQuesto software genera un sistema di crawler che girano per la rete, ");
            System.out.print("raccogliendo indirizzi email e salvandoli per un successivo utilizzo.");
            System.out.print("\n\n*----------*\n\nCome si utilizza?");
            System.out.print("\n\nIl funzionamento è molto semplice: è sufficiente inserire un URL ");
            System.out.print("(nel formato http://www.sito.com) e il programma lo analizzerà, raccogliendo ");
            System.out.print("automaticamente gli indirizzi dei link che troverà e analizzando successivamente ");
            System.out.print("anche quelli. Tutti gli indirizzi email trovati durante l'analisi sono salvati per ");
            System.out.print("un successivo utilizzo. ");
            System.out.print("\n\n*----------*\n\nQuale è l'utilità pratica di questo software?");
            System.out.print("\n\nIn questa versione il software si limita a raccogliere indirizzi email ");
            System.out.print("(si ricorda che lo spam non è una pratica legale), ma con poche e semplici ");
            System.out.print("modifiche è possibile fargli raccogliere e analizzare praticamente tutto ciò che ");
            System.out.print("può essere contenuto in una pagina web. Vista la natura di questo software ");
            System.out.print("(realizzato per un progetto d'esame), è da considerarsi un puro esercizio, ma che ");
            System.out.print("con poche modifiche può avere scopi decisamente più utili.");
            System.out.print("\n\n*----------*\n\nQuali comode funzionalità offre questo programma?");
            System.out.print("\n\n-Gli URL da visitare possono essere passati anche tramite file (workload.csv).");
            System.out.print("\n-Gli URL già visitati sono salvati in un file di testo e riletti ogni volta che ");
            System.out.print("il programma viene avviato, per evitare di ripetere lavoro già fatto in precedenza.");
            System.out.print("\n-Stessa cosa viene fatta per gli indirizzi email.");
            System.out.print("\n-I parametri di configurazione del programma sono contenuti in un semplice file di ");
            System.out.print("testo e comprendono il numero massimo di thread crawlers che operano in contemporanea ");
            System.out.print("e la profondità dell'analisi (espressa nel numero massimo di URL da analizzare.");
            System.out.print("\n\n*----------*\n\nIl programma è coperto da copyright?");
            System.out.print("\n\nLa licenza con cui viene fornito questo software è la Attribution-NonCommercial-ShareAlike ");
            System.out.print("3.0 Unported (CC BY-NC-SA 3.0). E' possibile modificare questo software e ridistribuirlo ");
            System.out.print("solo riportandone l'autore originale (Giovanni Dini). Non è possibile derivarne prodotti ");
            System.out.print("commerciali. Ogni derivazione dovrà essere distribuita sotto la stessa licenza.\n");
            System.out.println("\nInserire l'URL da cui cominciare l'analisi, oppure digitare \"file\" se ");
            System.out.println("si desidera leggere la workload dal file apposito ");
            System.out.println("(digitare \"help\" per istruzioni o \"exit\" per uscire):");
            first_url = user_input.next();
        }
        // System.out.println("Hai inserito questo testo: "+first_url);

        /**
         * Genero le liste che saranno utilizzate dal programma
         */
        
        ArrayList<String> workload = new ArrayList<>();
        ArrayList<String> visited = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<Integer> param = new ArrayList<>();

        // Se l'utente sceglie di uscire facciamo terminare il programma
        if ("exit".equals(first_url)) {
            return;
        }

        // Se l'utente sceglie di operare a partire dai file, il programma segue
        // queste istruzioni
        File workloadFile = new File("workload.csv");
        File lastWorkload = new File("nextWorkload.csv");
        if ("file".equals(first_url)) {
            if (!workloadFile.exists()) {
                try {
                    // Il file non esisteva, chiediamo all'utente di inserire
                    // manualmente un URL
                    workloadFile.createNewFile();
                    System.out.println("\n\nIl file non esisteva. Aggiungere manualmente il primo URL da analizzare: ");
                    first_url = user_input.next();
                } catch (IOException ex) {
                    //Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                // System.out.println("Devo leggere il file.");
                Organizer.fileWorkload(workloadFile, workload);
                first_url = (String) workload.get(0);
                // System.out.println("Devo analizzare questo: "+first_url);
            }
        }
        if ("restart".equals(first_url)) {
            if (!lastWorkload.exists()) {
                // Il file non esisteva, chiediamo all'utente di inserire
                // manualmente un URL
                System.out.println("\nIl file nextWorkload.csv non esiste. Aggiungere manualmente il primo URL da analizzare:");
                first_url = user_input.next();
            } else {
                System.out.println("Riprendo il lavoro dall'ultima sessione.");
                if (lastWorkload.length() == 0){
                    System.out.println("Il file nextWorkload.csv era vuoto. Inserire manualmente un URL:");
                    first_url = user_input.next();
                } else {
                Organizer.fileWorkload(lastWorkload, workload);
                first_url = (String) workload.get(0);
                }
                //System.out.println("Devo analizzare questo: " + first_url);
            }
        }

        /**
         * Queste sono le istruzioni che il programma dovrà usare per operare
         * sui file che contengono email già trovate e indirizzi precedentemente
         * visitati, in modo da non fare più volte lo stesso lavoro.
         */
        
        File emailsFile = new File("emails.csv");
        File visitedFile = new File("visited.csv");

        // Se l'utente sceglie di resettare il programma, cancelliamo i file che
        // contengono le history delle precedenti sessioni. Se non esistono, li creiamo
        // da zero.
        if ("reset".equals(first_url)) {
            // Resettiamo il file che contiene le workload salvate dalle precedenti sessioni
            if (!lastWorkload.exists()) {
                try {
                    lastWorkload.createNewFile();
                } catch (IOException ex) {
                    ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Il file nextWorkload.csv è stato resettato.");
            } else {
                lastWorkload.delete();
                try {
                    lastWorkload.createNewFile();
                } catch (IOException ex) {
                    ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Il file nextWorkload.csv è stato resettato.");

            // Resettiamo il file che contiene le email salvate dalle precedenti sessioni
            if (!emailsFile.exists()) {
                try {
                    emailsFile.createNewFile();
                } catch (IOException ex) {
                    ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Il file emails.csv è stato resettato.");
            } else {
                emailsFile.delete();
                try {
                    emailsFile.createNewFile();
                } catch (IOException ex) {
                    ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Il file emails.csv è stato resettato.");
            }

            // Resettiamo il file che contiene gli URL visitati nelle precedenti sessioni
            if (!visitedFile.exists()) {
                try {
                    visitedFile.createNewFile();
                } catch (IOException ex) {
                    ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Il file visited.csv è stato resettato.");
            } else {
                visitedFile.delete();
                try {
                    visitedFile.createNewFile();
                } catch (IOException ex) {
                    ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Il file visited.csv è stato resettato.");
            }

            System.out.println("\nOperazioni disponibili:");
            System.out.println("- Inserire l'URL da cui cominciare l'analisi o \"exit\" per uscire:");
            user_input = new Scanner(System.in);
            first_url = user_input.next();

            if ("exit".equals(first_url)) {
                return;
            }
        }

        /**
         * Creiamo il file per gestire le email trovate, se non esiste.
         */
        
        if (!emailsFile.exists()) {
            try {
                emailsFile.createNewFile();
            } catch (IOException ex) {
                ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Creiamo il file per gestire gli URL visitati, se non esiste.
         */
        
        if (!visitedFile.exists()) {
            try {
                visitedFile.createNewFile();
            } catch (IOException ex) {
                ////Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Aggiungiamo i dati delle precedenti sessioni nelle nostre liste.
        Organizer.fileEmails(emailsFile, emails);
        Organizer.fileVisited(visitedFile, visited);

        // Aggiungiamo il primo URL e lo passiamo al nostro crawler.
        workload.add(first_url);

        /**
         * Genero il manager.
         */
        
        // Inizializziamo le variabili che configureranno il nostro manager
        int numCrawlers; // Indica il numero massimo di crawler che operano in contemporanea
        int maxURLS; // Indica la profondità dell'analisi (numero di URL da visitare)

        /**
         * Andiamo a leggere il file di configurazione che contiene i parametri
         * per configurare il manager.
         */
        
        File paramFile = new File("preferences.cfg");
        // Se il file di configurazione non esiste

        if (!paramFile.exists()) {
            System.out.println("Il file di configurazione non esiste. Saranno utilizzati i parametri di default: ");
            System.out.println("Numero di crawlers in contemporanea: 5.\nProfondità dell'analisi: 30 URLs.");
            numCrawlers = 5;
            maxURLS = 30;
        } else {
            Organizer.fileParam(paramFile, param);
            // Se il file di configurazione è scritto male
            if (param.size() > 2) {
                System.out.println("Il file di configurazione non era scritto correttamente.");
                System.out.println("Contollare la documentazione per aiuto.");
                System.out.println("Saranno utilizzati i parametri di default: ");
                System.out.println("Numero di crawlers in contemporanea: 5.\nProfondità dell'analisi: 30 URLs.");
                numCrawlers = 5;
                maxURLS = 30;
            } else {
                // Se il file di configurazione è scritto bene, procediamo.
                numCrawlers = (Integer) param.get(0);
                maxURLS = (Integer) param.get(1);
                System.out.println("\nTrovato file di configurazione. Il programma avrà questi parametri: ");
                System.out.println("-Numero di crawlers in contemporanea: " + numCrawlers);
                System.out.println("-Profondità dell'analisi: " + maxURLS + " URLS\n");
            }
        }

        /**
         * Generiamo il manager.
         */
        
        Runnable manager = new Manager(workload, visited, emails, numCrawlers, maxURLS);
        Thread m = new Thread(manager);

        m.start();

        m.setName("Manager");
        try {
            m.join();
        } catch (InterruptedException ex) {
            //Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Terminato il manager, generiamo l'organizer per gestire l'output su
         * file.
         */
        
        Runnable organizer = new Organizer(visited, emails);
        Thread o = new Thread(organizer);

        o.start();

        o.setName("Organizer");
        try {
            o.join();
        } catch (InterruptedException ex) {
            //Logger.getLogger(ProgettoIngSoft.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("L'analisi è stata completata. ");
        System.out.println("Ci sono ancora " + workload.size() + " URL nella workload.");
        System.out.println("Vuoi che siano scritti in un file in modo da poter proseguire l'analisi successivamente? si/no");
        String answer = user_input.next();

        if ("si".equals(answer)) {
            File finalWorkload = new File("nextWorkload.csv");
            Organizer.finalWorkload(finalWorkload, workload);
        }

        /**
         * Termino il thread main.
         */
        
        /**
         * Output di controllo
         *
         * if(m.isAlive() == true){ System.out.println("*--------*");
         * System.out.println("Il manager è vivo"); } else {
         * System.out.println("*--------*"); System.out.println("Il manager è
         * morto"); }
         *
         * if(o.isAlive() == true){ System.out.println("*--------*");
         * System.out.println("L'organizer è vivo"); } else {
         * System.out.println("*--------*"); System.out.println("L'organizer è
         * morto"); }
         *
         * System.out.println("*--------*"); System.out.println("Lista degli URL
         * visitati: "); Manager.printList(visited);
         * System.out.println("*--------*"); System.out.println("Lista delle
         * email trovate: "); Manager.printList(emails);
         * System.out.println("\n\nMain in attesa di terminazione.");
         *
         */
    }
}
