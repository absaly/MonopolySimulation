package monopolySimulation;

import enums.Action;
import enums.DeckType;

public class Card {
	
	private String cardName;
	private DeckType cardDeckType;
	private Action cardAction;
	
	public Card(String name, DeckType deckType, Action action) {
		this.cardName = name;
		this.cardDeckType = deckType;
		this.cardAction = action;
		
		// TODO not sure if complete.
	}

	public String getCardName() {
		return cardName;
	}

	public DeckType getCardDeckType() {
		return cardDeckType;
	}

	public Action getCardAction() {
		return cardAction;
	}
	
	
	
}
