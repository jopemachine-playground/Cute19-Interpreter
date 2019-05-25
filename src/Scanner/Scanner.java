package Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import Token.*;

public class Scanner {
    // return tokens as an Iterator
    public static Iterator<Token> scan(String input) {
        ScanContext context = new ScanContext(input);
        return new TokenIterator(context);
    }

    // return tokens as a Stream 
    public static Stream<Token> stream(String input) {
        Iterator<Token> tokens = scan(input);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
}