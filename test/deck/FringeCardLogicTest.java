package deck;

import org.junit.Test;

import java.util.Collections;

import game.deck.Card;
import game.poker.player.CardLogic;
import game.deck.Hand;
import game.deck.HandRank;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for determining hand ranks in fringe scenarios while restrained to
 * a standard 52-card deck.
 */
public class FringeCardLogicTest extends AbstractCardLogicTest {

  @Test
  public void testRoyalFlushConflicts() {
    hand.addCard(new Card("Spades", 14));
    hand.addCard(new Card("Spades", 13));
    hand.addCard(new Card("Spades", 12));
    hand.addCard(new Card("Spades", 11));
    hand.addCard(new Card("Spades", 10));
    hand.addCard(new Card("Clubs", 14));
    hand.addCard(new Card("Hearts", 14));
    hand.addCard(new Card("Diamonds", 14));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Royal_Flush, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(10, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testExtendedStraightFlush() {
    hand.addCard(new Card("Spades", 13));
    hand.addCard(new Card("Spades", 12));
    hand.addCard(new Card("Spades", 11));
    hand.addCard(new Card("Spades", 10));
    hand.addCard(new Card("Spades", 9));
    hand.addCard(new Card("Spades", 8));
    hand.addCard(new Card("Spades", 7));
    hand.addCard(new Card("Spades", 6));
    hand.addCard(new Card("Spades", 5));
    hand.addCard(new Card("Spades", 4));
    hand.addCard(new Card("Spades", 3));
    hand.addCard(new Card("Spades", 2));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Straight_Flush, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(13, (int) Collections.max(ranked.getValues()));
    assertEquals(9, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testMultiFourKind() {
    hand.addCard(new Card("Spades", 12));
    hand.addCard(new Card("Hearts", 12));
    hand.addCard(new Card("Diamonds", 12));
    hand.addCard(new Card("Clubs", 12));
    hand.addCard(new Card("Spades", 13));
    hand.addCard(new Card("Hearts", 13));
    hand.addCard(new Card("Diamonds", 13));
    hand.addCard(new Card("Clubs", 13));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Four_Kind, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(4, (int) ranked.allValues().get(13));
    assertEquals(1, (int) ranked.allValues().get(12));
  }

  @Test
  public void testDoubleFullHouse() {
    hand.addCard(new Card("Spades", 7));
    hand.addCard(new Card("Hearts", 7));
    hand.addCard(new Card("Diamonds", 7));
    hand.addCard(new Card("Clubs", 5));
    hand.addCard(new Card("Spades", 5));
    hand.addCard(new Card("Hearts", 5));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Full_House, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(3, (int) ranked.allValues().get(7));
    assertEquals(2, (int) ranked.allValues().get(5));
  }

  @Test
  public void testDoubleFlush() {
    hand.addCard(new Card("Hearts", 10));
    hand.addCard(new Card("Hearts", 8));
    hand.addCard(new Card("Hearts", 6));
    hand.addCard(new Card("Hearts", 5));
    hand.addCard(new Card("Hearts", 3));
    hand.addCard(new Card("Clubs", 13));
    hand.addCard(new Card("Clubs", 11));
    hand.addCard(new Card("Clubs", 9));
    hand.addCard(new Card("Clubs", 4));
    hand.addCard(new Card("Clubs", 2));


    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Flush, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(13, (int) Collections.max(ranked.getValues()));
    assertEquals(2, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testExtendedStraight() {
    hand.addCard(new Card("Spades", 10));
    hand.addCard(new Card("Diamonds", 9));
    hand.addCard(new Card("Spades", 8));
    hand.addCard(new Card("Clubs", 7));
    hand.addCard(new Card("Hearts", 6));
    hand.addCard(new Card("Spades", 5));
    hand.addCard(new Card("Spades", 4));
    hand.addCard(new Card("Diamonds", 3));
    hand.addCard(new Card("Hearts", 2));
    hand.addCard(new Card("Hearts", 14));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Straight, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(10, (int) Collections.max(ranked.getValues()));
    assertEquals(6, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testTriplePair() {
    hand.addCard(new Card("Spades", 8));
    hand.addCard(new Card("Clubs", 8));
    hand.addCard(new Card("Hearts", 6));
    hand.addCard(new Card("Spades", 6));
    hand.addCard(new Card("Spades", 10));
    hand.addCard(new Card("Diamonds", 10));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Two_Pair, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(2, (int) ranked.allValues().get(10));
    assertEquals(2, (int) ranked.allValues().get(8));
    assertEquals(1, (int) ranked.allValues().get(6));
  }
}
