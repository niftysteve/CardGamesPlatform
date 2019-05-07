package game.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.StandardDeck;
import game.poker.player.ComputerPlayer;
import game.poker.player.Player;
import game.poker.player.PokerPlayer;
import game.poker.rules.HandRank;
import game.util.CyclicSet;

/**
 * Represents the game of Poker.
 */
public class PokerGame {
  private ArrayList<Player> players = new ArrayList<>();
  private ArrayList<Card> communityCards = new ArrayList<>();
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
    return players.stream().map(Player::getId)
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

    deal();
    blindSetup();
  }

  /**
   * Reshuffles the deck and deals new cards to each player.
   */
  public void deal() {
    deck.shuffle();
    List<Hand> playerHands = deck.dealCards(players.size(), 2);
    players.forEach(p -> p.initHand(playerHands.remove(0)));
  }

  /**
   * Adds the given amount of cards to the community table.
   * @param amount the amount of cards to be flipped
   */
  public void flipCard(int amount) {
    deck.burnCards(1);
    IntStream.range(0, amount).forEach(n -> communityCards.add(deck.drawCard()));
  }

  /**
   * Sets up the big blind and small blind players.
   */
  private void blindSetup() {

    CyclicSet cycle = allPlayerId();
    Integer smallPlayer = cycle.nextItem(button);
    Integer bigPlayer = cycle.nextItem(smallPlayer);

    players.get(smallPlayer).bet(25, false);
    players.get(bigPlayer).bet(50, false);
  }

  /**
   * Gets the sum of all player's bets.
   * @return the sum of all player's bets
   */
  public int totalPot() {

    int pot = 0;
    for (Player player : players) {
      pot += player.getBet();
    }

    return pot;
  }

  /**
   * Retrieves all players in order starting from the left of the big blind.
   * @return a list of all players
   */
  public ArrayList<Player> getPlayers() {
    CyclicSet cycle = allPlayerId();
    Integer betPlayer = cycle.fromStart(button, 3);

    ArrayList<Player> ordered = new ArrayList<>();

    for (int i = 0; i < players.size(); i++) {
      ordered.add(players.get(betPlayer));
      betPlayer = cycle.nextItem(betPlayer);
    }

    return ordered;
  }

  /**
   * Determines if only one player is still playing.
   * @return if the game is over
   */
  public boolean isGameOver() {
    int playCount = 0;
    for (Player player : players) {
      if (player.isPlaying()) {
        playCount += 1;
      }
    }

    return playCount == 1;
  }

  /**
   * Gets the current highest bet.
   * @return the current highest bet
   */
  public int getCurHigh() {
    return Collections.max(players.stream().map(Player::getBet).collect(Collectors.toList()));
  }

  /**
   * Converts the bets each player has into string form.
   * @return all player's bets in string form
   */
  public String betState() {
    StringBuilder builder = new StringBuilder();

    for (Player player : players) {
      String bet = player.getBet() + " | ";
      builder.append(bet);
    }

    String allBets = builder.toString();

    return allBets.substring(0, allBets.length() - 3);
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

    for (Card card : communityCards) {
      String curCard = card.getNamedValue() + " of " + card.getSuit() + ", ";
      builder.append(curCard);
    }

    String community = builder.toString();

    return community.substring(0, community.length() - 2);
  }

  /**
   * Finds the winners of the round and allots them the pot.
   * @return the ID of the round winners
   */
  public String resolveWin() {
    ArrayList<Player> winners = new ArrayList<>(players);
    winners.removeIf(p -> !p.isPlaying());

    int[] highestRank = new int[1];
    for (Player player : winners) {
      player.addCard(communityCards);
      HandRank rank = player.getRank();
      int rankValue = rank.getValue();

      if (rankValue > highestRank[0]) {
        highestRank[0] = rankValue;
      }
    }

    winners.removeIf(p -> p.getRank().getValue() != highestRank[0]);

    if (winners.size() != 1) {
      return splitPot(highestHand(winners));
    }
    else {
      Player victor = winners.get(0);
      victor.claim(totalPot());
      newRound();

      return Integer.toString(victor.getId());
    }
  }

  /**
   * Resolves tiebreakers by finding the player with the highest value card.
   * If all players have the same exact hand, then it is a draw
   * @param winners a list of players with tied hands
   * @return a list of players who won
   */
  private ArrayList<Player> highestHand(ArrayList<Player> winners) {
    ArrayList<Player> finalWinner = new ArrayList<>(winners);
    int minSize = Collections.min(winners.stream().map(p -> p.getRankedHand().getCards().size())
            .collect(Collectors.toList()));

    for (int i = 0; i < minSize; i++) {
      if (finalWinner.size() == 1) {
        break;
      }

      int[] maxVal = new int[]{0};
      for (Player player : winners) {
        CyclicSet cycle = new CyclicSet(player.getRankedHand().getValues());
        int curMax = cycle.fromEnd(cycle.last(), i);

        if (curMax > maxVal[0]) {
          maxVal[0] = curMax;
        }
      }
      finalWinner.removeIf(p -> !p.getRankedHand().getValues().contains(maxVal[0]));
    }

    return finalWinner;
  }

  /**
   * Splits the pot among winners of the round.
   * @param winners a list of winning players
   * @return the IDs of the winning players
   */
  private String splitPot(ArrayList<Player> winners) {

    StringBuilder builder = new StringBuilder();
    int split = totalPot() / winners.size();
    for (Player player : winners) {
      player.claim(split);
      String curWin = player.getId() + " & ";
      builder.append(curWin);
    }

    newRound();
    String result = builder.toString();

    return result.substring(0, result.length() - 3);
  }

  /**
   * Starts a new round by clearing the community cards and updating the dealers and players.
   */
  private void newRound() {
    communityCards.clear();
    players.forEach(Player::continuePlay);
    updateDealer();
    deal();
    blindSetup();
  }

  /**
   * Changes the dealer.
   */
  private void updateDealer() {
    CyclicSet cycle = allPlayerId();
    button = cycle.nextItem(button);

    if (!players.get(button).isPlaying()) {
      updateDealer();
    }
  }

  /**
   * Retrieves the current community cards.
   * @return a copy of the community cards
   */
  public ArrayList<Card> getCommunity() {
    return new ArrayList<>(communityCards);
  }

  /**
   * Retrieves the only human player in the game.
   * @return the human player
   */
  public Player getHuman() {
    return players.get(0);
  }

  /**
   * Determines if a round of betting is complete.
   * @return if the current round of betting is complete
   */
  public boolean roundDone() {
    HashSet<Integer> bets = players.stream().filter(Player::isPlaying).map(Player::getBet)
            .collect(Collectors.toCollection(HashSet::new));

    if (bets.size() == 1) {
      return true;
    }
    else {
      HashSet<Integer> allInBets = players.stream().filter(Player::isPlaying)
              .filter(p -> p.getMoney() == 0).map(Player::getBet)
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
  public void resetRaise() {
    boolean alreadyRaised = players.stream().filter(Player::getRaised)
            .collect(Collectors.toList()).size() > 0;

    if (alreadyRaised) {
      players.forEach(Player::setRaise);
    }
  }
}
