package deck;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.StandardDeck;

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

    Card cardOne = new Card("Test", 15);
    hand.addCard(cardOne);

    assertEquals(8, hand.getCards().size());
  }

  @Test
  public void testGetSuits() {
    String[] testSuits = new String[]{"Hearts", "Spades", "Hearts", "Spades",
            "Spades", "Diamonds", "Diamonds"};
    ArrayList<String> actualSuits = hand.getSuits();
    for (int i = 0; i < actualSuits.size(); i++) {
      assertEquals(testSuits[i], actualSuits.get(i));
    }
  }

  @Test
  public void testGetValues() {
    Integer[] testValues = new Integer[]{12, 3, 9, 8, 11, 5, 14};
    ArrayList<Integer> actualValues = hand.getValues();
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
    TreeMap<String, Integer> actualValues = hand.allSuits();
    ArrayList<String> keys = new ArrayList<>(actualValues.keySet());
    ArrayList<Integer> quantity = new ArrayList<>(actualValues.values());
    String[] testValues = new String[]{"Diamonds", "Hearts", "Spades"};
    Integer[] testQuantity = new Integer[]{2, 2, 3};

    for (int i = 0; i < actualValues.size(); i++) {
      assertEquals(testValues[i], keys.get(i));
    }

    for (int i = 0; i < actualValues.size(); i++) {
      assertEquals(testQuantity[i], quantity.get(i));
    }
  }
}
