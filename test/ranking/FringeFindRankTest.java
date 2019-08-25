package ranking;

import org.junit.Test;

import java.util.Collections;

import game.deck.Card;
import game.deck.Rank;
import game.deck.Suit;
import game.poker.rules.FindRank;
import game.deck.Hand;
import game.poker.rules.HandRank;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for determining hand ranks in fringe scenarios while restrained to
 * a standard 52 card deck.
 */
public class FringeFindRankTest extends AbstractFindRankTest {

  @Test
  public void testRoyalFlushConflicts() {
    hand.addCard(new Card(Suit.Spades, Rank.Ace));
    hand.addCard(new Card(Suit.Spades, Rank.King));
    hand.addCard(new Card(Suit.Spades, Rank.Queen));
    hand.addCard(new Card(Suit.Spades, Rank.Jack));
    hand.addCard(new Card(Suit.Spades, Rank.Ten));
    hand.addCard(new Card(Suit.Clubs, Rank.Ace));
    hand.addCard(new Card(Suit.Hearts, Rank.Ace));
    hand.addCard(new Card(Suit.Diamonds, Rank.Ace));

    FindRank logic = new FindRank(hand);
    assertEquals(HandRank.Royal_Flush, logic.getRank());

    Hand ranked = logic.getRankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(10, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testExtendedStraightFlush() {
    hand.addCard(new Card(Suit.Spades, Rank.King));
    hand.addCard(new Card(Suit.Spades, Rank.Queen));
    hand.addCard(new Card(Suit.Spades, Rank.Jack));
    hand.addCard(new Card(Suit.Spades, Rank.Ten));
    hand.addCard(new Card(Suit.Spades, Rank.Nine));
    hand.addCard(new Card(Suit.Spades, Rank.Eight));
    hand.addCard(new Card(Suit.Spades, Rank.Seven));
    hand.addCard(new Card(Suit.Spades, Rank.Six));
    hand.addCard(new Card(Suit.Spades, Rank.Five));
    hand.addCard(new Card(Suit.Spades, Rank.Four));
    hand.addCard(new Card(Suit.Spades, Rank.Three));
    hand.addCard(new Card(Suit.Spades, Rank.Two));

    FindRank logic = new FindRank(hand);
    assertEquals(HandRank.Straight_Flush, logic.getRank());

    Hand ranked = logic.getRankedHand();
    assertEquals(13, (int) Collections.max(ranked.getValues()));
    assertEquals(9, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testMultiFourKind() {
    hand.addCard(new Card(Suit.Spades, Rank.Queen));
    hand.addCard(new Card(Suit.Hearts, Rank.Queen));
    hand.addCard(new Card(Suit.Diamonds, Rank.Queen));
    hand.addCard(new Card(Suit.Clubs, Rank.Queen));
    hand.addCard(new Card(Suit.Spades, Rank.King));
    hand.addCard(new Card(Suit.Hearts, Rank.King));
    hand.addCard(new Card(Suit.Diamonds, Rank.King));
    hand.addCard(new Card(Suit.Clubs, Rank.King));

    FindRank logic = new FindRank(hand);
    assertEquals(HandRank.Four_Kind, logic.getRank());

    Hand ranked = logic.getRankedHand();
    assertEquals(4, (int) ranked.allValues().get(Rank.King.getValue()));
    assertEquals(1, (int) ranked.allValues().get(Rank.Queen.getValue()));
  }

  @Test
  public void testDoubleFullHouse() {
    hand.addCard(new Card(Suit.Spades, Rank.Seven));
    hand.addCard(new Card(Suit.Hearts, Rank.Seven));
    hand.addCard(new Card(Suit.Diamonds, Rank.Seven));
    hand.addCard(new Card(Suit.Clubs, Rank.Five));
    hand.addCard(new Card(Suit.Spades, Rank.Five));
    hand.addCard(new Card(Suit.Hearts, Rank.Five));

    FindRank logic = new FindRank(hand);
    assertEquals(HandRank.Full_House, logic.getRank());

    Hand ranked = logic.getRankedHand();
    assertEquals(3, (int) ranked.allValues().get(Rank.Seven.getValue()));
    assertEquals(2, (int) ranked.allValues().get(Rank.Five.getValue()));
  }

  @Test
  public void testDoubleFlush() {
    hand.addCard(new Card(Suit.Clubs, Rank.King));
    hand.addCard(new Card(Suit.Clubs, Rank.Jack));
    hand.addCard(new Card(Suit.Clubs, Rank.Nine));
    hand.addCard(new Card(Suit.Clubs, Rank.Four));
    hand.addCard(new Card(Suit.Clubs, Rank.Two));
    hand.addCard(new Card(Suit.Hearts, Rank.Ten));
    hand.addCard(new Card(Suit.Hearts, Rank.Eight));
    hand.addCard(new Card(Suit.Hearts, Rank.Six));
    hand.addCard(new Card(Suit.Hearts, Rank.Five));
    hand.addCard(new Card(Suit.Hearts, Rank.Three));


    FindRank logic = new FindRank(hand);
    assertEquals(HandRank.Flush, logic.getRank());

    Hand ranked = logic.getRankedHand();
    assertEquals(13, (int) Collections.max(ranked.getValues()));
    assertEquals(2, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testExtendedStraight() {
    hand.addCard(new Card(Suit.Spades, Rank.Ten));
    hand.addCard(new Card(Suit.Diamonds, Rank.Nine));
    hand.addCard(new Card(Suit.Spades, Rank.Eight));
    hand.addCard(new Card(Suit.Clubs, Rank.Seven));
    hand.addCard(new Card(Suit.Hearts, Rank.Six));
    hand.addCard(new Card(Suit.Spades, Rank.Five));
    hand.addCard(new Card(Suit.Spades, Rank.Four));
    hand.addCard(new Card(Suit.Diamonds, Rank.Three));
    hand.addCard(new Card(Suit.Hearts, Rank.Two));
    hand.addCard(new Card(Suit.Hearts, Rank.Ace));

    FindRank logic = new FindRank(hand);
    assertEquals(HandRank.Straight, logic.getRank());

    Hand ranked = logic.getRankedHand();
    assertEquals(10, (int) Collections.max(ranked.getValues()));
    assertEquals(6, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testTriplePair() {
    hand.addCard(new Card(Suit.Spades, Rank.Eight));
    hand.addCard(new Card(Suit.Clubs, Rank.Eight));
    hand.addCard(new Card(Suit.Hearts, Rank.Six));
    hand.addCard(new Card(Suit.Spades, Rank.Six));
    hand.addCard(new Card(Suit.Spades, Rank.Ten));
    hand.addCard(new Card(Suit.Diamonds, Rank.Ten));

    FindRank logic = new FindRank(hand);
    assertEquals(HandRank.Two_Pair, logic.getRank());

    Hand ranked = logic.getRankedHand();
    assertEquals(2, (int) ranked.allValues().get(Rank.Ten.getValue()));
    assertEquals(2, (int) ranked.allValues().get(Rank.Eight.getValue()));
    assertEquals(1, (int) ranked.allValues().get(Rank.Six.getValue()));
  }
}
