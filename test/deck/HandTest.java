package deck;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.Rank;
import game.deck.StandardDeck;
import game.deck.Suit;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for a hand of playing cards
 */
public class HandTest {
  private Hand hand;

  @Before
  public void init() {
    Deck deck = new StandardDeck(52);
    hand = deck.dealCards(1, 7).get(0);
  }

  @Test
  public void testAddCard() {
    assertEquals(7, hand.getCards().size());

    Card cardOne = new Card(Suit.Clubs, Rank.Ace);
    hand.addCard(cardOne);

    assertEquals(8, hand.getCards().size());
  }

  @Test
  public void testGetSuits() {
    Suit[] testSuits = new Suit[]{Suit.Hearts, Suit.Spades, Suit.Hearts, Suit.Spades,
            Suit.Spades, Suit.Diamonds, Suit.Diamonds};
    List<Suit> actualSuits = hand.getSuits();
    for (int i = 0; i < actualSuits.size(); i++) {
      assertEquals(testSuits[i], actualSuits.get(i));
    }
  }

  @Test
  public void testGetValues() {
    Integer[] testValues = new Integer[]{12, 3, 9, 8, 11, 5, 14};
    List<Integer> actualValues = hand.getValues();
    for (int i = 0; i < actualValues.size(); i++) {
      assertEquals(testValues[i], actualValues.get(i));
    }
  }

  @Test
  public void testAllValues() {
    TreeMap<Integer, Integer> actualValues = hand.allValues();
    ArrayList<Integer> keys = new ArrayList<>(actualValues.keySet());
    ArrayList<Integer> quantity = new ArrayList<>(actualValues.values());
    Integer[] testValues = new Integer[]{3, 5, 8, 9, 11, 12, 14};
    Integer[] testQuantity = new Integer[]{1};

    for (int i = 0; i < actualValues.size(); i++) {
      assertEquals(testValues[i], keys.get(i));
    }

    for (int i = 0; i < actualValues.size(); i++) {
      assertEquals(testQuantity[0], quantity.get(i));
    }
  }

  @Test
  public void testAllSuits() {
    HashMap<Suit, Integer> actualValues = hand.allSuits();
    List<Suit> keys = new ArrayList<>(actualValues.keySet());
    List<Integer> quantity = new ArrayList<>(actualValues.values());
    Suit[] testValues = new Suit[]{Suit.Diamonds, Suit.Hearts, Suit.Spades};
    Integer[] testQuantity = new Integer[]{2, 2, 3};

    for (int i = 0; i < actualValues.size(); i++) {
      assertEquals(testValues[i], keys.get(i));
    }

    for (int i = 0; i < actualValues.size(); i++) {
      assertEquals(testQuantity[i], quantity.get(i));
    }
  }
}
