package Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import Node.BinaryOpNode;
import Node.BinaryOpNode.BinType;
import Node.BooleanNode;
import Node.FunctionNode;
import Node.FunctionNode.FunctionType;
import Node.IdNode;
import Node.IntNode;
import Node.ListNode;
import Node.Node;
import Node.QuoteNode;
import Token.Token;
import Token.TokenType;

public class CuteParser {
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node() {};

	public CuteParser(File file) {
		try {
			tokens = Scanner.scan(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		if (!tokens.hasNext()) {
			return null;
		}
		return tokens.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();

		if (t == null) {
			System.out.println("No more token");
			return null;
		}

		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
		
		case APOSTROPHE:
			QuoteNode quoteNode = new QuoteNode(parseExpr());
			ListNode listNode = ListNode.cons(quoteNode, ListNode.EMPTY_LIST);
			return listNode;
			
		case ID:
			return new IdNode(tLexeme);
			
		case INT:
			if (tLexeme == null) {
				System.out.println("???");
			}
			return new IntNode(tLexeme);

		case DIV:
			BinaryOpNode divNode = new BinaryOpNode();
			divNode.binType = BinType.DIV;
			return divNode;
			
		case EQ:
			BinaryOpNode eqNode = new BinaryOpNode();
			eqNode.binType = BinType.EQ;
			return eqNode;
			
		case MINUS:
			BinaryOpNode minusNode = new BinaryOpNode();
			minusNode.binType = BinType.MINUS;
			return minusNode;
			
		case GT:
			BinaryOpNode gtNode = new BinaryOpNode();
			gtNode.binType = BinType.GT;
			return gtNode;
			
		case PLUS:
			BinaryOpNode plusNode = new BinaryOpNode();
			plusNode.binType = BinType.PLUS;
			return plusNode;
			
		case TIMES:
			BinaryOpNode timesNode = new BinaryOpNode();
			timesNode.binType = BinType.TIMES;
			return timesNode;
			
		case LT:
			BinaryOpNode ltNode = new BinaryOpNode();
			ltNode.binType = BinType.LT;
			return ltNode;

		case ATOM_Q:
			FunctionNode atom_qNode = new FunctionNode();
			atom_qNode.funcType = FunctionType.ATOM_Q;
			return atom_qNode;
			
		case CAR:
			FunctionNode carNode = new FunctionNode();
			carNode.funcType = FunctionType.CAR;
			return carNode;
			
		case CDR:
			FunctionNode cdrNode = new FunctionNode();
			cdrNode.funcType = FunctionType.CDR;
			return cdrNode;
			
		case COND:
			FunctionNode condNode = new FunctionNode();
			condNode.funcType = FunctionType.COND;
			return condNode;
			
		case CONS:
			FunctionNode consNode = new FunctionNode();
			consNode.funcType = FunctionType.CONS;
			return consNode;
			
		case DEFINE:
			FunctionNode defineNode = new FunctionNode();
			defineNode.funcType = FunctionType.DEFINE;
			return defineNode;
			
		case EQ_Q:
			FunctionNode eq_qNode = new FunctionNode();
			eq_qNode.funcType = FunctionType.EQ_Q;
			return eq_qNode;
			
		case LAMBDA:
			FunctionNode lambdaNode = new FunctionNode();
			lambdaNode.funcType = FunctionType.LAMBDA;
			return lambdaNode;
			
		case NOT:
			FunctionNode notNode = new FunctionNode();
			notNode.funcType = FunctionType.NOT;
			return notNode;
			
		case NULL_Q:
			FunctionNode null_qNode = new FunctionNode();
			null_qNode.funcType = FunctionType.NULL_Q;
			return null_qNode;
			
		case FALSE:
			return BooleanNode.FALSE_NODE;
			
		case TRUE:
			return BooleanNode.TRUE_NODE;
			
		case L_PAREN:
			return parseExprList();
			
		case R_PAREN:
			return END_OF_LIST;
			
		case QUOTE:
			return new QuoteNode(parseExpr());

		default:
			System.out.println("Parsing Error!");
			return null;

		}
	}
	
	private ListNode parseExprList(){
		Node head = parseExpr();
		
		if(head == null) {
			return null;
		}
		if(head == END_OF_LIST) {
			return ListNode.EMPTY_LIST;
		}
		
		ListNode tail = parseExprList();
		
		if(tail == null) {
			return null;
		}

		return ListNode.cons(head, tail);
	}

}
