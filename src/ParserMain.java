import java.io.*;
import java.util.stream.Stream;
import Node.*;
import Scanner.CuteParser;

public class ParserMain {
    public static final void main(String... args) throws Exception {
        ClassLoader cloader = ParserMain.class.getClassLoader();
        File file = new File(cloader.getResource("as07.txt").getFile()); 
        printTextFile(file);
        
        CuteParser cuteParser = new CuteParser(file);
        NodePrinter nodePrinter = new NodePrinter(cuteParser.parseExpr());
        nodePrinter.prettyPrint();
    }  
    
    public static void printTextFile(File txtFile) {
    	try {
        	FileReader freader = new FileReader(txtFile);	
        	int singleCh = 0;
            while((singleCh = freader.read()) != -1){
                System.out.print((char)singleCh);
            }
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
}

    