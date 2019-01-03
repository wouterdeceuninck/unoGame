package exceptions;

public class NotInPlayersHand extends RuntimeException {
		public NotInPlayersHand(String message) {
			super(message);
		}
	}