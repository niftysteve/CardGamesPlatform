package game.poker.rules;

import game.deck.Hand;

/**
 * Represents rank finding processes.
 */
public interface RankLogic {

  /**
   * Determines if the rank can be applied.
   * @return if the rank fits the hand
   */
  boolean validRank();

  /**
   * Extracts the proper cards to form the optimal ranked hand.
   * @return the optimal ranked hand
   */
  Hand findRankedHand();

  /**
   * Retrieves the representing hand rank.
   * @return the hand rank
   */
  HandRank getRank();
}
