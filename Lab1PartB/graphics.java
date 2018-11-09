package Lab1PartB;
import java.awt.Color;
import static java.awt.Color.*;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Random;
import studijosKTU.ScreenKTU;
import java.awt.event.MouseEvent;
import java.lang.StringBuilder;
import java.util.*;
public class graphics extends ScreenKTU {
    static private final Fonts demofont = Fonts.boldC;
    ScreenKTU sc;
    int Xsize;
    int Ysize;
    Color Color1, Color2, Color3, Color4, Color5, Color6;
    String Keyboard;
    char correctAns[];
    int answered;

    public graphics(int xsize, int ysize) {
        super(20, 20, xsize, ysize, demofont, Grid.OFF);
        Xsize = xsize;
        Ysize = ysize;
        Color1 = new Color(0xE5989B);
        Color2 = new Color(0x6D6875);
        Color3 = new Color(0xFFEBE7);
        Color4 = new Color(0xFFB4A2);
        Color5 = new Color(0xFFCDB2);
        Color6 = new Color(0xFCE9E5);
        answered = 0;
        setTitle("\u00a9 KTU IF \u00a9 Kartuvės");
        setColors(Color1, Color2);
        fillRect(0, 0, Xsize, Ysize);
        setColors(Color5, Color2);
        fillBorder(-1, -1, Xsize+1, Ysize+1);
        String word = FindNewWord();
        correctAns = word.toCharArray();
        String keyboard = GenerateKeyboard(20, word);
        keyboard = ShuffleString(keyboard);
        Keyboard = keyboard;
        PrintKeyboard(20, keyboard);
        refresh();
    }
    
    public String FindNewWord()
    {   
        String output = "tempword";
        boolean ilgis = true;
        String abicele= "AaĄąBbCcČčDdEeĘęĖėFfGgHhIiĮįYyJjKkLlMmNnOoPpRrSsŠšTtUuŲųŪūVvZzŽž";
        while(ilgis )
        {
            output = Lab1B.RandomWord();
            if(output.length() <= 9 && output.length() >= 4)
            {
                boolean skipToNext = false;
                for(char raide : output.toCharArray())
                    if(-1 == abicele.indexOf(raide)){
                        skipToNext = true;
                        break;
                    }
                if(skipToNext == true)
                    continue;
                ilgis = false;
            }   
        }
        output = output.toLowerCase();
        setColors(Color4, Color2);
        fillRect(Xsize/4-1, Ysize/2-output.length()/2-1, 3, output.length()+2);
 
        setColors(Color1, Color2);
        for(int i=0; i < output.length(); i++)
        {
           print(Xsize/4, i+Ysize/2-output.length()/2, "*");
           //print(1+Xsize/4, i+Ysize/2-output.length()/2, zodis[i]);
        }
        refresh();
        
        return output;

    }

    public String GenerateKeyboard(int MaxKeys, String zodis)
    {
        String chars = "aąbcčdeęėfghiįyjklmnoprsštuųūvzž";
        Set set = new HashSet(); 
        StringBuilder keyboard = new StringBuilder();
        for(int i=0; i<zodis.length(); i++)
        {
            if(!set.contains(zodis.charAt(i)))
            {
                set.add(zodis.charAt(i));
                keyboard.append(zodis.charAt(i));
            }
        }
        Random selector = new Random();
        for(int i=keyboard.length(); i < MaxKeys;){
            char randomChar =  chars.charAt(selector.nextInt(keyboard.length()-1));
            if(!set.contains(randomChar)){
                keyboard.append(randomChar);
                set.add(randomChar);
                i++;
            }
       }
       return keyboard.toString();
    }
    public void PrintKeyboard(int MaxKeys, String keyboard)
    {
        int KeyboardX1, KeyboardX2, KeyboardY1, KeyboardY2;
        KeyboardX1 = Ysize/10;
        KeyboardX2 = Ysize - Ysize/10 - KeyboardX1;
        KeyboardY1 = Xsize/2;
        KeyboardY2 = Xsize/8*3;
        setColors(Color4, Color2);
        fillBorder(KeyboardY1+1, KeyboardX1+1, KeyboardY2, KeyboardX2);
        setColors(Color3, Color2);
        fillRect(KeyboardY1, KeyboardX1, KeyboardY2, KeyboardX2);
        char kb[] = keyboard.toCharArray();
        for(int i=0; i < MaxKeys/10; i++)
        {
            for(int y=0; y < MaxKeys/2; y++)
            {
                print(KeyboardY1+i*3+2, KeyboardX1+y*3+2, kb[y+(i*10)]);
            }
        }
    }   
    @Override
    public void mouseClicked(MouseEvent e) {
         int Y = e.getY();
         int X = e.getX();
         int Ykb = (e.getX()/cellW -25+2)/3+5;
         int Xkb = (e.getY()/cellH-5-10)/3;
         System.out.println(X);
         System.out.println(Y);
         if(Xkb >= 0 && Xkb < 2 && Ykb >= 0 && Ykb < 10 )
         {
            System.out.println(Keyboard.charAt(Xkb*10+Ykb));
            CheckIfCorrect(Xkb*10+Ykb);

         }
        
     }
    public void CheckIfCorrect(int x)
    {
        System.out.println(x);
        for(int i=0;i<correctAns.length;i++)
        {
            System.out.println(correctAns[i] == Keyboard.charAt(x));
            if(correctAns[i] == Keyboard.charAt(x))
            {
                PrintCorrect(i);
                answered++;
                if(answered == correctAns.length)
                    Win();
                correctAns[i] = '0';

            }
        }
    }
    public void PrintCorrect(int x)
    {
        setColors(Color3, Color2);
        print(Xsize/4, x+Ysize/2-correctAns.length/2, correctAns[x]);
        refresh();
    }

    public void Win()
    {
        int KeyboardX1, KeyboardX2, KeyboardY1, KeyboardY2;
        KeyboardX1 = Ysize/10;
        KeyboardX2 = Ysize - Ysize/10 - KeyboardX1;
        KeyboardY1 = Xsize/2;
        KeyboardY2 = Xsize/8*3;
        setColors(Color3, Color2);
        fillRect(KeyboardY1, KeyboardX1, KeyboardY2, KeyboardX2);
        char zodis[] = new String("Laimėjai!").toCharArray();
        for(int i=0; i < zodis.length; i++)
        {
           print(Xsize/8*7-2, i+Ysize/2-zodis.length/2, zodis[i]);
        }         
    }
    public String ShuffleString(String target)
    {
        String output = new String();
        List<String> temp = Arrays.asList(target.split(""));
        Collections.shuffle(temp);
        for(String letter : temp)
            output += letter;
        return output;
    }

}
