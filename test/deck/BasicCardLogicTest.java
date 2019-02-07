package deck;

import org.junit.Test;

import java.util.Collections;

import game.deck.Card;
import game.poker.player.CardLogic;
import game.deck.Hand;
import game.deck.HandRank;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for determining hand ranks in basic scenarios while restrained to a standard 52-card deck
 * and seven card poker hand (two from hand, five from community).
 */
public class BasicCardLogicTest extends AbstractCardLogicTest {

  @Test
  public void testFindRoyalFlush() {
    hand.addCard(new Card("Clubs", 14));
    hand.addCard(new Card("Clubs", 13));
    hand.addCard(new Card("Clubs", 12));
    hand.addCard(new Card("Clubs", 11));
    hand.addCard(new Card("Clubs", 10));
    hand.addCard(new Card("Spades", 9));
    hand.addCard(new Card("Hearts", 12));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Royal_Flush, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(10, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testFindStraightFlush() {
    hand.addCard(new Card("Clubs", 3));
    hand.addCard(new Card("Clubs", 5));
    hand.addCard(new Card("Clubs", 14));
    hand.addCard(new Card("Clubs", 4));
    hand.addCard(new Card("Clubs", 2));
    hand.addCard(new Card("Hearts", 8));
    hand.addCard(new Card("Diamonds", 10));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Straight_Flush, logic.findRank());


    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(2, (int) Collections.min(ranked.getValues()));
    assertTrue(ranked.getValues().contains(5));
  }

  @Test
  public void testFindFourKind() {
    hand.addCard(new Card("Diamonds", 11));
    hand.addCard(new Card("Clubs", 11));
    hand.addCard(new Card("Spades", 7));
    hand.addCard(new Card("Spades", 11));
    hand.addCard(new Card("Hearts", 10));
    hand.addCard(new Card("Hearts", 2));
    hand.addCard(new Card("Hearts", 11));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Four_Kind, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(4, (int) ranked.allValues().get(11));
  }

  @Test
  public void testFindFullHouse() {
    hand.addCard(new Card("Diamonds", 11));
    hand.addCard(new Card("Clubs", 11));
    hand.addCard(new Card("Hearts", 10));
    hand.addCard(new Card("Hearts", 2));
    hand.addCard(new Card("Spades", 10));
    hand.addCard(new Card("Spades", 7));
    hand.addCard(new Card("Spades", 11));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Full_House, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(11, (int) Collections.max(ranked.getValues()));
    assertEquals(10, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testFindFlush() {
    hand.addCard(new Card("Spades", 2));
    hand.addCard(new Card("Spades", 7));
    hand.addCard(new Card("Spades", 11));
    hand.addCard(new Card("Spades", 6));
    hand.addCard(new Card("Spades", 9));
    hand.addCard(new Card("Clubs", 11));
    hand.addCard(new Card("Hearts", 10));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Flush, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(5, Collections.frequency(ranked.getSuits(),"Spades"));
  }

  @Test
  public void testFindStraight() {
    hand.addCard(new Card("Spades", 12));
    hand.addCard(new Card("Diamonds", 11));
    hand.addCard(new Card("Spades", 10));
    hand.addCard(new Card("Clubs", 9));
    hand.addCard(new Card("Hearts", 8));
    hand.addCard(new Card("Spades", 3));
    hand.addCard(new Card("Spades", 2));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Straight, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(12, (int) Collections.max(ranked.getValues()));
    assertEquals(8, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testFindThreeKind() {
    hand.addCard(new Card("Clubs", 7));
    hand.addCard(new Card("Spades", 8));
    hand.addCard(new Card("Spades", 6));
    hand.addCard(new Card("Diamonds", 6));
    hand.addCard(new Card("Hearts", 6));
    hand.addCard(new Card("Hearts", 2));
    hand.addCard(new Card("Hearts", 14));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Three_Kind, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(3, (int) ranked.allValues().get(6));
  }

  @Test
  public void testFindTwoPair() {
    hand.addCard(new Card("Clubs", 9));
    hand.addCard(new Card("Spades", 9));
    hand.addCard(new Card("Spades", 6));
    hand.addCard(new Card("Diamonds", 6));
    hand.addCard(new Card("Hearts", 5));
    hand.addCard(new Card("Hearts", 2));
    hand.addCard(new Card("Hearts", 13));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Two_Pair, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(13, (int) Collections.max(ranked.getValues()));
    assertEquals(2, (int) ranked.allValues().get(6));
    assertEquals(2, (int) ranked.allValues().get(9));
  }

  @Test
  public void testFindPair() {
    hand.addCard(new Card("Clubs", 5));
    hand.addCard(new Card("Diamonds", 6));
    hand.addCard(new Card("Hearts", 7));
    hand.addCard(new Card("Spades", 3));
    hand.addCard(new Card("Spades", 11));
    hand.addCard(new Card("Spades", 9));
    hand.addCard(new Card("Spades", 5));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Pair, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(2, (int) ranked.allValues().get(5));
  }

  @Test
  public void testFindHighCard() {
    hand.addCard(new Card("Clubs", 14));
    hand.addCard(new Card("Diamonds", 13));
    hand.addCard(new Card("Hearts", 10));
    hand.addCard(new Card("Hearts", 9));
    hand.addCard(new Card("Spades", 7));
    hand.addCard(new Card("Spades", 4));
    hand.addCard(new Card("Spades", 2));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.High_Card, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    for (Integer value : ranked.allValues().values()) {
      assertEquals(1, (int) value);
    }
  }
}
