package GiovanniDini.app;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {

        /**
         * Nasco.
         */
        
        //System.out.println("Thread main partito.");
        
        /**
         * Ricevo l'URL iniziale e faccio partire il manager.
         * Ricevo da input da tastiera l'URL iniziale, lo inserisco nella lista
         * degli URL da visitare, istanzio il manager e gli passo le liste.
         */
        
        System.out.println("Inserire l'URL da cui cominciare l'analisi, oppure digitare \"file\" se");
        System.out.println("si desidera leggere la workload dal file apposito (per informazioni digitare \"help\"):");
        Scanner user_input = new Scanner(System.in);
        String first_url;
        first_url = user_input.next();
        while ("help".equals(first_url)) {
            System.out.println("Ciao, sono l'aiuto");
            first_url = user_input.next();
        }
        //System.out.println("Hai inserito questo testo: "+first_url);
        
        /**
         * Genero le liste che saranno utilizzate.
         */
        
        ArrayList workload = new ArrayList();
        ArrayList visited = new ArrayList();
        ArrayList emails = new ArrayList();
        
        if ("file".equals(first_url)) {
            File workloadFile = new File ("workload.txt");
            if(!workloadFile.exists()) {
                try {
                    workloadFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Devo leggere il file.");
            Organizer.fileWorkload(workloadFile, workload);
            first_url = (String) workload.get(0);
            System.out.println("Devo analizzare questo: "+first_url);
        }
        
        File emailsFile = new File("emails.txt");
        File visitedFile = new File ("visited.txt");
        if(!emailsFile.exists()) {
            try {
                emailsFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!visitedFile.exists()) {
            try {
                visitedFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        Organizer.fileEmails(emailsFile, emails);
        Organizer.fileVisited(visitedFile, visited);
        
        workload.add(first_url);

        /**
         * Genero il manager.
         */
        
        Runnable manager = new Manager(workload, visited, emails);
        Thread m = new Thread(manager);
        m.start();
        m.setName("Manager");
        try {
            m.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Genero l'organizer.
         */
        Runnable organizer = new Organizer(visited, emails);
        Thread o = new Thread(organizer);
        o.start();
        o.setName("Organizer");
        try {
            o.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            /**
             * Muoio.
             */
        
        /*if(m.isAlive() == true){
            System.out.println("*--------*");
            System.out.println("Il manager è vivo");
        } else {
            System.out.println("*--------*");
            System.out.println("Il manager è morto");
        }
        
        if(o.isAlive() == true){
            System.out.println("*--------*");
            System.out.println("L'organizer è vivo");
        } else {
            System.out.println("*--------*");
            System.out.println("L'organizer è morto");
        }*/
        
        /*System.out.println("*--------*");
        System.out.println("Lista degli URL visitati: ");
        Manager.printList(visited);
        System.out.println("*--------*");
        System.out.println("Lista delle email trovate: ");
        Manager.printList(emails);*/
        //System.out.println("\n\nMain in attesa di terminazione.");
    }
}
