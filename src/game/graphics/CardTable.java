package game.graphics;

import java.util.ArrayList;

import game.deck.Card;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents the table where all cards are displayed.
 */
public class CardTable extends BorderPane {

  /**
   * Constructs a CardTable.
   */
  public CardTable() {
    super();
  }

  /**
   * Displays cards in the center of the panel.
   * @param cards the cards to be displayed
   */
  public void centerDisplay(ArrayList<Card> cards) {
    HBox box = new HBox(10);
    box.setAlignment(Pos.CENTER);
    setCenter(cardDisplay(cards, box));
  }

  /**
   * Displays cards at the bottom of the panel.
   * @param cards the cards to be displayed
   */
  public void bottomDisplay(ArrayList<Card> cards) {
    HBox box = new HBox(10);
    box.setAlignment(Pos.CENTER);
    setBottom(cardDisplay(cards, box));
  }

  /**
   * Displays cards at the top of the panel.
   * @param cards the cards to be displayed
   */
  public void topDisplay(ArrayList<Card> cards) {
    HBox box = new HBox(10);
    box.setAlignment(Pos.CENTER);
    setCenter(cardDisplay(cards, box));
  }

  /**
   * Displays cards on the left of the panel.
   * @param cards the cards to be displayed
   */
  public void leftDisplay(ArrayList<Card> cards) {
    VBox box = new VBox(10);
    box.setAlignment(Pos.CENTER);
    setBottom(cardDisplay(cards, box));
  }

  /**
   * Displays cards on the right of the panel.
   * @param cards the cards to be displayed
   */
  public void rightDisplay(ArrayList<Card> cards) {
    VBox box = new VBox(10);
    box.setAlignment(Pos.CENTER);
    setCenter(cardDisplay(cards, box));
  }

  /**
   * Draws the playing cards.
   * @param cards the cards to be drawn
   * @param pane the panel where the cards are drawn
   */
  private Pane cardDisplay(ArrayList<Card> cards, Pane pane) {
    for (Card card : cards) {
      Rectangle background = new Rectangle(100, 140);
      background.setArcWidth(20);
      background.setArcHeight(20);
      background.setFill(Color.WHITE);

      Text cardInfo = new Text(card.getNamedValue() + " of \n" + card.getSuit());
      cardInfo.setFont(Font.font(14));

      StackPane stack = new StackPane();
      stack.getChildren().addAll(background, cardInfo);

      pane.getChildren().add(stack);
    }

    return pane;
  }
}
