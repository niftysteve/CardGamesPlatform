package game.poker.player;

import java.util.Random;

import game.poker.rules.HandRank;

/**
 * Represents a computer player.
 */
public class ComputerPlayer extends PokerPlayer {

  /**
   * Constructs a ComputerPlayer.
   * @param port the ID of this computer
   * @param money the amount of money this computer starts with
   */
  public ComputerPlayer(int port, int money) {
    super(port, money);
  }

  /**
   * Bets by raising a generated amount of money.
   */
  public void calculateBet() {
    bet(generateBet(), true);
  }

  /**
   * Determines the amount of money to bet.
   * @return the amount to bet
   */
  private int generateBet() {
    if (getMoney() == 0) {
      return 0;
    }

    Random rand = new Random();

    if (getRank().getValue() > HandRank.High_Card.getValue()) {
      return rand.nextInt(getMoney() - 10);
    }
    else {
      if (rand.nextInt(4) == 0) {
        fold();
        return 0;
      }
      else {
        return rand.nextInt(getMoney());
      }
    }
  }
}
