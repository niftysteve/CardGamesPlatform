package game.deck;

import java.util.List;

/**
 * Represents a deck of cards.
 */
public interface Deck {

  /**
   * Shuffles this deck.
   */
  void shuffle();

  /**
   * Deals cards to all players.
   * @param players the number of players
   * @param amount the number of cards to be dealt to each player
   * @return a list of hands dealt
   */
  List<Hand> dealCards(int players, int amount);

  /**
   * Draws a card by removing the top-most card from the deck.
   * @return the top-most card
   */
  Card drawCard();

  /**
   * Removes a given amount of cards from the top of the deck.
   * @param amount the number of cards to remove
   */
  void burnCards(int amount);

  /**
   * Determines the amount of cards left in the deck.
   * @return the amount of cards left
   */
  int remainingCards();
}
