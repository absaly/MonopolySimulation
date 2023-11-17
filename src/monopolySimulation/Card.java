package monopolySimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import enums.ChanceCards;
import enums.CommunityChestCards;
import enums.DeckType;

/**
 * Represents a card in the game Monopoly based of the card type.
 * 
 * @author Abbas Al-Younis, Tanner Durrant, Erin Mortensen, Trenton Stratton
 */
public class Card {
	
	private String cardName;
	private DeckType cardDeckType;
	
	/**
	 * Constructs a card with its name and deck type.
	 * 
	 * @param name
	 * @param deckType
	 */
	public Card(String name, DeckType deckType) {
		this.cardName = name;
		this.cardDeckType = deckType;
	}

	/**
	 * Returns card name.
	 * 
	 * @return cardName
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * Returns deck type of card.
	 * 
	 * @return cardDeckType
	 */
	public DeckType getCardDeckType() {
		return cardDeckType;
	}
	
	/**
	 * Returns community deck.
	 * 
	 * @return Stack of cards
	 */
	public static Stack<Card> getCommunityChestDeck() {
		CommunityChestCards[] cards = CommunityChestCards.values();
		
		Stack<Card> deck = new Stack<>();
		for (CommunityChestCards card : cards) 
			deck.add(new Card(card.toString(), DeckType.COMMUNITY));
		
		return deck;
	}
	
	/**
	 * Returns chance deck.
	 * 
	 * @return Stack of cards
	 */
	public static Stack<Card> getChanceDeck() {
		ChanceCards[] cards = ChanceCards.values();
		
		Stack<Card> deck = new Stack<>();
		for (ChanceCards card : cards) 
			deck.add(new Card(card.toString(), DeckType.CHANCE));
		
		return deck;
	}
	
	/**
	 * Returns string representation of card.
	 */
	@Override
	public String toString() {
		return "(" + cardName + ", " + cardDeckType + ")";
	}

	// Test client of chance deck being created.
	public static void main(String[] args) {
		ChanceCards[] chance = ChanceCards.values();
		
		List<Card> chanceDeck = new ArrayList<>();
		
		for (ChanceCards c : chance) {
			chanceDeck.add(new Card(c.toString(), DeckType.CHANCE));
		}
		
		System.out.println(chanceDeck);
	}
	
	
}
