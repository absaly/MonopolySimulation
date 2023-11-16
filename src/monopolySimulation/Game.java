package monopolySimulation;

import enums.Strategy;

public class Game {
	
	private Player player;
	private Board board;
	private int numberOfTurns;
	private Strategy strategy;
	
	public Game(Player player, Board board, int numberOfTurns, Strategy strategy) {
		this.player = player;
		this.board = board;
		this.numberOfTurns = numberOfTurns;
		this.strategy = strategy;
		
		// TODO not sure if complete.
	}
	
	public void takeTurn() {
		// TODO
	}
	
	public boolean checkIfInJail() {
		return false; // TODO
	}
	
	public boolean checkForDoubles() {
		return false; // TODO
	}
	
	public int[] roleDide() {
		return null; // TODO
	}
	
	public void movePlayer() {
		// TODO
	}
	
	public Card drawCard() {
		return null; // TODO
	}
	
	public void discardCard() {
		// TODO
	}
	
	public void checkGameState() {
		// TODO
	}
	
}
