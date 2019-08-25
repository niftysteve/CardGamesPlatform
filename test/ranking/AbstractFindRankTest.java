package ranking;

import org.junit.After;
import org.junit.Before;

import game.poker.rules.FindRank;
import game.deck.Hand;

import static junit.framework.TestCase.assertEquals;

/**
 * Maintains consistency across HandLogic tests.
 */
public abstract class AbstractFindRankTest {
  protected Hand hand;

  @Before
  public void init() {
    this.hand = new Hand();
  }

  @After
  public void handSize() {
    FindRank logic = new FindRank(hand);
    assertEquals(5, logic.getRankedHand().getCards().size());
  }
}
