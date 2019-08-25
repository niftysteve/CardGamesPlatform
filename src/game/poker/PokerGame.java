package game.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.StandardDeck;
import game.poker.player.ComputerPlayer;
import game.poker.player.PokerPlayer;
import game.poker.rules.HandRank;
import game.util.CyclicSet;

/**
 * Represents the game of Poker.
 */
public class PokerGame {
  private List<PokerPlayer> players = new ArrayList<>();
  private List<Card> communityCards = new ArrayList<>();
  private int button = 0;
  private Deck deck;

  /**
   * Constructs a game of Poker.
   * @param computer the number of computer players
   * @param startMoney the amount of money each player starts with
   */
  public PokerGame(int computer, int startMoney) {
    this.deck = new StandardDeck();
    createPlayers(computer, startMoney);
  }

  /**
   * Constructs a game of Poker.
   * @param computer the number of computer players
   * @param startMoney the amount of money each player starts with
   */
  public PokerGame(int computer, int startMoney, int seed) {
    this.deck = new StandardDeck(seed);
    createPlayers(computer, startMoney);
  }

  /**
   * Retrieves all player IDs.
   * @return a set of all player IDs
   */
  private CyclicSet allPlayerId() {
    return players.stream().filter(PokerPlayer::isPlaying).map(PokerPlayer::getId)
            .collect(Collectors.toCollection(CyclicSet::new));
  }

  /**
   * Initializes all Poker Players and deals them their first hand.
   * @param computer the amount of computer players
   * @param startMoney the amount of money each player starts with
   */
  private void createPlayers(int computer, int startMoney) {
    players.add(new PokerPlayer(0, startMoney));

    while (players.size() != computer + 1) {
      players.add(new ComputerPlayer(players.size(), startMoney));
    }

    startRound();
  }

  /**
   * Begins a round by shuffling the deck, dealing cards, and setting up the blinds.
   */
  private void startRound() {
    deck.shuffle();
    CyclicSet cycle = allPlayerId();
    List<Hand> playerHands = deck.dealCards(cycle.size(), 2);
    players.forEach(p -> {
      if (p.isPlaying()) p.initHand(playerHands.remove(0));
    });

    Integer smallPlayer = cycle.nextItem(button);
    Integer bigPlayer = cycle.nextItem(smallPlayer);

    players.get(smallPlayer).bet(25);
    players.get(bigPlayer).bet(50);
    resetRaise();
  }

  /**
   * Starts a new round by clearing the community cards and updating the dealers and players.
   */
  private void newRound() {
    communityCards.clear();
    players.forEach(PokerPlayer::continuePlay);
    updateDealer();
    startRound();
  }

  /**
   * Changes the dealer to the next player.
   */
  private void updateDealer() {
    CyclicSet cycle = allPlayerId();
    button = cycle.nextItem(button);

    if (!players.get(button).isPlaying()) {
      updateDealer();
    }
  }

  /**
   * Adds the given amount of cards to the community table.
   */
  public void flipCard() {
    if (communityCards.size() < 5) {
      deck.burnCards(1);
      int amount = communityCards.size() == 0 ? 3 : 1;
      IntStream.range(0, amount).forEach(n -> communityCards.add(deck.drawCard()));
      resetRaise();
    }
  }

  /**
   * Gets the sum of all player's bets.
   * @return the sum of all player's bets
   */
  public int totalPot() {
    int pot = 0;
    for (PokerPlayer player : players) {
      pot += player.getBet();
    }

    return pot;
  }

  /**
   * Retrieves all available players in order starting from the left of the big blind.
   * @return a list of all players
   */
  public List<PokerPlayer> availablePlayers() {
    CyclicSet cycle = allPlayerId();
    Integer betPlayer = cycle.fromStart(button, 3);

    List<PokerPlayer> ordered = new ArrayList<>();

    for (int i = 0; i < cycle.size(); i++) {
      ordered.add(players.get(betPlayer));
      betPlayer = cycle.nextItem(betPlayer);
    }

    return ordered;
  }

  /**
   * Determines if only one player is still playing.
   * @return if the game is over
   */
  public boolean inProgress() {
    return players.stream().filter(p -> p.getMoney() > 0 || p.isPlaying()).count() > 1;
  }

  /**
   * Finds the current highest bet.
   * @return the current highest bet
   */
  public int currentBet() {
    return Collections.max(players.stream().map(PokerPlayer::getBet).collect(Collectors.toList()));
  }

  /**
   * Finds the winner(s) of the round and allots them the pot.
   * @return the ID of the round winners
   */
  public String resolveWin() {
    for (Card card : communityCards) {
      for (PokerPlayer player : players) {
        player.addCard(card);
      }
    }

    List<HandRank> allRanks = players.stream().filter(PokerPlayer::isPlaying).map(PokerPlayer::getRank)
            .collect(Collectors.toList());
    HandRank maxRank = Collections.max(allRanks, Comparator.comparing(HandRank::getValue));

    List<PokerPlayer> winners = new ArrayList<>(players);
    winners.removeIf(p -> p.getRank() != maxRank);

    if (winners.size() > 1) {
      highestHand(winners);
    }

    return allocatePot(winners);
  }

  /**
   * Resolves tiebreakers by finding the player(s) with the highest summed hand.
   * If all players have the same exact hand, then it is a draw
   * @param winners a list of players with tied hands
   */
  private void highestHand(List<PokerPlayer> winners) {
    int maxHandSum = Collections.max(winners.stream().map(PokerPlayer::totalRankedSum)
            .collect(Collectors.toList()));
    winners.removeIf(p -> p.totalRankedSum() != maxHandSum);
  }

  /**
   * Splits the pot among winners of the round.
   * @param winners a list of winning players
   * @return the IDs of the winning players
   */
  private String allocatePot(List<PokerPlayer> winners) {
    StringBuilder builder = new StringBuilder();
    int split = totalPot() / winners.size();

    for (int i = 0; i < winners.size(); i++) {
      PokerPlayer player = winners.get(i);
      player.claim(split);
      String message = player.getId() + (i < winners.size() - 1 ? " & " : "");
      builder.append(message);
    }

    newRound();

    return builder.toString();
  }

  /**
   * Determines if a round of betting is complete.
   * @return if the current round of betting is complete
   */
  public boolean bettingDone() {
    HashSet<Integer> bets = players.stream().filter(PokerPlayer::isPlaying).map(PokerPlayer::getBet)
            .collect(Collectors.toCollection(HashSet::new));

    if (bets.size() == 1) {
      return true;
    }
    else {
      HashSet<Integer> allInBets = players.stream().filter(PokerPlayer::isPlaying)
              .filter(p -> p.getMoney() == 0).map(PokerPlayer::getBet)
              .collect(Collectors.toCollection(HashSet::new));

      for (Integer bet : allInBets) {
        bets.remove(bet);
      }

      return bets.size() == 1;
    }
  }

  /**
   * Resets the status on every player's raise attempt.
   */
  private void resetRaise() {
    players.forEach(p -> p.setRaise(false));
  }

  /**
   * Determines if every player has put money into the pot.
   * @return if every player has has put money into the pot
   */
  public boolean allRaised() {
    return players.stream().filter(p -> p.getMoney() > 0 && p.isPlaying()).allMatch(PokerPlayer::getRaised);
  }

  /**
   * Determines if the round should be closed.
   * @return if the round should be closed
   */
  public boolean closeRound() {
    return communityCards.size() == 5 || players.stream().filter(PokerPlayer::isPlaying).count() == 1;
  }

  /**
   * Retrieves the current community cards.
   * @return a copy of the community cards
   */
  public List<Card> getCommunity() {
    return new ArrayList<>(communityCards);
  }

  /**
   * Converts the bets each player has into string form.
   * @return all player's bets in string form
   */
  public String betState() {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < players.size(); i++) {
      PokerPlayer player = players.get(i);
      String bet = player.getBet() + (i < players.size() - 1 ? " | " : "");
      builder.append(bet);
    }

    return builder.toString();
  }

  /**
   * Converts all community cards into string form.
   * @return all community cards in string form
   */
  public String communityState() {

    if (communityCards.size() == 0) {
      return "";
    }

    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < communityCards.size(); i++) {
      Card card = communityCards.get(i);
      String curCard = card.getNamedValue() + " of " + card.getSuit()
              + (i < communityCards.size() - 1 ? ", " : "");
      builder.append(curCard);
    }

    return builder.toString();
  }
}
