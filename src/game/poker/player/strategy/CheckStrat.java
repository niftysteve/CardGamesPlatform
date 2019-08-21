package game.poker.player.strategy;

public class CheckStrat implements BetStrategy {
  public int calcBet(int base) {
    return base;
  }

  public int calcBet(int base, int seed) {
    return base;
  }
}
