package DeathAndTaxes;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
/**
 * This class reads files which describe a collection of utilites.
 *
 * This class parses files of the following form:
 * <ul>
 *   <li>File 	= Utility*</li>
 *   <li>Utility 	= code ',' name ',' monthlyPrice ';'</li>
 *   <li>code 	= number</li>
 *   <li>name		= alphaNumString</li>
 *   <li>monthlyPrice 	= number</li>
 * </ul>
 */
public class UtilityFileReader{
	private Token[] tokens;
	private int crrPos;
	private ArrayList<Utility> utilities = new ArrayList<>();

	/**
	 * <b>main</b> is mostly used for testing.
	 *
	 * This method reads command line arguments and interprets them as file
	 * names. Then it proceeeds to parse the files and print string 
	 * representations of all utilities found within, assuming files exist and
	 * are correct.
	 *
	 * If a file happens to be incorrect, parsing is aborted and an error 
	 * message is printed to standard error stream.
	 * */
	public static void main(String[] args){
		UtilityFileReader self = new UtilityFileReader();
		for(int i = 0; i < args.length; i++){
			Utility[] parsedUtils = self.parseUtilityFile(args[i]);
			if(parsedUtils == null){
				System.err.println("Failed to parse file " + args[i] 
						+ ".\n Make sure it exists and is correct.");
				continue;
			}
			for(int j = 0; j < parsedUtils.length; j++){
				System.out.println("Util #" + j 
						+ ":\n" + "\tCode: " + parsedUtils[j].code 
						+ "\n\tName: " + parsedUtils[j].name 
						+ "\n\tMonthly price: "+ parsedUtils[j].monthlyPrice);
			}
		}
	}
	public UtilityFileReader(){}
	/**
	 * Parses a utility file and returns an array of utilities as described in 
	 * the file.
	 * */
	public Utility[] parseUtilityFile(String fileName){
		try{
			String txt = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			Tokeniser tkn = new Tokeniser();
			tokens = tkn.tokenise(txt);
			crrPos = 0;
			while(!EOS()){
				Utility util = parseUtility();
				if(util == null) return null;
				utilities.add(util);
			}
			Utility[] ret = new Utility[utilities.size()];
			utilities.toArray(ret);
			return ret;
		} catch(IOException ex){
			return null;
		}
	}
	private final boolean EOS(){
		return crrPos >= tokens.length;
	}
	private final Token crrToken(){
		return tokens[crrPos];
	}
	//code ',' name ',' price ';'
	private Utility parseUtility(){
		//Input validation
		if(tokens.length - crrPos < 6) return null;
		boolean isValid = true;
		isValid &= isInteger(tokens[crrPos]);
		isValid &= tokens[crrPos + 1].str.equals(",");
		isValid &= tokens[crrPos + 2].type == TokenType.QuotedString 
				|| tokens[crrPos + 2].type == TokenType.AlphaNumeric;
		isValid &= tokens[crrPos + 3].str.equals(",");
		isValid &= isInteger(tokens[crrPos + 4]);
		isValid &= tokens[crrPos + 5].str.equals(";");
		if(!isValid) return null;
		//Reading data
		int code = Integer.parseInt(crrToken().str);
		crrPos++;
		crrPos++;
		String name = crrToken().str;
		crrPos++;
		crrPos++;
		int monthlyPrice = Integer.parseInt(crrToken().str);
		crrPos += 2;
		return new Utility(code, name, monthlyPrice);
	}
	private static final boolean isInteger(Token tkn){
		if(tkn.type != TokenType.Number){
			return false;
		} else {
			for(int i = 0; i < tkn.str.length(); i++){
				if(tkn.str.charAt(i) == '.')
					return false;
			}
		}
		return true;
	}
}
