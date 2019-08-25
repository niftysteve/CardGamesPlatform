package game.poker.cmdflow;

import java.io.IOException;
import java.util.Scanner;

import game.poker.PokerGame;
import game.poker.player.ComputerPlayer;
import game.poker.player.PokerPlayer;

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

    while (game.inProgress()) {
      if (!game.closeRound()) {
        playRound();
        game.flipCard();
      }
      else {
        displayEndStatus();
        writeMessage("\nPlayer " + game.resolveWin() + " has won the round\n\n");
      }
    }
  }

  /**
   * Plays a single round of Poker.
   */
  private void playRound() {
    while (!game.bettingDone() || !game.allRaised()) {
      for (PokerPlayer player : game.availablePlayers()) {
        if (player.isPlaying() && game.inProgress()) {
          if (player instanceof ComputerPlayer) {
            ((ComputerPlayer) player).calculateBet(game.getCommunity(),
                    game.availablePlayers().size() - 1, game.currentBet());
          }
          else {
            actionRound(player);
          }
        }
      }
    }
  }

  private void displayEndStatus() {
    writeMessage("\nCommunity cards: " + game.communityState() + "\n");
    writeMessage("All bets: " + game.betState() + "\n");
    for (PokerPlayer player : game.availablePlayers()) {
      writeMessage("Player " + player.getId() + " hand: " + player.handState() + "\n");
    }
  }

  /**
   * Presents the user with the game status and asks for their desired action.
   * @param player the player who is taking a turn
   */
  private void actionRound(PokerPlayer player) {
    writeMessage("Your player ID: " + player.getId() + "\n");
    writeMessage("Your cards: " + player.handState() + "\n");
    writeMessage("Community cards: " + game.communityState() + "\n");
    writeMessage("Your money: " + player.getMoney() + "\n");
    writeMessage("Your bet: " + player.getBet() + "\n");
    writeMessage("Current bets: " + game.betState() + "\n");
    writeMessage("What will you do?\n");

    boolean validInput = false;

    while (!validInput) {
      String action = dataIn.next();

      switch (action) {
        case "check":
          int betDif = game.currentBet() - player.getBet();
          player.bet(betDif);
          validInput = true;
          break;
        case "fold":
          player.fold();
          writeMessage("You have folded\n");
          validInput = true;
          break;
        case "raise":
          writeMessage("Enter amount to raise: ");
          int raise = dataIn.nextInt();
          player.bet((game.currentBet() - player.getBet()) + raise);
          validInput = true;
          break;
        case "all":
          player.bet(player.getMoney());
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
