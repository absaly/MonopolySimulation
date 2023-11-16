package monopolySimulation;

import java.util.ArrayList;

public class Board {
	
	private ArrayList<Card> chanceDeck;
	private ArrayList<Card> communityDeck;
	private int[] properties;
	private ArrayList<Card> chanceDiscardPile;
	private ArrayList<Card> communityDiscard;
	
	public Board(ArrayList<Card> chanceDeck, ArrayList<Card> communityDeck, int[] properties,
			ArrayList<Card> chanceDiscardPile, ArrayList<Card> communityDiscard) {
		this.chanceDeck = chanceDeck;
		this.communityDeck = communityDeck;
		this.properties = properties;
		this.chanceDiscardPile = chanceDiscardPile;
		this.communityDiscard = communityDiscard;
		
		// TODO
	}
	
	public boolean checkChanceDeckEmpty() {
		return false; // TODO
	}
	
}
