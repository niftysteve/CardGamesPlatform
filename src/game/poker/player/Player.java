package game.poker.player;

import java.util.ArrayList;
import java.util.List;

import game.deck.Card;
import game.deck.Hand;
import game.poker.rules.HandRank;

public interface Player {

  /**
   * Adds a card to this player's hand.
   * @param cards the new card to be added
   */
  void addCard(List<Card> cards);

  /**
   * Gives this player a new hand.
   * @param hand the new hand for this player
   */
  void initHand(Hand hand);

  /**
   * The player adds money to the pot.
   * @param amount the quantity to be added
   * @param status if the player raised
   */
  void bet(int amount, boolean status);

  /**
   * Gets the type of the highest ranked hand this player has.
   * @return the type of the highest ranked hand of this player
   */
  HandRank getRank();

  /**
   * Retrieves the highest ranked hand this player has.
   * @return the highest ranked hand of this player
   */
  Hand getRankedHand();

  /**
   * Retrieves this player's hand.
   * @return this player's hand
   */
  List<Card> getHand();

  /**
   * Converts this player's hand to string form.
   * @return the cards in this player's hand in string form
   */
  String handState();

  void setRaise();

  /**
   * The player receives money from the pot.
   * @param amount the amount to be received
   */
  void claim(int amount);

  /**
   * Sets this player's play state to not playing.
   */
  void fold();

  /**
   * Sets this player's play state to if they still have money to bet.
   */
  void continuePlay();

  /**
   * Determines if this player is still playing.
   * @return if this player is still playing
   */
  boolean isPlaying();

  /**
   * Retrieves the ID of this player.
   * @return the ID of this player
   */
  int getId();

  /**
   * Retrieves the amount of money this player currently has.
   * @return the amount of money this player currently has
   */
  int getMoney();

  /**
   * Retrieves the amount of money this player is currently betting.
   * @return the amount of money this player is currently betting
   */
  int getBet();

  boolean getRaised();
}
