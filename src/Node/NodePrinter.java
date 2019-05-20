package Node;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class NodePrinter {
	private final String OUTPUT_FILENAME = "output08.txt";

	private StringBuffer sb = new StringBuffer();
	private Node root;

	public NodePrinter(Node root) {
		this.root = root;
	}

	private void printNode(ListNode listNode) {
		if (listNode == ListNode.EMPTY_LIST) {
//			sb.append("( )");
			return;
		}
	
		printNode(listNode.car());		
		sb.append(" ");
		printNode(listNode.cdr());
	
	}
	
	private void printNode(QuoteNode quoteNode){
		if(quoteNode.nodeInside() == null) {
			return;
		}
		
		sb.append("'");
		printNode(quoteNode.nodeInside());
	}
	
	private void printNode(Node node){
		if(node == null) {
			return;
		}

		if(node instanceof ListNode) {

			ListNode ln = (ListNode) node;
			
			if(ln.car() instanceof QuoteNode) {
				printNode(ln.car());	
				printNode(ln.cdr());	
				return;
			}
			sb.append("( ");
			printNode(ln);
			sb.append(")");
		}

		else if(node instanceof QuoteNode){
			QuoteNode qd = (QuoteNode) node;
			printNode(qd);
		}

		else if(node instanceof ValueNode){
			ValueNode vn = (ValueNode) node;
			// sb.append("[" + vn + "]");
			sb.append(vn);
		}
		
		else {
			assert(false);
		}
	
	}
	
	public void prettyPrint() {
		printNode(root);

		try(FileWriter fw = new FileWriter(OUTPUT_FILENAME);
				PrintWriter pw = new PrintWriter(fw)){
			pw.write(sb.toString());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
