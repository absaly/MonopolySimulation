package monopolySimulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.Knuth;
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

	static int[] board = new int[BOARD_SIZE]; // game board
	static int current = 0; // current position of the player
	static Queue<Card> chanceDeck = Card.getChanceDeck(); // chance deck
	static Queue<Card> communityDeck = Card.getCommunityChestDeck(); // community deck
	static boolean inJail = false; // are you in jail
	static int[] n = { 1_000, 10_000, 100_000, 1_000_000 }; // how many turns a player will have
	static List<Card> hand = new ArrayList<>(); // Represents player's hand

	static int chanceTimesDrawn = 0; // counts the number of times the chance deck has been drawn from.
	static int communityTimesDrawn = 0; // counts the number of times the community deck has been drawn from.

	static int threeDoubleDiceRollCounter = 0; // counts the number of doubles rolled

	static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) {
		/*
		 * Strategy A) If you have a Get Out of Jail Free card, you must use it
		 * immediately. If you don’t have the card, then you should immediately assume
		 * you would have paid the $50 fine and gotten out of jail immediately.
		 */
		strategyA();
		try (FileWriter fw = new FileWriter("src/monopolySimulation/csvData/StrategyA.csv")) {
			fw.append(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// clear string builder
		sb = new StringBuilder();

		/*
		 * Strategy B) If you have a Get Out of Jail Free card, you must use it
		 * immediately. If you don’t have the card, then try to roll doubles for your
		 * next three turns to see if you can get out of jail that way. If you have not
		 * gotten out of jail after three turns, assume you would have paid the $50 fine
		 * on the fourth term and get out of jail on that turn
		 */
		strategyB();
		try (FileWriter fw = new FileWriter("src/monopolySimulation/csvData/StrategyB.csv")) {
			fw.append(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs strategy B for the simulation.
	 */
	private static void strategyB() {
		// Loops 10 times to run the simulation for strategy B.
		for (int currentTurns = 1; currentTurns <= 10; currentTurns++) {
			shuffleCommunity(); // shuffles deck at the beginning of game.
			shuffleChance();    // ^

			System.out.println("Strategy B simulation #" + currentTurns + " of 10"); // Prints out the current # of the
																					 // simulation
			// Loops length of n array containing how many turns a player has
			for (int i = 0; i < n.length; i++) {
				System.out.println("Strategy B for N = " + n[i]); // prints how many turns the player will have.
				sb.append("Strategy B for N = " + n[i] + "\n");   // adds to the StringBuilder for csv file.
				int doubleTurnCount = 0; // counts doubles for strategy B
				// Runs the simulation for the number of turns the player will have.
				for (int turns = 0; turns < n[i]; turns++) {
					// Check if chance deck has been drawn from based on the size of deck to get
					// fair shuffling.
					if (chanceTimesDrawn == chanceDeck.size()) {
						shuffleChance();
					}
					// Check if community deck has been drawn from based on the size of deck to get
					// fair shuffling.
					if (communityTimesDrawn == communityDeck.size()) {
						shuffleCommunity();
					}

					// checks if player is in jail and looks for get out of jail cards for strategy B
					if (inJail) {

						boolean usedGetOutOfJailCard = false;
						// Loops though player's hand looking for get out of jail card
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
						// performs strategy B if no card is found
						if (!usedGetOutOfJailCard) {
							for (int j = 0; j < 3; j++) { // tries to roll three doubles.
								int diceOne = diceRoll();
								int diceTwo = diceRoll();

								// need to increment our turn count because rolling the dice counts as a turn
								turns++;

								// get to the end of the for loop so we can record it
								if (turns >= n[i]) {
									continue;
								}
								// checks if double
								if (diceOne == diceTwo) {
									inJail = false;
									break;
								}

								doubleTurnCount++; // increments count
							}

							if (doubleTurnCount == 3) { // checks if doubles rolled is 3
								inJail = false;
								doubleTurnCount = 0;
							}
						}
					}

					// Rolls dice
					int diceRoll1 = diceRoll();
					int diceRoll2 = diceRoll();
					// checks of doubles were rolled.
					if (diceRoll1 == diceRoll2) {
						threeDoubleDiceRollCounter++;
					} else {
						threeDoubleDiceRollCounter = 0; // doubles need to be rolled consecutively
					}
					// If three doubles are rolled then you are sent to jail.
					if (threeDoubleDiceRollCounter == 3) {
						inJail = true;
						current = JAIL;
						board[current]++;
						threeDoubleDiceRollCounter = 0;
						continue;
					}
					// moves player according to the dice roll amount.
					movePlayer(diceRoll1 + diceRoll2);
					// Checks if we are on a community chest spot and draws card
					if (current == COMMUNITY_CHEST_1 || current == COMMUNITY_CHEST_2 || current == COMMUNITY_CHEST_3) {
						drawCommunityCard();
					}
					// Checks if we are on a chance chest spot and draws card
					if (current == CHANCE_1 || current == CHANCE_2 || current == CHANCE_3) {
						drawChanceCard();
					}
				}
				printResults(n[i]); // prints the result for current number of turns player has
				// reset for next game
				Arrays.fill(board, 0);
				current = 0;
				inJail = false;
				hand = new ArrayList<>();
				chanceDeck = Card.getChanceDeck();
				communityDeck = Card.getCommunityChestDeck();
				chanceTimesDrawn = 0;
				communityTimesDrawn = 0;
				threeDoubleDiceRollCounter = 0;
			}

		}
	}

	/**
	 * Performs strategy A for the simulation.
	 */
	private static void strategyA() {
		// Loops 10 times to run the simulation for strategy A.
		for (int currentTurns = 1; currentTurns <= 10; currentTurns++) {
			shuffleCommunity(); // shuffle decks at beginning of game
			shuffleChance(); // ^
			System.out.println("Strategy A simulation #" + currentTurns + " of 10"); // Prints out the current # of the
																						// simulation
			// Loops length of n array containing how many turns a player has
			for (int i = 0; i < n.length; i++) {
				System.out.println("Strategy A for N = " + n[i]); // Prints out the number of turns the player will
																	// have.
				sb.append("Strategy A for N = " + n[i] + "\n"); // adds to the StringBuilder for csv file.
				for (int turns = 0; turns < n[i]; turns++) { // Runs the simulation for the number of turns the player
																// will have.
					// Check if chance deck has been drawn from based on the size of deck to get
					// fair shuffling.
					if (chanceTimesDrawn == chanceDeck.size()) {
						shuffleChance();
					}
					// Check if community deck has been drawn from based on the size of deck to get
					// fair shuffling.
					if (communityTimesDrawn == communityDeck.size()) {
						shuffleCommunity();
					}

					// checks if player is in jail and looks for get out of jail cards performs
					// strategy A.
					if (inJail) {
						boolean usedGetOutOfJailCard = false;
						// Loops through hand to see if player has Get out of Jail card.
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

					// Rolls dice
					int diceRoll1 = diceRoll();
					int diceRoll2 = diceRoll();
					// checks if rolled doubles
					if (diceRoll1 == diceRoll2) {
						threeDoubleDiceRollCounter++;
					} else {
						threeDoubleDiceRollCounter = 0;
					}
					// gets put in jail when three doubles are rolled.
					if (threeDoubleDiceRollCounter == 3) {
						inJail = true;
						current = JAIL;
						board[current]++;
						threeDoubleDiceRollCounter = 0;
						continue;
					}
					// moves player according to dice roll.
					movePlayer(diceRoll1 + diceRoll2);
					// Checks if we are on a community chest spot and draws card
					if (current == COMMUNITY_CHEST_1 || current == COMMUNITY_CHEST_2 || current == COMMUNITY_CHEST_3) {
						drawCommunityCard();
					}
					// checks if we are on a chance spot and draws a card
					if (current == CHANCE_1 || current == CHANCE_2 || current == CHANCE_3) {
						drawChanceCard();
					}

				}
				printResults(n[i]); // prints the result for current number of turns player has

				/*
				 * Need to reset game for next iteration
				 */
				Arrays.fill(board, 0);
				current = 0;
				inJail = false;
				hand = new ArrayList<>();
				chanceDeck = Card.getChanceDeck();
				communityDeck = Card.getCommunityChestDeck();
				chanceTimesDrawn = 0;
				communityTimesDrawn = 0;
				threeDoubleDiceRollCounter = 0;
			}

		}
	}

	/**
	 * Prints the results of the simulation.
	 * 
	 * @param lengh total number of turns.
	 */
	private static void printResults(int lengh) {
		int totalTurns = lengh;
		for (int i = 0; i < board.length; i++) {
			double percentage = ((double) board[i] / totalTurns * 100);
			String formattedPercentage = String.format("%.2f", percentage);
			System.out.println("Spot " + (i + 1) + ": " + board[i] + " Percentage: " + formattedPercentage + "%");
			sb.append(board[i] + ",").append(formattedPercentage + "%\n");
		}
		System.out.println();

	}

	/**
	 * Shuffles community deck.
	 */
	private static void shuffleCommunity() {
		communityTimesDrawn = 0;
		Card[] cs = new Card[communityDeck.size()];

		for (int k = 0; k < cs.length; k++) {
			cs[k] = communityDeck.dequeue();
		}
		Knuth.shuffle(cs);
		for (Card c : cs) {
			communityDeck.enqueue(c);
		}
	}

	/**
	 * Shuffle chance deck.
	 */
	private static void shuffleChance() {
		chanceTimesDrawn = 0;
		Card[] cs = new Card[chanceDeck.size()];
		for (int k = 0; k < cs.length; k++) {
			cs[k] = chanceDeck.dequeue();
		}
		Knuth.shuffle(cs);
		for (Card c : cs) {
			chanceDeck.enqueue(c);
		}
	}

	/**
	 * Performs the action of community cards and deals with all counting if the
	 * card moves you.
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
			communityDeck.enqueue(c);
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

	/**
	 * Performs all actions for the chance cards deals with all counting if the card
	 * moves you.
	 * 
	 * @param c chance card
	 */
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
			chanceDeck.enqueue(c);
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

	/**
	 * Move player to the nearest utility.
	 */
	private static void nearestUtil() {
		while (current != UTILITY_1 && current != UTILITY_2) {
			current = (current + 1) % BOARD_SIZE;
		}
		board[current]++;
	}

	/**
	 * Moves player to the nearest rail road.
	 */
	private static void nearestRail() {
		while (current != RAIL_1 && current != RAIL_2 && current != RAIL_3 && current != RAIL_4) {
			current = (current + 1) % BOARD_SIZE;
		}
		board[current]++;
	}

	/**
	 * Draws community card from chance deck and adds it to your hand.
	 */
	private static void drawCommunityCard() {
		Card communityCard = communityDeck.dequeue();

		if (communityCard.getCommuntiyCard() == CommunityChestCards.GET_OUT_OF_JAIL)
			hand.add(communityCard);
		else
			communityCardAction(communityCard);
	}

	/**
	 * Draws chance card from the chance deck and adds it to your hand.
	 */
	private static void drawChanceCard() {
		chanceTimesDrawn++;
		Card chanceCard = chanceDeck.dequeue();

		if (chanceCard.getChanceCard() == ChanceCards.GET_OUT_OF_JAIL)
			hand.add(chanceCard);
		else
			chanceActions(chanceCard);
	}

	/**
	 * Moves "player" with the specified number of steps.
	 * 
	 * @param steps amount you need player to move.
	 */
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
