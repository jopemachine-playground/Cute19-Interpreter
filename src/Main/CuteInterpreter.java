package Main;

import Node.*;
import Node.FunctionNode.FunctionType;
import Scanner.CuteParser;
import Scanner.DefineTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CuteInterpreter {

	private CuteParser parser;

	private Stack<String> temporalVariables = new Stack();

	public CuteInterpreter(String input){
		this.parser = new CuteParser(input);
	}

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
			ListNode rootExprList = ((ListNode)(rootExpr));

			if(rootExprList.car() instanceof FunctionNode) {
				FunctionNode rootExprFunc = ((FunctionNode)(rootExprList.car()));
				// '(' 다음에 나오는 첫 번째 원소가 lambda일 때
				if(rootExprFunc.funcType == FunctionType.LAMBDA) {
					System.out.println("Case 1");

					ListNode argumentNodes = (ListNode) (rootExprList.cdr().car());
					ListNode expression = rootExprList.cdr().cdr();

					if(!(argumentNodes.car() instanceof IdNode)){
						System.out.println("Case 1-2");
						ListNode temp = (ListNode) stripList(expression);
						unBindArguments(temporalVariables);
						temporalVariables.clear();
						return runList(temp);
					}
					else {
						System.out.println("Case 1-1");
					}

					return rootExpr;
				}
			}

			else if(rootExprList.car() instanceof ListNode) {

				ListNode rootExprInList = (ListNode) rootExprList.car();

				parser = new CuteParser(rootExprInList.toString(rootExprInList, true));

				ListNode valueNode = ((ListNode) rootExpr).cdr();

				if(rootExprInList.car() instanceof FunctionNode) {
					FunctionNode rootExprFunc = ((FunctionNode)(rootExprInList.car()));

					ListNode actualParameterList = rootExprList.cdr();
					// '(' 다음에 나오는 첫 번째 원소가 lambda이고 인자도 함께 주어지는 경우
					if(rootExprFunc.funcType == FunctionType.LAMBDA) {

						// 임시 변수 테이블에 할당한다. (argument가 괄호로 감싸있다고 가정함)
						ListNode argumentNodes = (ListNode) (rootExprInList.cdr().car());
						ListNode expression = rootExprInList.cdr().cdr();

						// 재귀함수를 돌면서 인자들과 value을 전부 테이블에 등록한 후 다시 파싱한 후 돌아옴

						ListNode resultExpression = (ListNode) stripList(expression);

						if(argumentNodes.car() instanceof IdNode){

							System.out.println("Case 2-1");
							bindArguments(argumentNodes, valueNode);
							DefineTable.Debugging();

							Node newParseTree = parser.parseExpr();

							return runExpr(newParseTree);
						}

						// IDNode가 아닌 경우, 이제 그냥 runList로 표현식을 리턴해주면 된다.
						else {
							System.out.println("Case 2-2");
							unBindArguments(temporalVariables);
							temporalVariables.clear();
							return runList(resultExpression);
						}

					}
				}
			}

			return runList((ListNode) rootExpr);
		} else
			errorLog("run Expr error");
		return null;
	}

	private void bindArguments(ListNode arguments, ListNode values){

		IdNode idNode = (IdNode) arguments.car();
//		System.out.println("임시 변수 바인딩");
		DefineTable.define(idNode.toString(), values.car());

		temporalVariables.push(idNode.toString());

		if(arguments.cdr() == ListNode.EMPTY_LIST){
			return;
		}

		bindArguments(arguments.cdr(), values.cdr());
	}

	private void unBindArguments(Stack<String> arguments){

		DefineTable.delete(arguments.pop());

		if(arguments.size() == 0){
			return;
		}

		unBindArguments(arguments);
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

				Node argumentNodes;
				Node valueFunctionNode;

				argumentNodes = (ListNode) (operand.car());
				valueFunctionNode = (((ListNode) (operand.cdr().car())));

				System.out.println("실행");


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
