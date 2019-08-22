package game.poker.player.decision;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.StandardDeck;
import game.poker.rules.FindRank;
import game.poker.rules.HandRank;

/**
 * Represents tne current status of a Poker game.
 */
public class PokerState {
  private List<Card> hand;
  private List<Card> board;
  private List<Hand> opponents;
  private Deck deck;
  private FindRank logic = new FindRank();
  private int visit = 0;
  private double winCount = 0;

  /**
   * Constructs a PokerState given game information.
   * @param hand the hand of the computer
   * @param board the community cards
   * @param opponents the hand each opponents has
   * @param deck the current deck of cards
   */
  public PokerState(List<Card> hand, List<Card> board, List<Hand> opponents, Deck deck) {
    this.hand = hand;
    this.board = board;
    this.opponents = opponents;
    this.deck = deck;
  }

  /**
   * Generates all possible game states.
   * @return a list of all possible game states
   */
  public List<PokerState> generateStates() {
    List<PokerState> states = new ArrayList<>();
    for (int i = 0; i < deck.remainingCards(); i++) {
      Deck spareDeck = new StandardDeck();
      spareDeck.setStreamCards(deck.allCards());
      Card drawnCard = spareDeck.drawAtPosition(i);

      List<Card> newCommunity = new ArrayList<>(board);
      newCommunity.add(drawnCard);

      PokerState state = new PokerState(hand, newCommunity, opponents, spareDeck);
      states.add(state);
    }

    return states;
  }

  /**
   * Determines the amount of points won at this state.
   * @return the points won at this state
   */
  public double winFactor() {
    HandRank selfRank = logic.getRank(new Hand(hand), board);
    HandRank maxRank = HandRank.High_Card;
    for (Hand opponent : opponents) {
      HandRank opponentRank = logic.getRank(opponent, board);
      
      if (opponentRank.getValue() > maxRank.getValue()) {
        maxRank = opponentRank;
      }
    }

    int selfValue = selfRank.getValue();
    int oppValue = maxRank.getValue();

    if (selfValue > oppValue) {
      return 1;
    }
    else if (selfValue + 1 > oppValue) {
      return 0.5;
    }
    else if (selfValue + 2 > oppValue) {
      return 0.2;
    }
    else {
      return 0;
    }
  }

  /**
   * Makes a random play by adding another card to the community.
   */
  public void randomPlay() {
    Card card = deck.drawCard();
    board.add(card);
  }

  /**
   * Determines if the game is still being played.
   * @return if the game is still being played
   */
  public boolean stillPlaying() {
    return this.board.size() < 5;
  }

  /**
   * Increments the amount of times this state has been reached.
   */
  public void incrementVisit() {
    this.visit += 1;
  }

  /**
   * Increments the amount of points won.
   * @param count the amount of points to add
   */
  public void incrementWins(double count) {
    this.winCount += count;
  }

  /**
   * Retrieves the number of visits.
   * @return the number of visits
   */
  public int getVisit() {
    return visit;
  }

  /**
   * Retrieves the win count.
   * @return the win count
   */
  public double getWinCount() {
    return winCount;
  }
}
