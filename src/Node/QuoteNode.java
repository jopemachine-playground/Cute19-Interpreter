package Node;

public class QuoteNode implements Node{
	Node quoted;
	
	public QuoteNode(Node quoted) {
		this.quoted = quoted;
	}
	@Override
	public String toString() {
		return quoted.toString();
	}
	
	public Node nodeInside() {
		return quoted;
	}

}
