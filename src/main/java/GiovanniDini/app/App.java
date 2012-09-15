package GiovanniDini.app;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static void main(String[] args) {

        /**
         * Nasco.
         */
        
        System.out.println("Thread main partito.");
        
        /**
         * Ricevo l'URL iniziale e istanzio il manager.
         * Ricevo da input da tastiera l'URL iniziale, lo inserisco nella lista
         * degli URL da visitare, istanzio il manager e gli passo le liste.
         */
        
        System.out.println("Inserire l'URL da cui cominciare l'analisi:");
        Scanner user_input = new Scanner(System.in);
        String first_url;
        first_url = user_input.next();
        System.out.println("Hai inserito questo testo: "+first_url);
        
        /**
         * Genero le liste che saranno utilizzate.
         */
        
        ArrayList workload = new ArrayList();
        ArrayList visited = new ArrayList();
        ArrayList emails = new ArrayList();
        
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
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            /**
             * Muoio.
             */
        
        if(m.isAlive() == true){
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
        }
        
        /*System.out.println("*--------*");
        System.out.println("Lista degli URL visitati: ");
        Manager.printList(visited);
        System.out.println("*--------*");
        System.out.println("Lista delle email trovate: ");
        Manager.printList(emails);*/
        System.out.println("\n\nMain in attesa di terminazione.");
    }
}
