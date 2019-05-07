package deck;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.StandardDeck;
import game.deck.Suit;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for a standard deck of playing cards.
 */
public class StandardDeckTest {
  private Deck deck;

  @Before
  public void init() {
    this.deck = new StandardDeck(52);
  }

  @Test(expected = IllegalArgumentException.class)
  public void zeroDeal() {
    List<Hand> zeroPlayer = deck.dealCards(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void excessDeal() {
    List<Hand> excess = deck.dealCards(5, 11);
  }

  @Test(expected = IllegalArgumentException.class)
  public void lackDraw() {
    deck.burnCards(52);
    Card noCard = deck.drawCard();
  }

  @Test(expected = IllegalArgumentException.class)
  public void lackBurn() {
    deck.burnCards(52);
    deck.burnCards(1);
  }

  @Test
  public void testFormation() {
    int spade = 0;
    int club = 0;
    int heart = 0;
    int diamond = 0;
    for (int i = 0; i < 52; i++) {
      Card card = deck.drawCard();

      switch(card.getSuit()) {
        case Spades:
          spade += 1;
          break;
        case Clubs:
          club += 1;
          break;
        case Hearts:
          heart += 1;
          break;
        case Diamonds:
          diamond += 1;
          break;
        default:
          throw new IllegalArgumentException("Invalid suit found");
      }
    }

    assertEquals(13, spade);
    assertEquals(13, club);
    assertEquals(13, heart);
    assertEquals(13, diamond);
  }

  @Test
  public void testDeal() {
    List<Hand> zeroHand = deck.dealCards(1, 0);
    assertEquals(1, zeroHand.size());
    for (Hand hand : zeroHand) {
      assertEquals(0, hand.getCards().size());
    }

    List<Hand> singleHand = deck.dealCards(1, 1);
    assertEquals(1, singleHand.size());
    for (Hand hand : singleHand) {
      assertEquals(1, hand.getCards().size());
    }

    List<Hand> multiHand = deck.dealCards(5, 10);
    assertEquals(5, multiHand.size());
    for (Hand hand : multiHand) {
      assertEquals(10, hand.getCards().size());
    }
  }

  @Test
  public void testDraw() {
    Card firstDraw = deck.drawCard();
    assertEquals(5, firstDraw.getRank());
    assertEquals("5", firstDraw.getNamedValue());
    assertEquals(Suit.Spades, firstDraw.getSuit());
    assertEquals(51, deck.remainingCards());

    Card secondDraw = deck.drawCard();
    assertEquals(9, secondDraw.getRank());
    assertEquals("9", secondDraw.getNamedValue());
    assertEquals(Suit.Spades, secondDraw.getSuit());
    assertEquals(50, deck.remainingCards());

    Card thirdDraw = deck.drawCard();
    assertEquals(2, thirdDraw.getRank());
    assertEquals("2", thirdDraw.getNamedValue());
    assertEquals(Suit.Spades, thirdDraw.getSuit());
    assertEquals(49, deck.remainingCards());
  }

  @Test
  public void testBurn() {
    deck.burnCards(0);
    assertEquals(52, deck.remainingCards());

    deck.burnCards(-1);
    assertEquals(52, deck.remainingCards());

    deck.burnCards(1);
    assertEquals(51, deck.remainingCards());

    deck.burnCards(51);
    assertEquals(0, deck.remainingCards());
  }
}
