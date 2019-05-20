package Node;

import Token.*;
import java.util.HashMap;
import java.util.Map;

public class FunctionNode implements ValueNode {

	public enum FunctionType {

		DEFINE {
			TokenType tokenType() {
				return TokenType.DEFINE;
			}
		},
		LAMBDA {
			TokenType tokenType() {
				return TokenType.LAMBDA;
			}
		},
		COND {
			TokenType tokenType() {
				return TokenType.COND;
			}
		},
		QUOTE {
			TokenType tokenType() {
				return TokenType.QUOTE;
			}
		},
		CAR {
			TokenType tokenType() {
				return TokenType.CAR;
			}
		},
		CDR {
			TokenType tokenType() {
				return TokenType.CDR;
			}
		},
		CONS {
			TokenType tokenType() {
				return TokenType.CONS;
			}
		},
		ATOM_Q {
			TokenType tokenType() {
				return TokenType.ATOM_Q;
			}
		},
		NULL_Q {
			TokenType tokenType() {
				return TokenType.NULL_Q;
			}
		},
		NOT {
			TokenType tokenType() {
				return TokenType.NOT;
			}
		},
		EQ_Q {
			TokenType tokenType() {
				return TokenType.EQ_Q;
			}
		};
		
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();

		static FunctionType getFunctionType(FunctionType tType) {
			return fromTokenType.get(tType);
		}

		static {
			for (FunctionType bType : FunctionType.values()) {
				fromTokenType.put(bType.tokenType(), bType);
			}
		}

		abstract TokenType tokenType();
	}

	public FunctionType value;
	
	@Override
	public String toString() {

		return value.name();
	}

	public void setValue(FunctionType tType) {
		FunctionType fType = FunctionType.getFunctionType(tType);
		value = fType;
	}
}
