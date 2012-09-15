/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GiovanniDini.app;

import java.util.*;

/**
 *
 * @author gioggi2002
 */
public class Organizer implements Runnable {
    static ArrayList visited;
    static ArrayList emails;
    
    public Organizer(ArrayList visited, ArrayList emails) {
        this.visited = Manager.visited;
        this.emails = Manager.emails;
    }
    
    public void run(){
        System.out.println("*--*--*--*--*");
        System.out.println("Lista degli URL visitati:");
        Manager.printList(visited);
        System.out.println("*--*--*--*--*");
        System.out.println("Lista degli indirizzi email trovati:");
        Manager.printList(emails);
        System.out.println("*--*--*--*--*");
    }
    
}