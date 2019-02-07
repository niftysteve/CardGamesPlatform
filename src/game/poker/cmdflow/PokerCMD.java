package game.poker.cmdflow;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import game.poker.PokerGame;
import game.poker.player.ComputerPlayer;
import game.poker.player.Player;

/**
 * Controls the flow and user interaction of Poker.
 */
public class PokerCMD implements GameController {

  private final Readable read;
  private final Appendable ap;
  private Scanner dataIn;
  private PokerGame game;

  public PokerCMD(Readable read, Appendable ap, PokerGame game) {
    this.read = read;
    this.ap = ap;
    this.game = game;
  }

  @Override
  public void playGame() {
    dataIn = new Scanner(this.read);

    Iterator<Player> playerIterator = game.getPlayers().iterator();
    while (playerIterator.hasNext()) {
      Player player = playerIterator.next();
      if (player instanceof ComputerPlayer) {
        ((ComputerPlayer) player).calculateBet();
        playerIterator.remove();
      }
      else {
        break;
      }
    }

    while (!game.isGameOver()) {
      playRound();
      game.flipCard(3);
      playRound();
      game.flipCard(1);
      playRound();
      game.flipCard(1);
      writeMessage("\nPlayer " + game.resolveWin() + " has won the round\n\n");
      game.deal();
    }
  }

  /**
   * Plays a single round of Poker.
   */
  private void playRound() {

    for (Player player : game.getPlayers()) {
      if (player.isPlaying() && !player.getRaised() && !game.isGameOver()) {

        if (player instanceof ComputerPlayer) {
          game.resetRaise();
          ((ComputerPlayer) player).calculateBet();
        }
        else {
          actionRound(player);
        }
      }
    }
  }

  /**
   * Presents the user with the game status and asks for their desired action.
   * @param player the player who is taking a turn
   */
  private void actionRound(Player player) {
    writeMessage("Your player ID: " + player.getId() + "\n");
    writeMessage("Your cards: " + player.handState() + "\n");
    writeMessage("Community Cards: " + game.communityState() + "\n");
    writeMessage("Your money: " + player.getMoney() + "\n");
    writeMessage("Your bet: " + player.getBet() + "\n");
    writeMessage("Current bets: " + game.betState() + "\n");
    writeMessage("What will you do?\n");

    boolean validInput = false;

    while (!validInput) {
      String action = dataIn.next();

      switch (action) {
        case "check":
          int betDif = game.getCurHigh() - player.getBet();
          if (betDif > player.getMoney()) {
            writeMessage("You don't have enough money. Try again.\n");
          }
          else {
            player.bet(betDif, false);
            validInput = true;
          }
          break;
        case "fold":
          player.fold();
          writeMessage("You have folded\n");
          validInput = true;
          break;
        case "raise":
          writeMessage("Enter amount to raise: ");
          int raise = dataIn.nextInt();
          player.bet((game.getCurHigh() - player.getBet()) + raise, true);
          validInput = true;
          break;
        case "all":
          player.bet(player.getMoney(), true);
          validInput = true;
          break;
        default:
          writeMessage("Invalid action. Try again.\n");
      }
    }
  }

  /**
   * Displays a message to the user by writing it to the appendable.
   * @param message the message to be displayed
   * @throws IllegalStateException if the message cannot be written
   */
  private void writeMessage(String message) {

    try {
      this.ap.append(message);
    }
    catch (IOException e) {
      throw new IllegalStateException("Data cannot be written");
    }
  }
}
