package game.poker.player.strategy;

import java.util.Random;

/**
 * Represents a highly aggressive betting strategy.
 */
public class HighAggroStrat implements BetStrategy {
  private int UPPER_FACTOR = 10;
  private int LOWER_FACTOR = 5;

  @Override
  public int calcBet(int base) {
    Random rand = new Random();
    int upper = base * UPPER_FACTOR;
    int lower = base * LOWER_FACTOR;
    return rand.nextInt(upper + 1 - lower) + lower;
  }
}
