package game.poker.player;

import java.util.List;
import java.util.Random;

import game.deck.Card;
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
   * Determines a proper amount to bet.
   */
  public void calculateBet(List<Card> board, int players, int currentBet) {
    ComputerBrain brain = new ComputerBrain(getHand(), board, players, currentBet);
    int result = brain.calculateBet();
    if (result == 0) {
      fold();
    }
    else {
      bet(result - getBet());
    }
  }
}
