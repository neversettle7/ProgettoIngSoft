/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GiovanniDini.app;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gioggi2002
 */
public class Manager implements Runnable {
    static ArrayList workload;
    static ArrayList visited;
    static ArrayList emails;
    int numCrawlers;
    Condition not_empty;
    int maxURLS;
    int counterNotify;
    static Semaphore stopGenerator;

    public Manager(ArrayList workload, ArrayList visited, ArrayList emails, int numCrawlers, int maxURLS){
        Manager.workload = workload;
        Manager.visited = visited;
        Manager.emails = emails;
        /*//Queste variabili modificano il comportamento del programma:
        //Indica il numero massimo di thread crawler che possono operare in
        //contemporanea
        this.numCrawlers = 5;
        //Indica il numero di URL da analizzare
        maxURLS = 20;*/
        Manager.stopGenerator = new Semaphore(numCrawlers);
        int counterNotify = 0;
    }
    
    public void run() {
        
        /**
         * Nasco.
         */
        
        Thread m = Thread.currentThread();
        String name = m.getName();
        //System.out.println("Thread "+name+" appena nato.");
        
        //System.out.println("Thread iniziali attivi: "+Thread.activeCount());
        
        /**
         * Genero il primo crawler.
         * Partendo dal primo URL.
         */
        
        Runnable e = new Crawler(prendiURL(workload));
        Thread t = new Thread(e);
        getToken();
        t.start();
        t.setName("Crawler_start");
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /**
         * Genero nuovi crawlers.
         * Partendo dai nuovi URL nella workload.
         */
        
        int i = 1;
        int analyzedURLS = 1;
        Runnable crawler[];
        crawler = new Crawler[numCrawlers];
        if (workload.isEmpty()){
            System.out.println("\nATTENZIONE:");
            System.out.println("Non ci sono (nuovi?) URL da analizzare.");
            System.out.println("Se si crede che questo sia dovuto ad un problema, cancellare il file \"visited.csv\".");
            System.out.println("Il programma termina qui.\n");
        } else {
        while (analyzedURLS != maxURLS){
            if(stopGenerator.availablePermits()>0 && workload.size()>0){
                Runnable crawler1 = new Crawler(prendiURL(workload));
                Thread t1 = new Thread(crawler1);
                getToken();
                t1.start();
                t1.setName("Crawler"+i);
                i++;
                //System.out.println("Numero stopGenerator: "+stopGenerator.availablePermits());
                System.out.println("Analizzati: "+analyzedURLS+" MAX: "+maxURLS);
                analyzedURLS++;
                System.out.println("Ci sono "+workload.size()+" link in workload.");
                System.out.println("Ci sono "+visited.size()+" link in visited.");
                System.out.println("Ci sono "+Thread.activeCount()+" thread attivi.");
                //printList(visited);
                counterNotify = 0;
            } else {
                while (counterNotify == 0){
                /*System.out.println("Permessi disponibili: "+stopGenerator.availablePermits());
                System.out.println("Dimensione della coda workload: "+workload.size());
                System.out.println("STO IN ATTESA.");*/
                counterNotify++;
                }
            }
        }
        }

        
        /**
         * Muoio.
         */
        
        while(stopGenerator.availablePermits()!=numCrawlers){
            //Attendo che tutti i thread abbiano rilasciato il token.
        }
        
        //System.out.println("THREAD "+name+" in attesa di terminazione. ");
    }
    
    /**
     * Questo metodo serve per prendere il primo URL contenuto nell'ArrayList
     * workload, ovvero la lista che contiene gli URL da visitare.
     * @param workload, la lista con gli URL da visitare.
     * @return il primo URL nella lista di attesa.
     */
    public synchronized String prendiURL(ArrayList workload)
    {
        String URL;
        //Retrieving values from list
        int i = 0;
        Object obj = (workload.get(i));
        URL = obj.toString();
        workload.remove(i);
        visited.add(URL);
        return URL;
    }
    
    public static void printList(ArrayList list) {
    for(int i = 0; i < list.size(); i++)
        {
            int j = i + 1;
            System.out.println("Elemento numero "+j+" : "+list.get(i));
        }
    }
    
     /**
     * Questo metodo serve per controllare se l'URL trovato dal crawler è già
     * stato analizzato.
     * @param URL è l'url trovato dal crawler.
     */
    public static void checkURL(String URL) {
        /* Cerco se l'elemento è già presente nella lista visited*/
        if (visited.contains(URL) || workload.contains(URL)){
            //System.out.println(URL+" già visitato.");
        } else {
            addURL(URL);
        }
    }
    
    /**
     * Questo metodo serve per controllare se l'email trovata dal crawler è già
     * stata inserita nella lista.
     * @param email è la email trovata dal crawler.
     */
    public static void checkEmail(String email) {
        if (emails.contains(email)){
            //System.out.println(URL+" già visitato.");
        } else {
            addEmail(email);
        }
    }
    
    /**
     * Questo metodo serve per inserire l'URL trovato dal crawler nella lista
     * degli URL in attesa di analisi.
     * @param URL è l'URL da inserire.
     */
    public static void addURL(String URL) {
        workload.add(URL);
    }
    
    
    /**
     * Questo metodo serve per inserire l'indirizzo email trovato dal crawler
     * nella lista delle emails trovate.
     * @param email è l'indirizzo email da inserire.
     */
    public static void addEmail(String email) {
        emails.add(email);
    }
    
    
    /**
     * Questo metodo serve per prendere un token dal semaforo e limitare
     * così il numero di thread contemporanei che vengono generati.
     */
    public static void getToken(){
        try {
            stopGenerator.acquire();
            //System.out.println("Token acquisito da un crawler: "+stopGenerator.availablePermits());
            //System.out.println("THREAD ATTIVI: "+Thread.activeCount());
        } catch (InterruptedException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Questo metodo serve per rilasciare un token dal semaforo e lasciare
     * così il posto ad un altro thread che può essere avviato in background.
     */
    public static void releaseToken(){
        stopGenerator.release();
        //System.out.println("Token rilasciato da un crawler: "+stopGenerator.availablePermits());
    }
}
