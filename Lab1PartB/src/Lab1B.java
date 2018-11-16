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
        Color Color1 = new Color(0xE5989B);
        Color Color2 = new Color(0x6D6875);
        Color Color3 = new Color(0xFFEBE7);
        Color Color4 = new Color(0xFFB4A2);
        Color Color5 = new Color(0xFFCDB2);
        Color Color6 = new Color(0xFCE9E5);
 
        new graphics(30, 50, Color1, Color2, Color3, Color4, Color5, Color6);
    }
}
