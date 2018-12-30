package exceptions;

public class WrongCardOnPileException extends RuntimeException {
		public WrongCardOnPileException (String message) {
			super(message);
		}
	}