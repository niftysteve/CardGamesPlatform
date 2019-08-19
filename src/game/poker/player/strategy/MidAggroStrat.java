package game.poker.player.strategy;

import java.util.Random;

public class MidAggroStrat implements BetStrategy {
  private int UPPER_FACTOR = 5;
  private int LOWER_FACTOR = 2;

  public int calcBet(int base) {
    Random rand = new Random();
    int upper = base * UPPER_FACTOR;
    int lower = base * LOWER_FACTOR;
    return rand.nextInt(upper + 1 - lower) + lower;
  }
}