package main.client.controller;

public class Opponent {
	private final String name;
	private int amountCards;
	private int score;
	private final int id;

	public Opponent(String name, int amountCards, int score, int id) {
		this.name = name;
		this.amountCards = amountCards;
		this.score = score;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAmountCards() {
		return amountCards;
	}

	public void setAmountCards(int i) {
		amountCards = i;
	}

	public int getScore() {
		return score;
	}
}