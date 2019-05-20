package Char;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import Scanner.ScannerException;

public class CharStream {
	private final Reader reader;
	private Character cache;
	
	public static CharStream from(File file) throws FileNotFoundException {
		return new CharStream(new FileReader(file));
	}
	
	CharStream(Reader reader) {
		this.reader = reader;
		this.cache = null;
	}
	
	public Char nextChar() {
		if ( cache != null ) {
			char ch = cache;
			cache = null;
			
			return Char.of(ch);
		}
		else {
			try {
				int ch = reader.read();
				if ( ch == -1 ) {
					return Char.end();
				}
				else {
					return Char.of((char)ch);
				}
			}
			catch ( IOException e ) {
				throw new ScannerException("" + e);
			}
		}
	}
	
	void pushBack(char ch) {
		cache = ch;
	}
}
