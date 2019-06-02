import java.util.HashMap;
import java.util.Map;

import Node.Node;

public class DefineTable {
	
	private static Map<String, Node> defineTable = new HashMap<String, Node>();
	
	public static Node lookupTable(String id) {
		return defineTable.get(id);
	}
	
	public static void define(String id, Node value) {
		defineTable.put(id, value);
	}
}
