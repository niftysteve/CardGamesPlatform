package game.poker.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.deck.Card;
import game.deck.Deck;
import game.deck.Hand;
import game.deck.StandardDeck;
import game.poker.player.decision.PokerNode;
import game.poker.player.decision.PokerState;
import game.poker.rules.FindRank;

public class ComputerBrain {
  private static final int WIN_SCORE = 10;
  private PokerNode root;

  public ComputerBrain(List<Card> hand, List<Card> board, int players) {
    List<Card> exclude = Stream.concat(hand.stream(), board.stream()).collect(Collectors.toList());
    Deck base = new StandardDeck(true, exclude);
    List<Hand> opponentHands = optimalHands(board, base, players);
    opponentHands.forEach(opp -> base.removeKnown(opp.getCards()));

    PokerState initial = new PokerState(hand, board, opponentHands, base);
    this.root = new PokerNode(initial);
  }

  public int calculateBet() {
    int count = 26;
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
      boolean result = simulateRandomPlayout(nodeToExplore);
      // Phase 4 - Update
      backPropagation(nodeToExplore, result);
      count--;
    }

    PokerNode winner = root.getBestChild();

    return 0;
  }

  private double uctValue(int total, int visit, double wins) {
    if (visit == 0) {
      return Integer.MAX_VALUE;
    }

    return ((double) wins / (double) visit) + 1.41 * Math.sqrt(Math.log(total) / (double) visit);
  }


  private PokerNode findBestNode(PokerNode node) {
    int parentVisit = node.getState().getVisit();
    return Collections.max(node.getChildren(),
            Comparator.comparing(child -> uctValue(parentVisit,
                    child.getState().getVisit(), child.getState().getScore())));
  }


  private PokerNode selectPromisingNode(PokerNode root) {
    PokerNode base = root;
    while (base.getChildren().size() != 0) {
      base = findBestNode(base);
    }
    return base;
  }


  private void expandNode(PokerNode node) {
    List<PokerState> states = node.getState().generateStates();
    states.forEach(state -> {
      PokerNode newNode = new PokerNode(state);
      newNode.setParent(node);
      node.getChildren().add(newNode);
    });
  }

  private void backPropagation(PokerNode nodeToExplore, boolean winner) {
    PokerNode tempNode = nodeToExplore;
    while (tempNode != null) {
      tempNode.getState().incrementVisit();
      if (winner) {
        tempNode.getState().addScore(WIN_SCORE);
      }
      tempNode = tempNode.getParent();
    }
  }
  private boolean simulateRandomPlayout(PokerNode node) {
    PokerNode tempNode = new PokerNode(node);
    PokerState tempState = tempNode.getState();
    boolean outcome = false;

    while (tempState.stillPlaying()) {
      tempState.randomPlay();
      outcome = tempState.isWinner();
    }
    return outcome;
  }


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
        Hand option = new Hand(Arrays.asList(first, second));
        optimal.add(option);

        if (optimal.size() > players) {
          optimal.poll();
        }
      }
    }

    return new ArrayList<>(optimal);
  }


}
