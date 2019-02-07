package game.poker;

import game.graphics.CardTable;
import game.graphics.PokerController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Launches a game of Poker
 */
public class LaunchPoker extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setScene(new Scene(createScene()));
    primaryStage.setWidth(1000);
    primaryStage.setHeight(800);
    primaryStage.setResizable(false);
    primaryStage.setTitle("Poker");
    primaryStage.show();
  }

  /**
   * Creates the graphics.
   * @return the fully constructed scene
   */
  private Parent createScene() {
    Pane base = new Pane();
    base.setPrefSize(1000, 800);

    Region background = new Region();
    background.setPrefSize(1000, 800);
    background.setStyle("-fx-background-color: black");

    HBox panelBox = new HBox(5);
    panelBox.setPadding(new Insets(5, 5, 5, 5));
    
    Rectangle cardField = new Rectangle(750, 760);
    cardField.setArcWidth(50);
    cardField.setArcHeight(50);
    cardField.setFill(Color.GREEN);
    Rectangle actionField = new Rectangle(230, 760);
    actionField.setArcWidth(50);
    actionField.setArcHeight(50);
    actionField.setFill(Color.ORANGE);
    
    Text info = new Text("Please select your game");
    Button poker = new Button("Poker");

    VBox gameSelect = new VBox(15, info, poker);
    gameSelect.setAlignment(Pos.CENTER);

    panelBox.getChildren().addAll(new StackPane(cardField, gameSelect));
    base.getChildren().addAll(background, panelBox);

    ComboBox<Integer> playerQuery = new ComboBox<>();
    ComboBox<Integer> moneyQuery = new ComboBox<>();

    Button launchPoker = new Button("Begin");
    launchPoker.setOnAction(event -> {
      if (!(playerQuery.getSelectionModel().isEmpty()
              || moneyQuery.getSelectionModel().isEmpty())) {
        PokerGame game = new PokerGame(playerQuery.getValue(), moneyQuery.getValue());
        CardTable table = new CardTable();
        panelBox.getChildren().clear();
        panelBox.getChildren().addAll(
                new StackPane(cardField, table), 
                new StackPane(actionField, new PokerController(game, table)));
      }
    });

    poker.setOnAction(event -> {
      gameSelect.getChildren().clear();

      VBox playerSide = new VBox(10);
      Text playerText = new Text("CPU Count:");
      playerQuery.getItems().addAll(1, 2, 3);
      playerSide.getChildren().addAll(playerText, playerQuery);

      VBox moneySide = new VBox(10);
      Text moneyText = new Text("Start money:");
      moneyQuery.getItems().addAll(250, 500, 750, 1000);
      moneySide.getChildren().addAll(moneyText, moneyQuery);

      HBox infoPanel = new HBox(20, playerSide, moneySide);
      infoPanel.setAlignment(Pos.CENTER);
      VBox fullQuery = new VBox(20);
      fullQuery.setAlignment(Pos.CENTER);
      fullQuery.getChildren().addAll(infoPanel, launchPoker);

      gameSelect.getChildren().add(fullQuery);
    });

    return base;
  }
}
