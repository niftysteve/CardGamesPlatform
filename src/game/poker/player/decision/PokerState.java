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

public class PokerState {
  private List<Card> hand;
  private List<Card> board;
  private List<Hand> opponents;
  private Deck deck;
  private int visit;
  private double score;

  public PokerState(List<Card> hand, List<Card> board, List<Hand> opponents, Deck deck) {
    this.hand = hand;
    this.board = board;
    this.opponents = opponents;
    this.deck = deck;
  }

  private HandRank stateRank() {
    List<Card> allCards = Stream.concat(hand.stream(), board.stream()).collect(Collectors.toList());
    FindRank logic = new FindRank(new Hand(allCards));
    return logic.getRank();
  }

  public List<PokerState> generateStates() {
    List<PokerState> states = new ArrayList<>();
    List<Card> baseCards = deck.allCards();
    for (int i = 0; i < baseCards.size(); i++) {
      Card current = baseCards.get(i);
      List<Card> excludeCurrent = new ArrayList<>(baseCards);
      excludeCurrent.remove(i);
      Deck remaining = new StandardDeck(false, excludeCurrent);
      List<Card> end = board;
      end.add(current);
      PokerState state = new PokerState(hand, end, opponents, remaining);
      states.add(state);
    }

    return states;
  }
  
  public boolean isWinner() {
    HandRank maxRank = HandRank.High_Card;
    for (Hand opponent : opponents) {
      List<Card> opponentHand = opponent.getCards();
      List<Card> allCards = Stream.concat(opponentHand.stream(), board.stream()).collect(Collectors.toList());
      FindRank logic = new FindRank(new Hand(allCards));
      HandRank opponentRank = logic.getRank();
      
      if (opponentRank.getValue() > maxRank.getValue()) {
        maxRank = opponentRank;
      }
    }
    
    return stateRank().getValue() > maxRank.getValue();
  }

  public void randomPlay() {
    Card card = deck.drawCard();
    board.add(card);
  }

  public boolean stillPlaying() {
    return this.board.size() < 5;
  }

  public void incrementVisit() {
    this.visit += 1;
  }

  public int getVisit() {
    return visit;
  }

  public double getScore() {
    return score;
  }
  
  public void addScore(int toAdd) {
    this.score += toAdd;
  }
}
