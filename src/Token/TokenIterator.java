package Token;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import Scanner.*;

public class TokenIterator implements Iterator<Token> {
	private final ScanContext context;
	private Optional<Token> nextToken;
	
	public TokenIterator(ScanContext context) {
		this.context = context;
		nextToken = readToNextToken(context);
	}

	@Override
	public boolean hasNext() {
		return nextToken.isPresent();
	}

	@Override
	public Token next() {
		if ( !nextToken.isPresent() ) {
			throw new NoSuchElementException();
		}
		
		Token token = nextToken.get();
		nextToken = readToNextToken(context);
		
		return token;
	}

	private Optional<Token> readToNextToken(ScanContext context) {
		State current = State.START;
		while ( true ) {
			TransitionOutput output = current.transit(context);
			if ( output.nextState() == State.MATCHED ) {
				return output.token();
			}
			else if ( output.nextState() == State.FAILED ) {
				throw new ScannerException();
			}
			else if ( output.nextState() == State.EOS ) {
				return Optional.empty();
			}
			
			current = output.nextState();
		}
	}
}
