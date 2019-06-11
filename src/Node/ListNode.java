package Node;

import java.util.List;

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

	public default String toString(ListNode cdr, boolean first){

		String firstL = "";

		if(first) firstL = "( ";

		if(cdr == ListNode.EMPTY_LIST){
			return " )";
		}

		if(cdr.car() instanceof ListNode){
			ListNode cdrInlist = (ListNode) cdr.car();
			return "( " + cdrInlist.toString(cdrInlist, false) + " " + this.toString(cdr.cdr(), false);
		}

		return firstL + cdr.car().toString() + " " +  this.toString(cdr.cdr(), false);
	}
}
