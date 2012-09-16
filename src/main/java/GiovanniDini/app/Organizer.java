/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GiovanniDini.app;

import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            FileOutputStream output = new FileOutputStream("emails.txt");
            PrintStream write = new PrintStream(output);
            for (int i = 0; i < emails.size(); i++) {
                write.print(emails.get(i));
                write.print(",");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("*--*--*--*--*");
        System.out.println("Lista degli URL visitati:");
        Manager.printList(visited);
        System.out.println("*--*--*--*--*");
        System.out.println("Lista degli indirizzi email trovati:");
        Manager.printList(emails);
        System.out.println("*--*--*--*--*");
    }
    
}