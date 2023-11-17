package monopolySimulation;


import java.util.Stack;
import java.util.List;

public class Board {
	
	private Stack<Card> chanceDeck;
	private Stack<Card> communityDeck;
	private int[] properties;
	private Stack<Card> chanceDiscardPile;
	private Stack<Card> communityDiscard;
	
	public Board(int[] properties) {
		this.chanceDeck = Card.getChanceDeck();
		this.communityDeck = Card.getCommunityChestDeck();
		this.properties = properties;
		
		// TODO
	}
	
	public void addToChanceDiscardPile() {
		chanceDiscardPile.push(chanceDeck.pop());
	}
	
	public boolean checkChanceDeckEmpty() {
		return false; // TODO
	}
	
}
