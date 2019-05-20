package Node;

public class IdNode implements ValueNode{
	
	public String idString;
	
	public IdNode(String text) {
		idString = text;
	}
	
	@Override
	public String toString() {
		return idString;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof IdNode)) return false;
		IdNode idNode = (IdNode) o;
		// 수정된 부분
		return idString.equals(idNode.idString);
	}
	
}
