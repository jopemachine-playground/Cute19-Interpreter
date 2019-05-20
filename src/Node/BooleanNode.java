package Node;

public class BooleanNode implements ValueNode{

	Boolean value;
	
	public static BooleanNode FALSE_NODE = new BooleanNode(false);
	public static BooleanNode TRUE_NODE = new BooleanNode(true);
	
	private BooleanNode(Boolean b) {
		value = b;
	}
	
	@Override
	public String toString() {
		return value ? "#T" : "#F";
	}

}
