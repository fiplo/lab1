package Lab1PartB;
import java.awt.Color;
import static java.awt.Color.*;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Random;
import studijosKTU.ScreenKTU;
import java.awt.event.MouseEvent;

public class graphics extends ScreenKTU {
    static private final Fonts demofont = Fonts.boldC;
    ScreenKTU sc;
    int extInd;
    public graphics(int Xsize, int Ysize) {
        super(18, 18, Xsize, Ysize, demofont, Grid.ON);
        
        setTitle("\u00a9 KTU IF \u00a9 Kartuvės");
        setColors(new Color(0xE894BC), new Color(0x3B5249));
        fillRect(0, 0, Xsize, Ysize);
        String word = FindNewWord(Xsize, Ysize);
        refresh();
    }
    
    public String FindNewWord(int Xsize, int Ysize)
    {   
        String output = "tempword";
        boolean ilgis = true;
        string abicele= "AaĄąBbCcČčDdEeĘęĖėFfGgHhIiĮįYyJjKkLlMmNnOoPpRrSsŠšTtUuŲųŪūVvZzŽž";
        while(ilgis )
        {
            output = Lab1B.RandomWord();
            if(output.length() <= 9 && output.length() >= 4)
            {
                foreach(char raide in output.toCharArray())
                {
                    if(!abicele.contains(raide))
                    continue;
                }
            }
            ilgis = false;
        }
        char zodis[] = output.toCharArray();
        setColors(new Color(0xE894BC), new Color(0x3B5249));
        for(int i=0; i < output.length(); i++)
        {
           print(0+Xsize/3, i+Ysize/4, "*");
        }
        refresh();
        return output;

    }

    public void GenerateKeyboard(int MaxKeys, char zodis[], int zodislength)
    {
        String chars = "aąbcčdeęėfghiįyjklmnoprsštuųūvzž"
        for(int i=0; i <= MaxKeys; i++)
        {
           for()  
        }
    }
}
