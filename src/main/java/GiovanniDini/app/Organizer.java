/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GiovanniDini.app;

import java.io.*;
import java.util.*;
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
        Organizer.visited = Manager.visited;
        Organizer.emails = Manager.emails;
    }
    
    public void run(){
        try {
            FileOutputStream outputEmails = new FileOutputStream("emails.txt");
            PrintStream writeEmails = new PrintStream(outputEmails);
            for (int i = 0; i < emails.size(); i++) {
                writeEmails.print(emails.get(i));
                writeEmails.print(",");
            }
            writeEmails.close();
            System.out.println("*--*--*--*--*");
            System.out.println("La lista delle emails trovate è nel file emails.txt.");
            FileOutputStream outputVisited = new FileOutputStream("visited.txt");
            PrintStream writeVisited = new PrintStream(outputVisited);
            for (int i = 0; i < visited.size(); i++) {
                writeVisited.print(visited.get(i));
                writeVisited.print(",");
            }
            writeVisited.close();
            System.out.println("*--*--*--*--*");
            System.out.println("La lista degli URL visitati è nel file visited.txt.");
            System.out.println("*--*--*--*--*");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        System.out.println("*--*--*--*--*");
        System.out.println("Lista degli URL visitati:");
        Manager.printList(visited);
        System.out.println("*--*--*--*--*");
        System.out.println("Lista degli indirizzi email trovati:");
        Manager.printList(emails);
        System.out.println("*--*--*--*--*");*/
    }
    
    public static ArrayList fileEmails(File file, ArrayList emails) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            
            try {
                while ((line = br.readLine()) != null){
                    String[] value = line.split(",");
                    for (String email : value){
                        //System.out.println(email);
                        emails.add(email);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emails;
    }
    
    public static ArrayList fileVisited(File file, ArrayList visited) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            
            try {
                while ((line = br.readLine()) != null){
                    String[] value = line.split(",");
                    for (String URL : value){
                        //System.out.println(URL);
                        visited.add(URL);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return visited;
    }
    
}