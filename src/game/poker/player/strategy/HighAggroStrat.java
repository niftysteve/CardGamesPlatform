package game.poker.player.strategy;

import java.util.Random;

public class HighAggroStrat implements BetStrategy {
  private int UPPER_FACTOR = 9999;
  private int LOWER_FACTOR = 5;

  public int calcBet(int base) {
    Random rand = new Random();
    int upper = base * UPPER_FACTOR;
    int lower = base * LOWER_FACTOR;
    return rand.nextInt(upper + 1 - lower) + lower;
  }
}
