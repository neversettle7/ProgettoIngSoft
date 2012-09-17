/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GiovanniDini.app;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author gioggi2002
 */
public class Crawler implements Runnable {
    String URL;
    String name;
    
    /**
     * Passiamo i parametri al crawler.
     * @param URL
     */
    public Crawler(String URL){
        this.URL = URL;
    }
    
    /**
     * Il nostro metodo per eseguire il crawling.
     * @param URL è l'URL della pagina da analizzare.
     */
    
    @Override
    public void run() {

            /**
             * Nasco.
             */
            
            Thread t = Thread.currentThread();
            String name = t.getName();
            System.out.println();
            //System.out.println("Thread "+name+" appena nato.");
            
        try {
            Connection.Response response = Jsoup.connect(URL).timeout(0).execute();
            int statusCode = response.statusCode();
            String contentType = response.contentType();
            if (statusCode == 200) {
                try {
                    String[] contentTypeClean = contentType.split(";");
                    contentType = contentTypeClean[0];
                    if ("text/html".equals(contentType) || "text/plain".equals(contentType) ||
                        "text/css".equals(contentType) || "text/asp".equals(contentType) ||
                        "text/xml".equals(contentType) || "text/uri-list".equals(contentType) ||
                        "text/richtext".equals(contentType)) {
                        Document doc = Jsoup.connect(URL).timeout(0).get();
                        parsing(doc);
                        System.out.println(name+" "+statusCode+" "+contentType);
                    } else {
                        System.out.println("La pagina "+URL+" non è stata processata perché aveva questo formato: "+contentType);
                    }
                }
                catch (IOException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("La pagina "+URL+" ha dato questo errore: "+statusCode);
            }
            } catch (IOException ex) {
            //Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("404 ERROR");
        }
            
            
            //System.out.print("Il thread "+name);

            
            /**
             * Rilascio il token.
             */
            
            Manager.releaseToken();
            
            /**
             * Muoio.
             */
            
            //System.out.println("Thread "+name+" in fase di terminazione.");
            
            //System.out.println("Thread attivi prima di questa terminazione: "+Thread.activeCount());

    }
    
    protected void parsing(Document doc) {
        //System.out.println(" deve lavorare su questo indirizzo: "+URL);
            /**
             * Effettuo il parsing della pagina.
             */
            //Document doc = Jsoup.connect(URL).timeout(0).get();
            //System.out.println(doc);
            findLinksEmails(doc);
        
    }
    
    protected void findLinksEmails(Document doc) {
        Elements links = doc.select("a[href]");
        //System.out.println("\nLinks: (%d)", links.size());
        for (Element link : links) {
            //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
            String content = link.attr("abs:href").toString();
            String emailPre = "mailto:";
            if (content.contains(emailPre)){
                String newStr;
                newStr = link.attr("abs:href").replace(emailPre,"");
                newStr = newStr.replaceAll(" ","");
                String[] newStrClean = newStr.split("\\?");
                newStr = newStrClean[0];
                Manager.checkEmail(newStr);
                //System.out.println("Ho trovato questa email: "+newStr);
            } else {
            Manager.checkURL(link.attr("abs:href"));
            }
        }
    }
    
    protected void findEmails(Document doc) {
        Elements emails = doc.select("a[href]");
        print("\nEmails: (%d)", emails.size());
        for (Element email : emails) {
            Manager.addEmail(email.attr(""));
        }
    }
    
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
     
     private static String trim(String s, int width) {
        if (s.length() > width){
            return s.substring(0, width-1) + ".";
        } else {
            return s;
        }
    }
}