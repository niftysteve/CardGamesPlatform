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
public class PokerPlayer implements Player {
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

  @Override
  public void addCard(ArrayList<Card> cards) {
    for (Card c : cards) {
      hand.addCard(c);
    }
  }

  @Override
  public void initHand(Hand hand) {
    this.hand = hand;
    this.curBet = 0;
  }

  @Override
  public void bet(int amount, boolean status) {
    if (!raised) {
      if (amount > money) {
        amount = money;
      }

      money -= amount;
      curBet += amount;
      raised = status;
    }
  }

  @Override
  public void setRaise() {
    this.raised = false;
  }

  @Override
  public HandRank getRank() {
    FindRank logic = new FindRank(hand);

    return logic.getRank();
  }

  @Override
  public Hand getRankedHand() {
    FindRank logic = new FindRank(hand);

    return logic.getRankedHand();
  }

  @Override
  public List<Card> getHand() {
    return hand.getCards();
  }


  @Override
  public String handState() {
    StringBuilder builder = new StringBuilder();

    for (Card card : hand.getCards()) {
      String curCard = card.getNamedValue() + " of " + card.getSuit() + ", ";
      builder.append(curCard);
    }

    String hand = builder.toString();
    return hand.substring(0, hand.length() - 2);
  }

  @Override
  public void claim(int amount) {
    money += amount;
  }

  @Override
  public void fold() {
    this.playing = false;
  }

  @Override
  public void continuePlay() {
    playing = money > 0;
  }

  @Override
  public boolean isPlaying() {
    return this.playing;
  }

  @Override
  public int getId() {
    return this.port;
  }

  @Override
  public int getMoney() {
    return this.money;
  }

  @Override
  public int getBet() {
    return this.curBet;
  }

  @Override
  public boolean getRaised() {
    return this.raised;
  }
}
