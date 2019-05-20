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
    public static Iterator<Token> scan(File file) throws FileNotFoundException {
        ScanContext context = new ScanContext(file);
        return new TokenIterator(context);
    }

    // return tokens as a Stream 
    public static Stream<Token> stream(File file) throws FileNotFoundException {
        Iterator<Token> tokens = scan(file);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
}