package game.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Represents a hand of cards.
 */
public class Hand {
  private List<Card> hand;

  public Hand() {
    this.hand = new ArrayList<>();
  }

  /**
   * Constructs a Hand with a given list of Cards.
   * @param hand a list of Cards to fill the Hand with
   */
  public Hand(List<Card> hand) {
    this.hand = hand;
  }

  public Hand(Hand hand) {
    this.hand = hand.getCards();
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
  public List<Suit> getSuits() {
    List<Suit> suits = new ArrayList<>();

    hand.forEach(card -> suits.add(card.getSuit()));

    return suits;
  }

  /**
   * Gets all the card values in this hand.
   * @return a list of all card values in this hand
   */
  public List<Integer> getValues() {
    List<Integer> values = new ArrayList<>();

    hand.forEach(card -> values.add(card.getRank()));

    return values;
  }

  /**
   * Gets that amount of times the card value in this hand appear.
   * @return a map of card values and the amount of times they appear in this hand
   */
  public TreeMap<Integer, Integer> allValues() {

    TreeMap<Integer, Integer> quantity = new TreeMap<>();
    List<Integer> values = getValues();

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
  public HashMap<Suit, Integer> allSuits() {

    HashMap<Suit, Integer> quantity = new HashMap<>();
    List<Suit> rawSuits = getSuits();

    for (Suit suit : rawSuits) {
      if (!quantity.containsKey(suit)) {
        quantity.put(suit, Collections.frequency(rawSuits, suit));
      }
    }

    return quantity;
  }

  public List<Card> getCards() {
    return this.hand;
  }
}