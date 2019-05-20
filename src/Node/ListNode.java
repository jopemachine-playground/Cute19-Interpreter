package Node;

public interface ListNode extends Node{
	static ListNode EMPTY_LIST = new ListNode() {
		@Override
		public Node car() {
			return null;
		}
		@Override
		public ListNode cdr() {
			return null;
		}
	};
	
//	static ListNode END_LIST = new ListNode() {
//		@Override
//		public Node car() {
//			return null;
//		}
//		@Override
//		public ListNode cdr() {
//			return null;
//		}
//	};
	
	static ListNode cons(Node head, ListNode tail) {
		return new ListNode() {
			@Override
			public Node car() {
				return head;
			}
			@Override
			public ListNode cdr() {
				return tail;
			}
		};
	}
	
	public Node car();

	public ListNode cdr();
	
}
