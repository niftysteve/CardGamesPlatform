package game.poker;

import java.io.InputStreamReader;

import game.poker.PokerGame;
import game.poker.cmdflow.GameController;
import game.poker.cmdflow.PokerCMD;

/**
 * Plays a game of Poker on the command line.
 */
public class PlayPoker {

  public static void main(String[] args) {
    PokerGame game = new PokerGame(1, 500);
    GameController con = new PokerCMD(new InputStreamReader(System.in), System.out, game);
    con.playGame();
  }
}
