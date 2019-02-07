package game.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import game.poker.PokerGame;
import game.poker.player.ComputerPlayer;
import game.poker.player.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Handles the flow of a Poker game graphically.
 */
public class PokerController extends VBox {

  private PokerGame game;
  private CardTable table;
  private Player human;
  private Button proceed;
  private ArrayList<Control> controlDrawer = new ArrayList<>();
  private int roundCounter = 1;

  /**
   * Constructs and displays a PokerController to handle user input.
   * @param game the game of Poker to be played
   * @param table where the cards will be displayed
   */
  public PokerController(PokerGame game, CardTable table) {
    super();
    this.game = game;
    this.human = game.getHuman();
    this.table = table;
    setAlignment(Pos.CENTER);
    setSpacing(20);
    table.bottomDisplay(human.getHand());

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

    Text moneyText = new Text("Your current money:");
    Text playerMoney = new Text(Integer.toString(human.getMoney()));

    Text betText = new Text("All bets:");
    Text allBets = new Text(game.betState());

    TextField raiseAmount = new TextField();
    raiseAmount.setMaxWidth(100);
    raiseAmount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        raiseAmount.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });
    
    Button call = new Button("Call");
    Button raise = new Button("Raise");
    Button fold = new Button("Fold");
    controlDrawer.addAll(Arrays.asList(call, raise, fold, raiseAmount));
    proceed = new Button("Continue");
    proceed.setVisible(false);
    getChildren().addAll(moneyText, playerMoney, call, raiseAmount, raise, fold,
            betText, allBets, proceed);

    call.setOnAction(event -> {
      human.bet(game.getCurHigh() - human.getBet(), false);
      playRound();
      allBets.setText(game.betState());
      playerMoney.setText(Integer.toString(human.getMoney()));
    });

    raise.setOnAction(event -> {
      if (!raiseAmount.getText().equals("")) {
        int addition = Integer.parseInt(raiseAmount.getText());
        human.bet((game.getCurHigh() - human.getBet()) + addition, true);
        playRound();
        allBets.setText(game.betState());
        playerMoney.setText(Integer.toString(human.getMoney()));
        raiseAmount.clear();
      }
    });

    fold.setOnAction(event -> {
      human.fold();
      while (roundCounter < 4) {
        playRound();
        allBets.setText(game.betState());
        playerMoney.setText(Integer.toString(human.getMoney()));
      }
    });

    proceed.setOnAction(event -> {
      game.resolveWin();
      roundCounter = 1;
      table.centerDisplay(game.getCommunity());
      table.bottomDisplay(human.getHand());
      controlDrawer.forEach(b -> b.setDisable(false));
      proceed.setVisible(false);
      allBets.setText(game.betState());
      playerMoney.setText(Integer.toString(human.getMoney()));
    });
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
      }
    }

    if (game.roundDone() && roundCounter != 4) {
      if (roundCounter == 1) {
        game.flipCard(3);
      }
      else {
        game.flipCard(1);
      }

      roundCounter += 1;
      game.resetRaise();
    }

    if (roundCounter == 4) {
      controlDrawer.forEach(b -> b.setDisable(true));
      proceed.setVisible(true);
    }

    table.centerDisplay(game.getCommunity());
    table.bottomDisplay(human.getHand());
  }
}
