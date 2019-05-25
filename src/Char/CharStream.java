package Char;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import Scanner.ScannerException;

public class CharStream {
	private final Reader reader;
	private Character cache;
	
	// 실제론 존재하지 않는 가상의 파일 객체를 만들어 사용한다
	public static CharStream from(String input){
		File temp = new File("test.txt");
		FileWriter writer;
		try {
			writer = new FileWriter(temp);
			writer.write(input);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			return new CharStream(new FileReader(temp));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
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
