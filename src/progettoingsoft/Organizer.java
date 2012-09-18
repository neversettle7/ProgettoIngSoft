package progettoingsoft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Organizer implements Runnable {

    static ArrayList<String> visited;
    static ArrayList<String> emails;

    public Organizer(ArrayList<String> visited, ArrayList<String> emails) {
        Organizer.visited = Manager.visited;
        Organizer.emails = Manager.emails;
    }

    /**
     * Il metodo run scrive le liste ottenute sui relativi file.
     */
    
    @Override
    public void run() {
        try {
            FileOutputStream outputEmails = new FileOutputStream("emails.csv");
            try (PrintStream writeEmails = new PrintStream(outputEmails)) {
                for (int i = 0; i < emails.size(); i++) {
                    writeEmails.print(emails.get(i));
                    writeEmails.print(",");
                }
            }
            System.out.println("*--*--*--*--*");
            System.out.println("La lista delle emails trovate è nel file emails.csv.");
            FileOutputStream outputVisited = new FileOutputStream("visited.csv");
            try (PrintStream writeVisited = new PrintStream(outputVisited)) {
                for (int i = 0; i < visited.size(); i++) {
                    writeVisited.print(visited.get(i));
                    writeVisited.print(",");
                }
            }
            System.out.println("*--*--*--*--*");
            System.out.println("La lista degli URL visitati è nel file visited.csv.");
            System.out.println("*--*--*--*--*");
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Stampe di controllo.
         *
         * System.out.println("*--*--*--*--*"); System.out.println("Lista degli
         * URL visitati:"); Manager.printList(visited);
         * System.out.println("*--*--*--*--*"); System.out.println("Lista degli
         * indirizzi email trovati:"); Manager.printList(emails);
         * System.out.println("*--*--*--*--*");
         */
    }

    /**
     * Questi metodi servono per importare i dati (URL da visitare, indirizzi
     * email già trovati, URL già visitati) da file.
     */
    
    /**
     * Questo metodo importa i parametri del programma.
     *
     * @param file è il file su cui operiamo.
     * @param workload è la lista su cui operiamo
     * @return
     */
    
    public static ArrayList<Integer> fileParam(File file, ArrayList<Integer> param) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    String[] value = line.split(",");
                    for (String temp : value) {
                        //System.out.println(email);
                        int num = Integer.parseInt(temp);
                        param.add(num);
                    }
                }
            } catch (IOException ex) {
                //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return param;
    }

    /**
     * Questo metodo importa gli URL da visitare nella workload.
     *
     * @param file è il file su cui operiamo.
     * @param workload è il file su cui operiamo.
     * @return
     */
    
    public static ArrayList<String> fileWorkload(File file, ArrayList<String> workload) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            try {
                while ((line = br.readLine()) != null) {
                    String[] value = line.split(",");
                    for (String URL : value) {
                        //System.out.println(email);
                        workload.add(URL);
                    }
                }
            } catch (IOException ex) {
                //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return workload;
    }

    /**
     * Questo metodo importa gli indirizzi email già trovati nella lista emails.
     *
     * @param file è il file su cui operare.
     * @param emails è la lista su cui operiamo.
     * @return
     */
    
    public static ArrayList<String> fileEmails(File file, ArrayList<String> emails) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            try {
                while ((line = br.readLine()) != null) {
                    String[] value = line.split(",");
                    for (String email : value) {
                        //System.out.println(email);
                        emails.add(email);
                    }
                }
            } catch (IOException ex) {
                //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emails;
    }

    /**
     * Questo metodo importa gli URL già visitati nella lista visited.
     *
     * @param file è il file su cui operare.
     * @param visited è la lista su cui operare.
     * @return
     */
    
    public static ArrayList<String> fileVisited(File file, ArrayList<String> visited) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            try {
                while ((line = br.readLine()) != null) {
                    String[] value = line.split(",");
                    for (String URL : value) {
                        //System.out.println(URL);
                        visited.add(URL);
                    }
                }
            } catch (IOException ex) {
                //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return visited;
    }

    /**
     * Questo metodo serve per salvare il contenuto della workload, in modo che
     * sia riutilizzabile per una prossima sessione.
     *
     * @param file è il file su cui operiamo.
     * @param workload è la lista su cui operiamo.
     */
    
    public static void finalWorkload(File file, ArrayList<String> workload) {
        try {
            file.createNewFile();
            FileOutputStream outputWorkload = new FileOutputStream(file);
            try (PrintStream writeWorkload = new PrintStream(outputWorkload)) {
                for (int i = 0; i < workload.size(); i++) {
                    writeWorkload.print(workload.get(i));
                    writeWorkload.print(",");
                }
            }
        } catch (IOException ex) {
            //Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
