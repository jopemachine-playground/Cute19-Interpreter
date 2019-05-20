package Char;


public class Char {
	private final char value;
	private final CharacterType type;

	public enum CharacterType {
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	static Char of(char ch) {
		return new Char(ch, getType(ch));
	}
	
	static Char end() {
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}
	
	public char value() {
		return this.value;
	}
	
	public CharacterType type() {
		return this.type;
	}
	
	private static CharacterType getType(char ch) {
		int code = (int)ch;
		if ( Character.isLetter(ch) || ch == '?') { 
			return CharacterType.LETTER;
		}
		
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT;
		}
		
		switch ( ch ) {
			case '-': case '+': case '*': case '/':
			case '(': case ')':
			case '<': case '=': case '>':
			case '#': case '\'':
				return CharacterType.SPECIAL_CHAR;
		}
		
		if ( Character.isWhitespace(ch) ) {
			return CharacterType.WS;
		}
		
		throw new IllegalArgumentException("input=" + ch);
	}
}
