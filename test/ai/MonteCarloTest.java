package ai;

import game.deck.*;
import game.poker.player.ComputerBrain;
import org.junit.Before;
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
//    board.add(spare2);

    ComputerBrain brain = new ComputerBrain(hand, board, 1, 100);
    int result = brain.calculateBet();
    System.out.println(result);
  }
}