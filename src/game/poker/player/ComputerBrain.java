package game.poker.player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.StandardDeck;
import game.poker.player.decision.PokerNode;
import game.poker.player.decision.PokerState;
import game.poker.player.strategy.*;
import game.poker.rules.FindRank;

/**
 * Decides an amount to bet by utilizing Monte Carlo Tree Search.
 */
public class ComputerBrain {
  private static final int SAMPLES = 1000;
  private PokerNode root;
  private int currentBet;
  private Random rand;

  public ComputerBrain(List<Card> hand, List<Card> board, int players, int currentBet, int seed) {
    this(hand, board, players, currentBet);
    this.rand = new Random(seed);
  }

  /**
   * Constructs a ComputerBrain object given the current state of the game.
   * @param hand the hand of the computer
   * @param board the community cards
   * @param players the number of players in the game, excluding this one
   * @param currentBet the bet currently on the line
   */
  public ComputerBrain(List<Card> hand, List<Card> board, int players, int currentBet) {
    if (board.size() == 0) {
      this.root = null;
      this.currentBet = currentBet;
    }
    else {
      List<Card> exclude = Stream.concat(hand.stream(), board.stream()).collect(Collectors.toList());
      Deck base = new StandardDeck(exclude);
      List<Hand> opponentHands = optimalHands(board, base, players);
      opponentHands.forEach(opp -> base.removeKnown(opp.getCards()));

      PokerState initial = new PokerState(hand, board, opponentHands, base);
      this.root = new PokerNode(initial);
      this.currentBet = currentBet;
    }
    this.rand = new Random();
  }

  /**
   * Calculates a bet via Monte Carlo Tree Search.
   * @return an appropriate amount to bet
   */
  public int calculateBet() {
    if (root == null) {
      return currentBet;
    }

    int count = SAMPLES;
    while (count > 0) {
      // Phase 1 - Selection
      PokerNode promisingNode = selectPromisingNode(root);

      // Phase 2 - Expansion
      if (promisingNode.getState().stillPlaying()) {
        expandNode(promisingNode);
      }

      // Phase 3 - Simulation
      PokerNode nodeToExplore = promisingNode;
      if (promisingNode.getChildren().size() > 0) {
        nodeToExplore = promisingNode.getRandomChild();
      }

      double result = simulateRandomPlayout(nodeToExplore);
      // Phase 4 - Update
      backPropagation(nodeToExplore, result);
      count--;
    }

    PokerNode winner = root.getBestChild();
    double score = winner.calculateScore();

    System.out.println(score);

    if (score <= 0.3) {
      score = rand.nextDouble();
    }

    BetStrategy strategy;
    if (score > 0.7) {
      strategy = new HighAggroStrat();
    }
    else if (score > 0.5) {
      strategy = new MidAggroStrat();
    }
    else if (score > 0.3) {
      strategy = new LowAggroStrat();
    }
    else {
      strategy = new CheckStrat();
    }

    return strategy.calcBet(currentBet);
  }

  /**
   * Calculates the Upper Confidence Bound 1 Applied to Trees of a node.
   * @param totalVisits the total amount of times nodes in this branch has been visited
   * @param currentVisits the amount of times this node has been visited
   * @param currentWins the amount of times this node has won
   * @return the UCT value of a node
   */
  private double uctValue(int totalVisits, int currentVisits, double currentWins) {
    if (currentVisits == 0) {
      return Integer.MAX_VALUE;
    }

    return (currentWins / (double) currentVisits) + 1.41 * Math.sqrt(Math.log(totalVisits) / (double) currentVisits);
  }

  /**
   * Selects the best child node based on their UCT value
   * @param root
   * @return
   */
  private PokerNode selectPromisingNode(PokerNode root) {
    PokerNode base = root;
    while (base.getChildren().size() != 0) {
      int parentVisits = base.getState().getVisit();
      base = Collections.max(base.getChildren(), Comparator.comparing(child ->
              uctValue(parentVisits, child.getState().getVisit(), child.getState().getWinCount())));
    }
    return base;
  }

  /**
   * Expands a node by generating all of its possible children.
   * @param node the node to expand
   */
  private void expandNode(PokerNode node) {
    List<PokerState> states = node.getState().generateStates();
    states.forEach(state -> {
      PokerNode newNode = new PokerNode(state);
      newNode.setParent(node);
      node.addChild(newNode);
    });
  }

  /**
   * Propagates the results of a playout back to the root.
   * @param leaf the leaf from which to start the back propagation
   * @param winnings the win factor
   */
  private void backPropagation(PokerNode leaf, double winnings) {
    PokerNode tempNode = leaf;
    while (tempNode != null) {
      tempNode.getState().incrementVisit();
      tempNode.getState().incrementWins(winnings);
      tempNode = tempNode.getParent();
    }
  }

  /**
   * Simulates playing a game to the end.
   * @param node the beginning state of the game
   * @return the result of the playout
   */
  private double simulateRandomPlayout(PokerNode node) {
    PokerNode tempNode = new PokerNode(node);
    PokerState tempState = tempNode.getState();

    while (tempState.stillPlaying()) {
      tempState.randomPlay();
    }

    return tempState.winFactor();
  }

  /**
   * Determines the best possible hand each opponent could have.
   * @param board the current community cards
   * @param deck the current deck
   * @param players the total amount of players, excluding this one
   * @return a list of best possible hands
   */
  private List<Hand> optimalHands(List<Card> board, Deck deck, int players) {
    FindRank logic = new FindRank();
    List<Card> source = deck.allCards();
    PriorityQueue<Hand> optimal = new PriorityQueue<>(players,
            Comparator.comparing(hand -> logic.getRank(hand, board).getValue()));

    for (int i = 0; i < source.size(); i++) {
      Card first = source.get(i);

      for (int j = 0; j < source.size(); j++) {
        if (i == j) {
          continue;
        }

        Card second = source.get(j);
        Hand option = new Hand(first, second);
        optimal.add(option);

        while (optimal.size() > players) {
          optimal.poll();
        }
      }
    }

    return new ArrayList<>(optimal);
  }
}
