package main.exceptions;

public class GamePlayError extends RuntimeException {
		public GamePlayError(String s) {
			super(s);
		}
	}