package ai;

import game.deck.*;
import game.poker.player.ComputerBrain;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for Monte Carlo Tree Search with betting.
 */
public class MonteCarloTest {
  private static final int BET = 100;
  private List<Card> hand;
  private List<Card> board;

  @Before
  public void init() {
    this.hand = new ArrayList<>();
    this.board = new ArrayList<>();
  }

  @Test
  public void testEmptyBoard() {
    Card c1 = new Card(Suit.Clubs, Rank.Six);
    Card c2 = new Card(Suit.Diamonds, Rank.Seven);

    hand.add(c1);
    hand.add(c2);

    ComputerBrain brain = new ComputerBrain(hand, board, 1, BET);
    int result = brain.calculateBet();
    assertEquals(result, BET);
  }

  @Test
  public void testBluff() {
    Card c1 = new Card(Suit.Clubs, Rank.Two);
    Card c2 = new Card(Suit.Diamonds, Rank.Three);
    Card c3 = new Card(Suit.Hearts, Rank.Ten);
    Card c4 = new Card(Suit.Spades, Rank.Jack);
    Card c5 = new Card(Suit.Spades, Rank.Queen);

    hand.add(c1);
    hand.add(c2);
    board.add(c3);
    board.add(c4);
    board.add(c5);

    ComputerBrain brain = new ComputerBrain(hand, board, 1, BET, 52);
    int result = brain.calculateBet();
    assertTrue(result > BET);
  }

  @Test
  public void testLowAggro() {
    Card c1 = new Card(Suit.Clubs, Rank.Two);
    Card c2 = new Card(Suit.Diamonds, Rank.Seven);
    Card c3 = new Card(Suit.Hearts, Rank.Five);
    Card c4 = new Card(Suit.Spades, Rank.Six);
    Card c5 = new Card(Suit.Spades, Rank.Seven);

    hand.add(c1);
    hand.add(c2);
    board.add(c3);
    board.add(c4);
    board.add(c5);

    ComputerBrain brain = new ComputerBrain(hand, board, 1, BET);
    int result = brain.calculateBet();
    assertTrue(result < BET * 2);
  }

  @Test
  public void testHighAggro() {
    Card aClubs = new Card(Suit.Clubs, Rank.Ace);
    Card aDiamonds = new Card(Suit.Diamonds, Rank.Ace);
    Card aHearts = new Card(Suit.Hearts, Rank.Ace);
    Card aSpades = new Card(Suit.Spades, Rank.Ace);
    Card spare = new Card(Suit.Spades, Rank.Five);

    hand.add(aClubs);
    hand.add(aDiamonds);
    board.add(aHearts);
    board.add(aSpades);
    board.add(spare);

    ComputerBrain brain = new ComputerBrain(hand, board, 1, BET);
    int result = brain.calculateBet();
    assertTrue(result > BET * 5);
  }
}