package deck;

import org.junit.After;
import org.junit.Before;

import game.poker.rules.CardLogic;
import game.deck.Hand;

import static junit.framework.TestCase.assertEquals;

/**
 * Maintains consistency across CardLogic tests.
 */
public abstract class AbstractCardLogicTest {
  protected Hand hand;

  @Before
  public void init() {
    this.hand = new Hand();
  }

  @After
  public void handSize() {
    CardLogic logic = new CardLogic(hand);
    assertEquals(5, logic.rankedHand().getCards().size());
  }
}
