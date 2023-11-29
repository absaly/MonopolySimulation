package monopolySimulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;
import enums.ChanceCards;
import enums.CommunityChestCards;

public class Simulation {

	// All constant variables for places in the board
	static final int BOARD_SIZE = 40;
	static final int JAIL = 10;
	static final int GO_TO_JAIL = 30;
	static final int COMMUNITY_CHEST_1 = 2;
	static final int COMMUNITY_CHEST_2 = 17;
	static final int COMMUNITY_CHEST_3 = 33;
	static final int CHANCE_1 = 7;
	static final int CHANCE_2 = 22;
	static final int CHANCE_3 = 36;
	static final int BOARDWALK = 39;
	static final int ILLINOIS_AVE = 24;
	static final int CHARLES = 11;
	static final int RAIL_1 = 5;
	static final int RAIL_2 = 15;
	static final int RAIL_3 = 25;
	static final int RAIL_4 = 35;
	static final int UTILITY_1 = 12;
	static final int UTILITY_2 = 28;

	static int[] board = new int[BOARD_SIZE];
	static int current = 0;
	static Queue<Card> chanceDeck = Card.getChanceDeck();
	static Queue<Card> communityDeck = Card.getCommunityChestDeck();
	static boolean inJail = false;
	static int[] n = { 1_000, 10_000, 100_000, 1_000_000 };
	static List<Card> hand = new ArrayList<>();

	public static void main(String[] args) {
		/*
		 * Strategy A) If you have a Get Out of Jail Free card, you must use it
		 * immediately. If you don’t have the card, then you should immediately assume
		 * you would have paid the $50 fine and gotten out of jail immediately.
		 */
		strategyA();

		/*
		 * Strategy B) If you have a Get Out of Jail Free card, you must use it
		 * immediately. If you don’t have the card, then try to roll doubles for your
		 * next three turns to see if you can get out of jail that way. If you have not
		 * gotten out of jail after three turns, assume you would have paid the $50 fine
		 * on the fourth term and get out of jail on that turn
		 */
		strategyB();
	}

	private static void strategyB() {
		for (int currentTurns = 1; currentTurns <= 10; currentTurns++) {
			System.out.println("Strategy B simulation #" + currentTurns + " of 10");
			for (int i = 0; i < n.length; i++) {
				System.out.println("Strategy B for N = " + n[i]);
				int doubleTurnCount = 0;
				for (int turns = 0; turns < n[i]; turns++) {

					// checks if player is in jail and looks for get out of jail cards
					if (inJail) {
						boolean usedGetOutOfJailCard = false;

						for (Card c : new ArrayList<>(hand)) {
							if (c.getCommuntiyCard() == CommunityChestCards.GET_OUT_OF_JAIL) {
								communityCardAction(c);
								usedGetOutOfJailCard = true;
								hand.remove(c);
							} else if (c.getChanceCard() == ChanceCards.GET_OUT_OF_JAIL) {
								chanceActions(c);
								usedGetOutOfJailCard = true;
								hand.remove(c);
							}
						}

						if (!usedGetOutOfJailCard) {
							for (int j = 0; j < 3; j++) {
								int diceOne = diceRoll();
								int diceTwo = diceRoll();

								if (diceOne == diceTwo) {
									inJail = false;
									break;
								}

								doubleTurnCount++;
							}

							if (doubleTurnCount == 3) {
								inJail = false;
							}
						}
					}

					if (current == COMMUNITY_CHEST_1 || current == COMMUNITY_CHEST_2 || current == COMMUNITY_CHEST_3) {
						drawCommunityCard();
					}

					if (current == CHANCE_1 || current == CHANCE_2 || current == CHANCE_3) {
						drawChanceCard();
					}

					int diceRoll = diceRoll() + diceRoll();
					movePlayer(diceRoll);
				}
				printResults(n[i]);
			}
			Arrays.fill(board, 0);
		}
	}

	/**
	 * Performs strategy A for the simulation.
	 */
	private static void strategyA() {
		for (int currentTurns = 1; currentTurns <= 10; currentTurns++) {
			System.out.println("Strategy A simulation #" + currentTurns + " of 10");
			for (int i = 0; i < n.length; i++) {
				System.out.println("Strategy A for N = " + n[i]);
				for (int turns = 0; turns < n[i]; turns++) {
					// checks if player is in jail and looks for get out of jail cards
					if (inJail) {
						boolean usedGetOutOfJailCard = false;

						for (Card c : new ArrayList<>(hand)) {
							if (c.getCommuntiyCard() == CommunityChestCards.GET_OUT_OF_JAIL) {
								communityCardAction(c);
								usedGetOutOfJailCard = true;
								hand.remove(c);
							} else if (c.getChanceCard() == ChanceCards.GET_OUT_OF_JAIL) {
								chanceActions(c);
								usedGetOutOfJailCard = true;
								hand.remove(c);
							}
						}

						if (!usedGetOutOfJailCard) {
							// If no Get Out of Jail Free card, pay $50 fine and get out of jail immediately
							inJail = false;
						}
					}

					if (current == COMMUNITY_CHEST_1 || current == COMMUNITY_CHEST_2 || current == COMMUNITY_CHEST_3) {
						drawCommunityCard();
					}

					if (current == CHANCE_1 || current == CHANCE_2 || current == CHANCE_3) {
						drawChanceCard();
					}

					int diceRoll = diceRoll() + diceRoll();
					movePlayer(diceRoll);
				}
				printResults(n[i]);
			}
			
			Arrays.fill(board, 0);
		}
	}

	private static void printResults(int lengh) {
		int totalTurns = lengh;
	    for (int i = 0; i < board.length; i++) {
	    	double percentage = ((double) board[i] / totalTurns * 100);
	    	String formattedPercentage = String.format("%.2f", percentage);
	        System.out.println("Spot " + (i+1) + ": " + board[i] + " Percentage: " + formattedPercentage + "%");
	    }
	    System.out.println();

	}

	/**
	 * Performs the action of community card.
	 * 
	 * @param c card in hand
	 */
	private static void communityCardAction(Card c) {
		switch (c.getCommuntiyCard()) {
		case ADVANCE_TO_GO:
			current = 0;
			board[current]++;
			communityDeck.enqueue(c);
			break;
		case BANK_ERROR:
			communityDeck.enqueue(c);
			break;
		case DOCTOR_FEE:
			communityDeck.enqueue(c);
			break;
		case SALE_OF_STOCK:
			communityDeck.enqueue(c);
			break;
		case GET_OUT_OF_JAIL:
			inJail = false;
			communityDeck.enqueue(c);
			break;
		case GO_TO_JAIL:
			inJail = true;
			current = JAIL;
			board[current]++;
			break;
		case HOLIDAY_FUND:
			communityDeck.enqueue(c);
			break;
		case INCOME_TAX:
			communityDeck.enqueue(c);
			break;
		case ITS_YOUR_BIRTHDAY:
			communityDeck.enqueue(c);
			break;
		case LIFE_INSURANCE_MATURES:
			communityDeck.enqueue(c);
			break;
		case HOSPITAL_FEES:
			communityDeck.enqueue(c);
			break;
		case SCHOOL_FEES:
			communityDeck.enqueue(c);
			break;
		case RECEIVE_CONSULTANCY_FEE:
			communityDeck.enqueue(c);
			break;
		case STREET_REPAIR:
			communityDeck.enqueue(c);
			break;
		case WIN_BEAUTY_CONTEST:
			communityDeck.enqueue(c);
			break;
		case INHERIT_100:
			communityDeck.enqueue(c);
			break;
		default:
			System.out.println("Unknown card type");
		}
	}

	private static void chanceActions(Card c) {
		switch (c.getChanceCard()) {
		case ADVANCE_TO_BOARDWALK:
			current = BOARDWALK;
			board[current]++;
			chanceDeck.enqueue(c);
			break;
		case ADVANCE_TO_GO:
			current = 0;
			board[current]++;
			chanceDeck.enqueue(c);
			break;
		case ADVANCE_ILLINOIS_AVENUE:
			current = ILLINOIS_AVE;
			board[current]++;
			chanceDeck.enqueue(c);
			break;
		case ADVANCE_TO_STCHARLES_PLACE:
			current = CHARLES;
			board[current]++;
			chanceDeck.enqueue(c);
			break;
		case ADVANCE_TO_NEAREST_RAILROAD_1:
			nearestRail();
			chanceDeck.enqueue(c);
			break;
		case ADVANCE_TO_NEAREST_RAILROAD_2:
			nearestRail();
			chanceDeck.enqueue(c);
			break;
		case ADVANCE_TO_NEAREST_UTILITY:
			nearestUtil();
			chanceDeck.enqueue(c);
			break;
		case BANK_PAYS_YOU:
			chanceDeck.enqueue(c);
			break;
		case GET_OUT_OF_JAIL:
			inJail = false;
			chanceDeck.enqueue(c);
			break;
		case GO_BACK_3_SPACES:
			movePlayer(-3);
			chanceDeck.enqueue(c);
			break;
		case GO_TO_JAIL:
			inJail = true;
			current = JAIL;
			board[current]++;
			break;
		case MAKE_REPAIRS_ON_PROPERTY:
			chanceDeck.enqueue(c);
			break;
		case SPEEDING_FINE:
			chanceDeck.enqueue(c);
			break;
		case TRIP_TO_READING_RAILROAD:
			current = RAIL_1;
			board[current]++;
			chanceDeck.enqueue(c);
			break;
		case ELECTED_CHAIRMAN:
			chanceDeck.enqueue(c);
			break;
		case BUILDING_LOAN_MATURES:
			chanceDeck.enqueue(c);
			break;
		default:
			System.out.println("Unknown card type");
		}
	}

	private static void nearestUtil() {
		while (current != UTILITY_1 && current != UTILITY_2) {
			current = (current + 1) % BOARD_SIZE;
			board[current]++;
		}
	}

	private static void nearestRail() {
		while (current != RAIL_1 && current != RAIL_2 && current != RAIL_3 && current != RAIL_4) {
			current = (current + 1) % BOARD_SIZE;
			board[current]++;
		}
	}

	private static void drawCommunityCard() {
		Card communityCard = communityDeck.dequeue();

		if (communityCard.getCommuntiyCard() == CommunityChestCards.GET_OUT_OF_JAIL)
			hand.add(communityCard);
		else
			communityCardAction(communityCard);
	}

	private static void drawChanceCard() {
		Card chanceCard = chanceDeck.dequeue();

		if (chanceCard.getChanceCard() == ChanceCards.GET_OUT_OF_JAIL)
			hand.add(chanceCard);
		else
			chanceActions(chanceCard);
	}

	private static void movePlayer(int steps) {
		current = (current + steps) % BOARD_SIZE;
		if (current == GO_TO_JAIL) {
			board[current]++;
			current = JAIL;
			inJail = true;
			board[current]++;
		} else {
			board[current]++;
		}
	}

	/**
	 * Rolls for random number 1-6
	 * 
	 * @return int random dice roll
	 */
	private static int diceRoll() {
		return StdRandom.uniformInt(1, 7);
	}

}
