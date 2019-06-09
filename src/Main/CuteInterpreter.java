package Main;

import Node.*;
import Scanner.DefineTable;

public class CuteInterpreter {
	
	private void errorLog(String err) {
		Console console = Console.getInstance();
		console.setErrBuffer(err);
	}

	public Node runExpr(Node rootExpr) {

		if (rootExpr == null) {
			return null;
		}

		if (rootExpr instanceof IdNode) {
			return rootExpr;
		} else if (rootExpr instanceof IntNode) {
			return rootExpr;
		} else if (rootExpr instanceof BooleanNode) {
			return rootExpr;
		} else if (rootExpr instanceof ListNode) {
			return runList((ListNode) rootExpr);
		} else
			errorLog("run Expr error");
		return null;
	}

	private Node runList(ListNode list) {

		if (list.equals(ListNode.EMPTY_LIST))
			return list;

		if (list.car() instanceof FunctionNode) {
			return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
		}
		if (list.car() instanceof BinaryOpNode) {
			return runBinary(list);
		}
		return list;
	}

	private boolean operandCheckQuotedList(ListNode operand) {
		if (!(operand.car() instanceof QuoteNode)) {
			return false;
		}
		return true;
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {

		switch (operator.funcType) {
		case CAR:

			if (operandCheckQuotedList(operand) == false) {
				errorLog("Wrong Function Use");
				return null;
			}

			if (((ListNode) (runQuote(operand))).car() instanceof ListNode) {
				return new QuoteNode(((ListNode) (runQuote(operand))).car());
			} else {
				return ((ListNode) (runQuote(operand))).car();
			}
            
		case CDR:

			if (operandCheckQuotedList(operand) == false) {
				errorLog("Wrong Function Use");
				return null;
			}

			if (((ListNode) (runQuote(operand))).cdr() instanceof ListNode) {
				return new QuoteNode(((ListNode) (runQuote(operand))).cdr());
			} else {
				return ((ListNode) (runQuote(operand))).cdr();
			}

		case CONS:
			Node leftOperand;
			Node rightOperand;

			// 첫 번째 피연산자 처리
			if ((operand.car() instanceof ListNode)) {
				if (((ListNode) (operand.car())).car() instanceof QuoteNode) {
					leftOperand = runQuote((ListNode) operand.car());
				} else {
					leftOperand = null;
					errorLog("Wrong Input 1");
				}
			} else {
				leftOperand = operand.car();
			}

			// 두 번째 피연산자 처리
			if (operand.cdr().car() instanceof ListNode) {
				if (((ListNode) (operand.cdr().car())).car() instanceof QuoteNode) {
					rightOperand = runQuote(((ListNode) (operand.cdr().car())));
				} else {
					rightOperand = null;
					errorLog("Wrong Input 2");
				}
			} else {
				rightOperand = ListNode.cons(operand.cdr().car(), ListNode.EMPTY_LIST);
			}

			return new QuoteNode(ListNode.cons(leftOperand, (ListNode) rightOperand));

		case COND:

			return parsingConditionTree(operand, (ListNode) operand.car());

		case ATOM_Q:

			Node nd = ((QuoteNode) operand.car()).nodeInside();

			if (nd instanceof ListNode && !(nd.equals(ListNode.EMPTY_LIST))) {
				return BooleanNode.FALSE_NODE;
			} else {
				return BooleanNode.TRUE_NODE;
			}

		case NULL_Q:
			
			Node node = ((QuoteNode) operand.car()).nodeInside();

			if (node.equals(ListNode.EMPTY_LIST)) {
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}

		case NOT:
			
			return parsingNotTree(operand);
			
		case EQ_Q:
			
			Node leftNode;
			Node rightNode;
			
			if(!(operand.car() instanceof ListNode)) {
				errorLog("Wrong Input");
				return null;
			}
			
			if(((ListNode)(operand.car())).car() instanceof QuoteNode) {

				leftNode = runQuote((ListNode) operand.car());
				rightNode = runQuote(((ListNode) (operand.cdr().car()))); 
				
				if(leftNode.equals(rightNode)) {
					return BooleanNode.TRUE_NODE; 
				}
				else {
					return BooleanNode.FALSE_NODE;
				}
			}

		case LAMBDA:
			return null;
			
		case DEFINE:
			
			Node keyNode;
			Node valueNode;
			
			if(!(operand.car() instanceof IdNode)) {
				errorLog("Not defined behavior");
				return null;
			}
			
			keyNode = (IdNode) (operand.car());
			
			if((operand.cdr().car()) instanceof ListNode) {
				valueNode = (((ListNode) (operand.cdr().car()))); 
			}
			else {
				valueNode = operand.cdr().car();
			}
			
			DefineTable.define(keyNode.toString(), valueNode);
//			DefineTable.Debugging();
			return null;
			
		default:
			break;
		}
		return null;
	}

	private Node parsingConditionTree(ListNode ramainderListNode, ListNode presentListNode) {

		BooleanNode conditionBoolean = null;

		// 각 리스트의 condition 요소가 List 형태인지 BooleanNode 자체가 들어가 있는지를 판단
		if (presentListNode.car() instanceof BooleanNode) {
			conditionBoolean = (BooleanNode) presentListNode.car();
		} else if (presentListNode.car() instanceof ListNode) {
			ListNode condition = ((ListNode) (presentListNode.car()));
			conditionBoolean = (BooleanNode) runBinary(condition);
		} else {
			errorLog("Wrong Input");
		}

		System.out.println(conditionBoolean);

		// 조건에 맞는 식을 찾는다면 트리 파싱을 종료
		if (conditionBoolean.toString() == "#T") {
			return presentListNode.cdr().car();
		} else if (conditionBoolean.toString() == "#F") {

		}

		// 남은 remainder가 없다면 트리 파싱을 종료
		if (!(ramainderListNode.cdr().car() instanceof ListNode)) {
			return null;
		}

		// 조건에 맞는 식이 없고, 남은 조건이 남아있다면 파싱한다.
		return parsingConditionTree(ramainderListNode.cdr(), (ListNode) ramainderListNode.cdr().car());
	}
	
	private Node parsingNotTree(ListNode listNode) {
		
		if(listNode.car() instanceof BooleanNode) {
			return notOp((BooleanNode)listNode.car());
		}
		else if(listNode.car() instanceof BinaryOpNode) {
			Node result = runBinary(listNode);
			if(result instanceof BooleanNode) {
				return notOp((BooleanNode)result);
			}
			else {
				errorLog("Wrong Input");
				return null;
			}
		}
		
		else if(listNode.car() instanceof ListNode) {
			return parsingNotTree((ListNode) listNode.car());
		}
		else {
			errorLog("Wrong Input");
			return null;
		}
		
	}

	private Node notOp(BooleanNode bNode) {
		if(bNode.toString() == "#T") {
			return BooleanNode.FALSE_NODE;
		}
		else if(bNode.toString() == "#F"){
			return BooleanNode.TRUE_NODE;
		}
		else {
			errorLog("Wrong Input");
			return null;
		}
	}
	private Node stripList(ListNode node) {
		if (node.car() instanceof ListNode && node.cdr() == ListNode.EMPTY_LIST) {
			Node listNode = node.car();
			return listNode;
		} else {
			return node;
		}
	}

	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car();
		ListNode operands = list.cdr();

		switch (operator.binType) {
		case PLUS:
			if (operands.car() instanceof IntNode) {
				Integer operand_1 = ((IntNode) (operands.car())).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 + operand_2) + "");
			} else if (operands.car() instanceof ListNode) {
				Integer operand_1 = ((IntNode) (runExpr(operands.car()))).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 + operand_2) + "");
			}
			break;
		case MINUS:
			if (operands.car() instanceof IntNode) {
				Integer operand_1 = ((IntNode) (operands.car())).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 - operand_2) + "");
			} else if (operands.car() instanceof ListNode) {
				Integer operand_1 = ((IntNode) (runExpr(operands.car()))).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 - operand_2) + "");
			}
			break;
		case TIMES:
			if (operands.car() instanceof IntNode) {
				Integer operand_1 = ((IntNode) (operands.car())).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 * operand_2) + "");
			} else if (operands.car() instanceof ListNode) {
				Integer operand_1 = ((IntNode) (runExpr(operands.car()))).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 * operand_2) + "");
			}
			break;
		case DIV:
			if (operands.car() instanceof IntNode) {
				Integer operand_1 = ((IntNode) (operands.car())).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 / operand_2) + "");
			} else if (operands.car() instanceof ListNode) {
				Integer operand_1 = ((IntNode) (runExpr(operands.car()))).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new IntNode((operand_1 / operand_2) + "");
			}
			break;

		case LT:
			if (operands.car() instanceof IntNode) {
				Integer operand_1 = ((IntNode) (operands.car())).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new BooleanNode(operand_1 < operand_2);
			} else if (operands.car() instanceof ListNode) {
				Integer operand_1 = ((IntNode) (runExpr(operands.car()))).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new BooleanNode(operand_1 < operand_2);
			}

			break;

		case GT:

			if (operands.car() instanceof IntNode) {
				Integer operand_1 = ((IntNode) (operands.car())).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new BooleanNode(operand_1 > operand_2);
			} else if (operands.car() instanceof ListNode) {
				Integer operand_1 = ((IntNode) (runExpr(operands.car()))).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new BooleanNode(operand_1 > operand_2);
			}

			break;

		case EQ:

			if (operands.car() instanceof IntNode) {
				Integer operand_1 = ((IntNode) (operands.car())).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new BooleanNode(operand_1.equals(operand_2));
			} else if (operands.car() instanceof ListNode) {
				Integer operand_1 = ((IntNode) (runExpr(operands.car()))).getValue();
				Integer operand_2 = ((IntNode) (runExpr(operands.cdr().car()))).getValue();

				return new BooleanNode(operand_1.equals(operand_2));
			}

			break;

		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}

}
