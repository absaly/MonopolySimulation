package monopolySimulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import edu.princeton.cs.algs4.Knuth;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Queue;
import enums.ChanceCards;
import enums.CommunityChestCards;
import enums.DeckType;

public class Card {
	private String cardName;
	private DeckType cardDeckType;
	private CommunityChestCards communtiyCard;
	private ChanceCards chanceCard;
	
	
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
	 * Returns a community chest card.
	 * 
	 * @return community card
	 */
	public CommunityChestCards getCommuntiyCard() {
		return communtiyCard;
	}

	/**
	 * Sets the community card.
	 * 
	 * @param communtiyCard
	 */
	public void setCommuntiyCard(CommunityChestCards communtiyCard) {
		this.communtiyCard = communtiyCard;
	}

	/**
	 * Returns chance card.
	 * 
	 * @return chance card
	 */
	public ChanceCards getChanceCard() {
		return chanceCard;
	}

	/**
	 * Sets the chance card.
	 * 
	 * @param chanceCard
	 */
	public void setChanceCard(ChanceCards chanceCard) {
		this.chanceCard = chanceCard;
	}
	
	/**
	 * Returns community deck.
	 * 
	 * @return Stack of cards
	 */
	public static Queue<Card> getCommunityChestDeck() {
		CommunityChestCards[] cards = CommunityChestCards.values();
		
		Queue<Card> deck = new Queue<>();
		for (CommunityChestCards card : cards) {
			Card c = new Card(card.toString(), DeckType.COMMUNITY);
			c.setCommuntiyCard(card);
			deck.enqueue(c);
		}
		
		return deck;
	}
	

	/**
	 * Returns chance deck.
	 * 
	 * @return Stack of cards
	 */
	public static Queue<Card> getChanceDeck() {
		ChanceCards[] cards = ChanceCards.values();
		
		Queue<Card> deck = new Queue<>();
		for (ChanceCards card : cards)  {
			Card c = new Card(card.toString(), DeckType.CHANCE);
			c.setChanceCard(card);
			deck.enqueue(c);
		}
		
				
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
