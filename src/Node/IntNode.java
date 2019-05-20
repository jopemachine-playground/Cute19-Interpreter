package Node;

public class IntNode implements ValueNode{
	private Integer value;
	
	public Integer getValue() {
		return value;
	}
	@Override
	public String toString() {
		return value.toString();
	}
	
	public IntNode(String text) {
		this.value = new Integer(text);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IntNode)) return false;
		IntNode intNode = (IntNode) o;
		return intNode.equals(intNode.value);
	}
}
