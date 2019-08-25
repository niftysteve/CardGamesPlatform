import org.junit.Before;
import org.junit.Test;

import java.util.List;

import game.deck.Card;
import game.deck.Rank;
import game.deck.Suit;
import game.poker.PokerGame;
import game.poker.player.PokerPlayer;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for the game of Poker.
 */
public class PokerTest {
  private PokerGame game;

  @Before
  public void init() {
    this.game = new PokerGame(1, 500, 52);
  }

  @Test
  public void testFlipCard() {
    assertEquals(0, game.getCommunity().size());
    game.flipCard();
    assertEquals(3, game.getCommunity().size());
    game.flipCard();
    assertEquals(4, game.getCommunity().size());
    game.flipCard();
    assertEquals(5, game.getCommunity().size());
    game.flipCard();
    assertEquals(5, game.getCommunity().size());
  }

  @Test
  public void testAvailablePlayers() {
    List<PokerPlayer> players = game.availablePlayers();
    assertEquals(1, players.get(0).getId());
    assertEquals(0, players.get(1).getId());

    players.get(0).fold();

    assertEquals(1, game.availablePlayers().size());
  }

  @Test
  public void testInProgress() {
    assertTrue(game.inProgress());

    List<PokerPlayer> players = game.availablePlayers();
    PokerPlayer first = players.get(0);
    first.bet(first.getMoney());

    assertTrue(game.inProgress());

    first.continuePlay();

    assertFalse(game.inProgress());
  }

  @Test
  public void testCurrentBet() {
    assertEquals(50, game.currentBet());
    game.availablePlayers().get(0).bet(200);
    assertEquals(225, game.currentBet());
  }

  @Test
  public void testBettingDone() {
    List<PokerPlayer> players = game.availablePlayers();
    for (PokerPlayer p : players) {
      p.bet( 100 - game.currentBet());
    }

    assertTrue(game.bettingDone());

    PokerPlayer first = players.get(0);
    first.bet(first.getMoney());

    assertTrue(game.bettingDone());

    PokerPlayer second = players.get(1);
    second.bet(second.getMoney());

    assertTrue(game.bettingDone());
  }

  @Test
  public void testResolveWin() {
    List<PokerPlayer> players = game.availablePlayers();
    PokerPlayer first = players.get(0);
    first.bet(50);

    first.addCard(new Card(Suit.Clubs, Rank.Ace));
    first.addCard(new Card(Suit.Clubs, Rank.King));
    first.addCard(new Card(Suit.Clubs, Rank.Queen));
    first.addCard(new Card(Suit.Clubs, Rank.Jack));
    first.addCard(new Card(Suit.Clubs, Rank.Ten));

    PokerPlayer second = players.get(1);
    second.bet(50);

    second.addCard(new Card(Suit.Clubs, Rank.Six));
    second.addCard(new Card(Suit.Clubs, Rank.Five));
    second.addCard(new Card(Suit.Clubs, Rank.Four));
    second.addCard(new Card(Suit.Clubs, Rank.Three));
    second.addCard(new Card(Suit.Clubs, Rank.Two));

    assertEquals(1, first.getId());
    assertEquals(0, second.getId());
    assertEquals("1", game.resolveWin());
    assertEquals(550, first.getMoney());
    assertEquals(375, second.getMoney());

    first.bet(75);
    first.addCard(new Card(Suit.Clubs, Rank.Ace));
    first.addCard(new Card(Suit.Clubs, Rank.King));
    first.addCard(new Card(Suit.Clubs, Rank.Queen));
    first.addCard(new Card(Suit.Clubs, Rank.Jack));
    first.addCard(new Card(Suit.Clubs, Rank.Ten));

    second.addCard(new Card(Suit.Clubs, Rank.Ace));
    second.addCard(new Card(Suit.Clubs, Rank.King));
    second.addCard(new Card(Suit.Clubs, Rank.Queen));
    second.addCard(new Card(Suit.Clubs, Rank.Jack));
    second.addCard(new Card(Suit.Clubs, Rank.Ten));

    assertEquals("0 & 1", game.resolveWin());
    assertEquals(525, first.getMoney());
    assertEquals(400, second.getMoney());
  }

  @Test
  public void testAllRaise() {
    assertFalse(game.allRaised());

    for (PokerPlayer p : game.availablePlayers()) {
      p.bet(25);
    }

    assertTrue(game.allRaised());
  }

  @Test
  public void testCloseRound() {
    assertFalse(game.closeRound());

    PokerPlayer first = game.availablePlayers().get(0);
    first.fold();

    assertTrue(game.closeRound());

    first.continuePlay();

    assertFalse(game.closeRound());

    game.flipCard();
    game.flipCard();
    game.flipCard();

    assertTrue(game.closeRound());
  }
}
