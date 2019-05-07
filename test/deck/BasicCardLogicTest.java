package deck;

import org.junit.Test;

import java.util.Collections;

import game.deck.Card;
import game.deck.Rank;
import game.deck.Suit;
import game.poker.rules.CardLogic;
import game.deck.Hand;
import game.poker.rules.HandRank;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for determining hand ranks in basic scenarios while restrained to a standard Rank.FiveRank.Two-card deck
 * and seven card poker hand (two from hand, five from community).
 */
public class BasicCardLogicTest extends AbstractCardLogicTest {

  @Test
  public void testFindRoyalFlush() {
    hand.addCard(new Card(Suit.Clubs, Rank.Ace));
    hand.addCard(new Card(Suit.Clubs, Rank.King));
    hand.addCard(new Card(Suit.Clubs, Rank.Queen));
    hand.addCard(new Card(Suit.Clubs, Rank.Jack));
    hand.addCard(new Card(Suit.Clubs, Rank.Ten));
    hand.addCard(new Card(Suit.Spades, Rank.Nine));
    hand.addCard(new Card(Suit.Hearts, Rank.Queen));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Royal_Flush, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(10, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testFindStraightFlush() {
    hand.addCard(new Card(Suit.Clubs, Rank.Three));
    hand.addCard(new Card(Suit.Clubs, Rank.Five));
    hand.addCard(new Card(Suit.Clubs, Rank.Ace));
    hand.addCard(new Card(Suit.Clubs, Rank.Four));
    hand.addCard(new Card(Suit.Clubs, Rank.Two));
    hand.addCard(new Card(Suit.Hearts, Rank.Eight));
    hand.addCard(new Card(Suit.Diamonds, Rank.Ten));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Straight_Flush, logic.findRank());


    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(2, (int) Collections.min(ranked.getValues()));
    assertTrue(ranked.getValues().contains(Rank.Five.getValue()));
  }

  @Test
  public void testFindFourKind() {
    hand.addCard(new Card(Suit.Diamonds, Rank.Jack));
    hand.addCard(new Card(Suit.Clubs, Rank.Jack));
    hand.addCard(new Card(Suit.Spades, Rank.Seven));
    hand.addCard(new Card(Suit.Spades, Rank.Jack));
    hand.addCard(new Card(Suit.Hearts, Rank.Ten));
    hand.addCard(new Card(Suit.Hearts, Rank.Two));
    hand.addCard(new Card(Suit.Hearts, Rank.Jack));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Four_Kind, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(4, (int) ranked.allValues().get(Rank.Jack.getValue()));
  }

  @Test
  public void testFindFullHouse() {
    hand.addCard(new Card(Suit.Diamonds, Rank.Jack));
    hand.addCard(new Card(Suit.Clubs, Rank.Jack));
    hand.addCard(new Card(Suit.Hearts, Rank.Ten));
    hand.addCard(new Card(Suit.Hearts, Rank.Two));
    hand.addCard(new Card(Suit.Spades, Rank.Ten));
    hand.addCard(new Card(Suit.Spades, Rank.Seven));
    hand.addCard(new Card(Suit.Spades, Rank.Jack));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Full_House, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(11, (int) Collections.max(ranked.getValues()));
    assertEquals(10, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testFindFlush() {
    hand.addCard(new Card(Suit.Spades, Rank.Two));
    hand.addCard(new Card(Suit.Spades, Rank.Seven));
    hand.addCard(new Card(Suit.Spades, Rank.Jack));
    hand.addCard(new Card(Suit.Spades, Rank.Six));
    hand.addCard(new Card(Suit.Spades, Rank.Nine));
    hand.addCard(new Card(Suit.Clubs, Rank.Jack));
    hand.addCard(new Card(Suit.Hearts, Rank.Ten));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Flush, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(5, Collections.frequency(ranked.getSuits(),Suit.Spades));
  }

  @Test
  public void testFindStraight() {
    hand.addCard(new Card(Suit.Spades, Rank.Queen));
    hand.addCard(new Card(Suit.Diamonds, Rank.Jack));
    hand.addCard(new Card(Suit.Spades, Rank.Ten));
    hand.addCard(new Card(Suit.Clubs, Rank.Nine));
    hand.addCard(new Card(Suit.Hearts, Rank.Eight));
    hand.addCard(new Card(Suit.Spades, Rank.Three));
    hand.addCard(new Card(Suit.Spades, Rank.Two));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Straight, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(12, (int) Collections.max(ranked.getValues()));
    assertEquals(8, (int) Collections.min(ranked.getValues()));
  }

  @Test
  public void testFindThreeKind() {
    hand.addCard(new Card(Suit.Clubs, Rank.Seven));
    hand.addCard(new Card(Suit.Spades, Rank.Eight));
    hand.addCard(new Card(Suit.Spades, Rank.Six));
    hand.addCard(new Card(Suit.Diamonds, Rank.Six));
    hand.addCard(new Card(Suit.Hearts, Rank.Six));
    hand.addCard(new Card(Suit.Hearts, Rank.Two));
    hand.addCard(new Card(Suit.Hearts, Rank.Ace));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Three_Kind, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    assertEquals(3, (int) ranked.allValues().get(Rank.Six.getValue()));
  }

  @Test
  public void testFindTwoPair() {
    hand.addCard(new Card(Suit.Clubs, Rank.Nine));
    hand.addCard(new Card(Suit.Spades, Rank.Nine));
    hand.addCard(new Card(Suit.Spades, Rank.Six));
    hand.addCard(new Card(Suit.Diamonds, Rank.Six));
    hand.addCard(new Card(Suit.Hearts, Rank.Five));
    hand.addCard(new Card(Suit.Hearts, Rank.Two));
    hand.addCard(new Card(Suit.Hearts, Rank.King));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Two_Pair, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(13, (int) Collections.max(ranked.getValues()));
    assertEquals(2, (int) ranked.allValues().get(Rank.Six.getValue()));
    assertEquals(2, (int) ranked.allValues().get(Rank.Nine.getValue()));
  }

  @Test
  public void testFindPair() {
    hand.addCard(new Card(Suit.Clubs, Rank.Five));
    hand.addCard(new Card(Suit.Diamonds, Rank.Six));
    hand.addCard(new Card(Suit.Hearts, Rank.Seven));
    hand.addCard(new Card(Suit.Spades, Rank.Three));
    hand.addCard(new Card(Suit.Spades, Rank.Jack));
    hand.addCard(new Card(Suit.Spades, Rank.Nine));
    hand.addCard(new Card(Suit.Spades, Rank.Five));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.Pair, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(2, (int) ranked.allValues().get(Rank.Five.getValue()));
  }

  @Test
  public void testFindHighCard() {
    hand.addCard(new Card(Suit.Clubs, Rank.Ace));
    hand.addCard(new Card(Suit.Diamonds, Rank.King));
    hand.addCard(new Card(Suit.Hearts, Rank.Ten));
    hand.addCard(new Card(Suit.Hearts, Rank.Nine));
    hand.addCard(new Card(Suit.Spades, Rank.Seven));
    hand.addCard(new Card(Suit.Spades, Rank.Four));
    hand.addCard(new Card(Suit.Spades, Rank.Two));

    CardLogic logic = new CardLogic(hand);
    assertEquals(HandRank.High_Card, logic.findRank());

    Hand ranked = logic.rankedHand();
    assertEquals(14, (int) Collections.max(ranked.getValues()));
    for (Integer value : ranked.allValues().values()) {
      assertEquals(1, (int) value);
    }
  }
}
