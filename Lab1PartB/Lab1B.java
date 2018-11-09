package Lab1PartB;
import java.awt.Color;
import static java.awt.Color.*;
import java.util.Random;
import studijosKTU.ScreenKTU;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;
/**
 *
 * @author Paulius Ratkevičius, KTU IFF-7/12, Fri 09 Nov 2018 04:29:10 AM EET
 */
public class Lab1B {
    public static String RandomWord()
    {
        String output = "error";
        Pattern pattern = Pattern.compile("(?<=\\>).*(?=...Vikižodynas\\<\\/title\\>)");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://lt.wiktionary.org/wiki/Specialus:Atsitiktinis_puslapis").openStream(),"UTF-8"))){
            for(String line; (line = reader.readLine()) != null;)
            {
                Matcher regexmatch = pattern.matcher(line);
                if(regexmatch.find()){
                    output = regexmatch.group(0); 
                }  
            }
        }
        catch(Exception e){}
        finally{}
        return output;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new graphics(30, 50);
    }
}
