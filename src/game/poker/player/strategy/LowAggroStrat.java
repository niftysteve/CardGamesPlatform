package game.poker.player.strategy;

import java.util.Random;

public class LowAggroStrat implements BetStrategy {
  private int FACTOR = 2;

  public int calcBet(int base) {
    Random rand = new Random();
    int upper = base * FACTOR;
    return rand.nextInt(upper + 1 - base) + base;
  }
}
