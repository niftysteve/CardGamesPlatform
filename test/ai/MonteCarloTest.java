package ai;

import game.deck.*;
import game.poker.player.ComputerBrain;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonteCarloTest {

  @Test
  public void simpleTest() {
    List<Card> hand = new ArrayList<>();
    List<Card> board = new ArrayList<>();
    Card aClubs = new Card(Suit.Clubs, Rank.Ace);
    Card aDiamonds = new Card(Suit.Diamonds, Rank.Ace);
    Card aHearts = new Card(Suit.Hearts, Rank.Ace);
    Card aSpades = new Card(Suit.Spades, Rank.Ace);
    Card spare = new Card(Suit.Spades, Rank.Five);
    Card spare2 = new Card(Suit.Hearts, Rank.Two);

    hand.add(aClubs);
    hand.add(aDiamonds);
    board.add(aHearts);
    board.add(aSpades);
    board.add(spare);
    board.add(spare2);

    ComputerBrain brain = new ComputerBrain(hand, board, 1, 100);
    int result = brain.calculateBet();
    System.out.println(result);
  }

  @Test
  public void lowAggroTest() {
    List<Card> hand = new ArrayList<>();
    List<Card> board = new ArrayList<>();
    Card c1 = new Card(Suit.Clubs, Rank.Two);
    Card c2 = new Card(Suit.Diamonds, Rank.Five);
    Card c3 = new Card(Suit.Hearts, Rank.Five);
    Card c4 = new Card(Suit.Spades, Rank.Six);
    Card c5 = new Card(Suit.Spades, Rank.Seven);

    hand.add(c1);
    hand.add(c2);
    board.add(c3);
    board.add(c4);
    board.add(c5);

    ComputerBrain brain = new ComputerBrain(hand, board, 1, 100);
    int result = brain.calculateBet();
    System.out.println(result);
  }
}