package game.poker.player.strategy;

/**
 * Represents strategies used when betting.
 */
public interface BetStrategy {

  /**
   * Calculates an appropriate amount to bet.
   * @param bet the current amount being bet
   * @return the amount to bet
   */
  int calcBet(int bet);
}
