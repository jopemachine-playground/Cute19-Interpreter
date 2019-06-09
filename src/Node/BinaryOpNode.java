package Node;

import Token.*;
import java.util.HashMap;
import java.util.Map;

public class BinaryOpNode implements ValueNode {

	public enum BinType {
		MINUS {
			TokenType tokenType() {
				return TokenType.MINUS;
			}
		},
		PLUS {
			TokenType tokenType() {
				return TokenType.PLUS;
			}
		},
		TIMES {
			TokenType tokenType() {
				return TokenType.TIMES;
			}
		},
		DIV {
			TokenType tokenType() {
				return TokenType.DIV;
			}
		},
		LT {
			TokenType tokenType() {
				return TokenType.LT;
			}
		},
		GT {
			TokenType tokenType() {
				return TokenType.GT;
			}
		},
		EQ {
			TokenType tokenType() {
				return TokenType.EQ;
			}
		};

		private static Map<TokenType, BinType> fromTokenType = new HashMap<TokenType, BinType>();

		static BinType getBinType(TokenType tType) {
			return fromTokenType.get(tType);
		}

		static {
			for (BinType bType : BinType.values()) {
				fromTokenType.put(bType.tokenType(), bType);
			}
		}

		abstract TokenType tokenType();
	}
	
	public BinType binType;
	
	public void setValue(TokenType tType) {
		BinType bType = BinType.getBinType(tType);
		binType = bType;
	}
	
	@Override
	public String toString() {
		return toChar(binType);
	}

	static String toChar(BinType type) {
		switch ( type ) {
			case PLUS:
				return "+";
			case MINUS:
				return "-";
			case TIMES:
				return "*";
			case DIV:
				return "/";
			case LT:
				return "<";
			case GT:
				return ">";
			case EQ:
				return "=";

			default:
				throw new IllegalArgumentException("unregistered char: " + type);
		}
	}
	
}
