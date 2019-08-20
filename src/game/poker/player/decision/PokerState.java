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
  private int visit = 0;
  private double winCount = 0;

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
    for (int i = 0; i < deck.remainingCards(); i++) {
      Deck spareDeck = new StandardDeck(1);
      spareDeck.setStreamCards(deck.allCards());
      Card drawnCard = spareDeck.drawAtPosition(i);

      List<Card> newCommunity = board;
      newCommunity.add(drawnCard);

      PokerState state = new PokerState(hand, newCommunity, opponents, spareDeck);
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
  
  public double calculateScore() {
    return winCount / (double) visit;
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

  public double getWinCount() {
    return winCount;
  }
  
  public void addWinCount() {
    this.winCount += 1;
  }
}
