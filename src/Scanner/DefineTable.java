package Scanner;
import java.util.HashMap;
import java.util.Map;
import Node.Node;
import Node.ListNode;

public class DefineTable {

	private static Map<String, Node> defineTable = new HashMap<String, Node>();

	public static Node lookupTable(String id) {
		return defineTable.get(id);
	}

	public static void define(String id, Node value) {
		// if same key is in table, change the value
		defineTable.put(id, value);
	}

	public static void delete(String id){
		defineTable.remove(id);
	}

	// 테이블의 모든 내용을 출력하는 디버깅 용 함수
	public static void Debugging() {
		defineTable.forEach((string, value) -> {
			System.out.print(string + ": ");
			if(value instanceof ListNode) {
				ListNode lnode = (ListNode) value;
				System.out.println(lnode.toString(lnode, true));
			}
			else System.out.println(value.getClass());
		});
	}
}
