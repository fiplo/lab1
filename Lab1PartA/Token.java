package DeathAndTaxes;
/**
 * Simple data class which describes a token that a parser can consume.
 * */
public class Token{
	public final String str;
	public final TokenType type;

	public Token(String str, TokenType type){
		this.str = str;
		this.type = type;
	}
}
