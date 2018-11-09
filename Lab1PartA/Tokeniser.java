package DeathAndTaxes;

import java.util.Arrays;
import java.util.ArrayList;
/**
 * This class converts an array of char's into an array of tokens.
 *
 * <p>Valid tokens must match any of the following patterns:
 *   <ul>
 *    <li>Alphanumeric with underscore: [a-zA-Z_]+
 *    <li>Numbers: [0-9]+(\.[0-9]+)?
 *    <li>Everything between double quotes: ".*"
 *    <li>Every other character is a single token
 *   </ul>
 * </p>
 * 
 * */
public class Tokeniser{
	private String text;
	private int crrPos;
	private ArrayList<Token> tokens = new ArrayList<Token>();
		public Tokeniser(){}
	/**
	 * This method takes all command line arguments as file names, tokenises
	 * each of them and prints string representations of the tokens into 
	 * standard output.
	 * */
	public static void main(String[] args){
		Tokeniser tkn = new Tokeniser();
		for(int i = 0; i < args.length; i++){
			Token[] tokens = tkn.tokenise(args[i]);
			if(tokens == null){
				System.err.println("Tokenisation of arg" + i + " failed");
				continue;
			} else {
				System.out.println("Parsed tokens from arg" + i + ":");
			}
			for(int j = 0; j < tokens.length; j++){
				System.out.println("\t\"" + tokens[j].str + "\"");
			}
		}
	}
	private final void addToken(Token token){
		tokens.add(token);
	}
	/**
	 * Turns text into an array of tokens for a parser to read.
	 * */
	public final Token[] tokenise(String txt){
		text = txt;
		crrPos = 0;
		tokens = new ArrayList();		
		while(!EOS()){
			if(tryReadAlphanumeric()) continue;
			if(tryReadNumber()) continue;
			if(tryReadQuotes()) continue;
			if(tryReadOther()) continue;
			break;
		}
		if(!EOS()) return null;
		return Arrays.copyOf(tokens.toArray(), tokens.size(), Token[].class);
	}
	private final char crrChar(){
		return text.charAt(crrPos);
	}
	private final boolean EOS(){
		return crrPos >= text.length();
	}
	private final boolean tryReadAlphanumeric(){
		skipWS();
		int oldPos = crrPos;
		if(EOS()) return false;
		if(!Character.isLetter(crrChar()) && crrChar() != '_') return false;
		while(!EOS() && (crrChar() == '_' || Character.isLetter(crrChar())))
			crrPos++;
		String txt = text.substring(oldPos, crrPos);
		addToken(new Token(txt, TokenType.AlphaNumeric));
		return true;
	}
	private final boolean tryReadNumber(){
		skipWS();
		if(EOS() || !Character.isDigit(crrChar())) return false;
		int oldPos = crrPos;
		while(!EOS() && Character.isDigit(crrChar())) crrPos++;
		if(crrPos + 1 < text.length() 
				&& crrChar() == '.' 
				&& Character.isDigit(text.charAt(crrPos + 1))){
			crrPos++;
			while(!EOS() && Character.isDigit(crrChar()))
				crrPos++;		
		} 
		String txt = text.substring(oldPos, crrPos);
		addToken(new Token(txt, TokenType.Number));
		return true;
	}
	private final boolean tryReadQuotes(){
		if(text.length() - crrPos < 3 || crrChar() != '"') return false;
		int oldPos = crrPos;
		crrPos++;
		skipTo('"');
		if(EOS() || crrChar() != '"'){ 
			crrPos = oldPos;
			return false;
		}
		crrPos++;
		String txt = text.substring(oldPos + 1, crrPos - 1);
		addToken(new Token(txt, TokenType.QuotedString));
		return true;
	}
	private final boolean tryReadOther(){
		skipWS();
		if(!EOS()){
			String txt = text.substring(crrPos, crrPos + 1);
			addToken(new Token(txt, TokenType.Other));
			crrPos++;
			return true;
		}
		return false;
	}
	private final void skipWS(){
		while(!EOS() && Character.isWhitespace(crrChar()))
			crrPos++;
	}
	private final void skipTo(char ch){
		while(!EOS() && crrChar() != ch)
			crrPos++;
	}
}
