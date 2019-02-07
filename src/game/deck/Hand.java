package game.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;


/**
 * Represents a hand of cards.
 */
public class Hand {
  private ArrayList<Card> hand;

  public Hand() {
    this.hand = new ArrayList<>();
  }

  /**
   * Constructs a Hand with a given list of Cards.
   * @param hand a list of Cards to fill the Hand with
   */
  public Hand(ArrayList<Card> hand) {
    this.hand = hand;
  }

  /**
   * Adds a card to this hand.
   * @param card the new card to be added
   */
  public void addCard(Card card) {
    hand.add(card);
  }

  /**
   * Gets all the suits that appears in this hand.
   * @return a list of all suits that appear in this hand
   */
  public ArrayList<String> getSuits() {
    ArrayList<String> suits = new ArrayList<>();

    for (Card card : hand) {
      suits.add(card.getSuit());
    }

    return suits;
  }

  /**
   * Gets all the card values in this hand.
   * @return a list of all card values in this hand
   */
  public ArrayList<Integer> getValues() {
    ArrayList<Integer> values = new ArrayList<>();

    for (Card card : hand) {
      values.add(card.getValue());
    }

    return values;
  }

  /**
   * Gets that amount of times the card value in this hand appear.
   * @return a map of card values and the amount of times they appear in this hand
   */
  public TreeMap<Integer, Integer> allValues() {

    TreeMap<Integer, Integer> quantity = new TreeMap<>();
    ArrayList<Integer> values = getValues();

    for (Integer rank : values) {
      if (!quantity.containsKey(rank)) {
        quantity.put(rank, Collections.frequency(values, rank));
      }
    }

    return quantity;
  }

  /**
   * Gets the amount of times the suits in this hand appear.
   * @return a map of suits and the amount of times they appear in this hand
   */
  public TreeMap<String, Integer> allSuits() {

    TreeMap<String, Integer> quantity = new TreeMap<>();
    ArrayList<String> rawSuits = getSuits();

    for (String suit : rawSuits) {
      if (!quantity.containsKey(suit)) {
        quantity.put(suit, Collections.frequency(rawSuits, suit));
      }
    }

    return quantity;
  }

  public ArrayList<Card> getCards() {
    return this.hand;
  }
}