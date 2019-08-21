package game.poker.player.strategy;

/**
 * Represents a passive betting strategy,
 */
public class CheckStrat implements BetStrategy {

  @Override
  public int calcBet(int bet) {
    return bet;
  }
}
