package Scanner;
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
	
	// 테이블의 모든 내용을 출력하는 디버깅 용 함수
	public static void Debugging() {
		defineTable.forEach((string, node)->System.out.println("############################################\n" + string + ": " + node.getClass() + "\n############################################"));
	}
}