package game.poker.player;

import java.util.ArrayList;
import java.util.List;

import game.deck.Card;
import game.deck.Hand;
import game.poker.rules.FindRank;
import game.poker.rules.HandRank;

/**
 * Represents a poker player.
 */
public class PokerPlayer {
  private boolean playing = true;
  private boolean raised = false;
  private int curBet = 0;
  private int port;
  private int money;
  private Hand hand;

  /**
   * Constructs a poker player.
   * @param port the ID of this player
   * @param money the starting money
   */
  public PokerPlayer(int port, int money) {
    this.port = port;
    this.money = money;
    this.hand = new Hand();
  }

  /**
   * Constructs a poker player with an initial hand.
   * @param port the ID of this player
   * @param money the starting money
   * @param hand the initial hand of this player
   */
  public PokerPlayer(int port, int money, Hand hand) {
    this.port = port;
    this.money = money;
    this.hand = hand;
  }

  /**
   * Adds a card to this player's hand.
   * @param cards the new card to be added
   */
  public void addCard(Card... cards) {
    for (Card c : cards) {
      hand.addCard(c);
    }
  }

  /**
   * Gives this player a new hand.
   * @param hand the new hand for this player
   */
  public void initHand(Hand hand) {
    this.hand = hand;
    this.curBet = 0;
  }

  /**
   * The player adds money to the pot.
   * @param amount the quantity to be added
   */
  public void bet(int amount) {
    if (amount > money) {
      amount = money;
    }

    money -= amount;
    curBet += amount;
    raised = true;
  }

  /**
   * Gets the type of the highest ranked hand this player has.
   * @return the type of the highest ranked hand of this player
   */
  public HandRank getRank() {
    FindRank logic = new FindRank(hand);

    return logic.getRank();
  }

  /**
   * Retrieves the highest ranked hand this player has.
   * @return the highest ranked hand of this player
   */
  public Hand getRankedHand() {
    FindRank logic = new FindRank(hand);

    return logic.getRankedHand();
  }

  /**
   * Calculates the sum of each card value from the player's highest ranked hand.
   * @return the highest ranked hand of this player
   */
  public int totalRankedSum() {
    int total = 0;
    for (Card card : getRankedHand().getCards()) {
      total += card.getRank();
    }

    return total;
  }

  /**
   * The player receives money.
   * @param amount the amount to be received
   */
  public void claim(int amount) {
    money += amount;
  }

  /**
   * The player purposefully stopped playing the round.
   */
  public void fold() {
    this.playing = false;
    this.raised = true;
  }

  /**
   * The player still has enough money to play.
   */
  public void continuePlay() {
    playing = money > 0;
  }

  /**
   * Converts this player's hand to string form.
   * @return the cards in this player's hand in string form
   */
  public String handState() {
    StringBuilder builder = new StringBuilder();

    List<Card> cards = hand.getCards();
    for (int i = 0; i < cards.size(); i++) {
      Card card = cards.get(i);
      String curCard = card.getNamedValue() + " of " + card.getSuit()
              + (i < cards.size() - 1 ? ", " : "");
      builder.append(curCard);
    }

    return builder.toString();
  }

  /**
   * Determines if this player is still playing.
   * @return if this player is still playing
   */
  public boolean isPlaying() {
    return this.playing;
  }

  /**
   * Retrieves the ID of this player.
   * @return the ID of this player
   */
  public int getId() {
    return this.port;
  }

  /**
   * Retrieves the amount of money this player currently has.
   * @return the amount of money this player currently has
   */
  public int getMoney() {
    return this.money;
  }

  /**
   * Retrieves the amount of money this player is currently betting.
   * @return the amount of money this player is currently betting
   */
  public int getBet() {
    return this.curBet;
  }

  /**
   * Retrieves this player's hand.
   * @return this player's hand
   */
  public List<Card> getHand() {
    return hand.getCards();
  }

  /**
   * Retrieves this raise status.
   * @return if this player has raised
   */
  public boolean getRaised() {
    return this.raised;
  }

  /**
   * Sets the raise value.
   * @param raise the value to set
   */
  public void setRaise(boolean raise) {
    this.raised = raise;
  }
}
