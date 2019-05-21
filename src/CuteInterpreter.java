import java.io.*;
import java.util.stream.Stream;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import Node.*;
import Scanner.CuteParser;

public class CuteInterpreter {
    public static final void main(String... args) throws Exception {
        ClassLoader cloader = CuteInterpreter.class.getClassLoader();
        File file = new File(cloader.getResource("as08.txt").getFile()); 
        
        CuteParser cuteParser = new CuteParser(file);
        CuteInterpreter interpreter = new CuteInterpreter();
        Node parseTree = cuteParser.parseExpr();
        Node resultNode = interpreter.runExpr(parseTree);
        NodePrinter nodePrinter = new NodePrinter(resultNode);
        nodePrinter.prettyPrint();
    }  
    
    private void errorLog(String err) {
    	System.out.println(err);
    }
    
    public Node runExpr(Node rootExpr) {
    	
    	if(rootExpr == null) {
    		return null;
    	}
    	
    	if(rootExpr instanceof IdNode) {
    		return rootExpr;
    	}
    	else if(rootExpr instanceof IntNode) {
    		return rootExpr;
    	}
    	else if(rootExpr instanceof BooleanNode) {
    		return rootExpr;
    	}
    	else if(rootExpr instanceof ListNode) {
    		return runList((ListNode) rootExpr);
    	}
    	else errorLog("run Expr error");
    	return null;
    }
    
    private Node runList(ListNode list) {
    	
    	if(list.equals(ListNode.EMPTY_LIST))
    		return list;
    	
    	if(list.car() instanceof FunctionNode) {
    		return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
    	}
    	if(list.car() instanceof BinaryOpNode) {
    		return runBinary(list);
    	}
    	return list;
    }
    
    private Node runFunction(FunctionNode operator, ListNode operand) {
    	switch(operator.funcType) {
    		case CAR:
    			if(operand.car() instanceof QuoteNode) {
    				
    			}
    			return operand.car();
    		case CDR:
    			return operand.cdr();
    		case CONS:
    		case COND:
    		case LAMBDA:
    		case DEFINE:
    		case ATOM_Q:
    		case NULL_Q:
    		case NOT:
    		case EQ_Q:
    		case QUOTE:
    			return runQuote(operand);
    		default:
    			break;
    	}
    	return null;
    }
    
    // list.cdr()의 결과가 한 개의 노드라면, ListNode가 아닌 Node가 되게 한다.
    private Node stripList(ListNode node) {
    	if(node.car() instanceof ListNode && node.cdr() == ListNode.EMPTY_LIST) {
    		Node listNode = node.car();
    		return listNode;
    	}
    	else {
    		return node;
    	}
    }
    
    private Node runBinary(ListNode list) {
    	BinaryOpNode operator = (BinaryOpNode) list.car();
    	ListNode operands = list.cdr();
    	
    	switch(operator.binType) {
    		case PLUS:
    			if(operands.car() instanceof IntNode) {
    				Integer operand_1 = ((IntNode)(operands.car())).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 + operand_2) + "");
    			}
    			else if(operands.car() instanceof ListNode) {
    				Integer operand_1 = ((IntNode)(runExpr(operands.car()))).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 + operand_2) + "");
    			}
    			break;
    		case MINUS:
    			if(operands.car() instanceof IntNode) {
    				Integer operand_1 = ((IntNode)(operands.car())).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 - operand_2) + "");
    			}
    			else if(operands.car() instanceof ListNode) {
    				Integer operand_1 = ((IntNode)(runExpr(operands.car()))).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 - operand_2) + "");
    			}
    			break;
    		case TIMES:
    			if(operands.car() instanceof IntNode) {
    				Integer operand_1 = ((IntNode)(operands.car())).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 * operand_2) + "");
    			}
    			else if(operands.car() instanceof ListNode) {
    				Integer operand_1 = ((IntNode)(runExpr(operands.car()))).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 * operand_2) + "");
    			}
    			break;
    		case DIV:
    			if(operands.car() instanceof IntNode) {
    				Integer operand_1 = ((IntNode)(operands.car())).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 / operand_2) + "");
    			}
    			else if(operands.car() instanceof ListNode) {
    				Integer operand_1 = ((IntNode)(runExpr(operands.car()))).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new IntNode((operand_1 / operand_2) + "");
    			}
    			break;
    			
    		case LT:
    			if(operands.car() instanceof IntNode) {
    				Integer operand_1 = ((IntNode)(operands.car())).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new BooleanNode(operand_1 < operand_2);
    			}
    			else if(operands.car() instanceof ListNode) {
    				Integer operand_1 = ((IntNode)(runExpr(operands.car()))).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new BooleanNode(operand_1 < operand_2);
    			}
    			
				break;
				
    		case GT:
    			
    			if(operands.car() instanceof IntNode) {
    				Integer operand_1 = ((IntNode)(operands.car())).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new BooleanNode(operand_1 > operand_2);
    			}
    			else if(operands.car() instanceof ListNode) {
    				Integer operand_1 = ((IntNode)(runExpr(operands.car()))).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new BooleanNode(operand_1 > operand_2);
    			}
    			
				break;

    		case EQ:

    			if(operands.car() instanceof IntNode) {
    				Integer operand_1 = ((IntNode)(operands.car())).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
    				return new BooleanNode(operand_1.equals(operand_2));
    			}
    			else if(operands.car() instanceof ListNode) {
    				Integer operand_1 = ((IntNode)(runExpr(operands.car()))).getValue();
    				Integer operand_2 = ((IntNode)(runExpr(operands.cdr().car()))).getValue();
    				
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
















    