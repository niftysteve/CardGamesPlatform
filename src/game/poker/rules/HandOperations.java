package game.poker.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import game.deck.Card;
import game.deck.Hand;
import game.deck.Suit;
import game.util.CyclicSet;

/**
 * Handles poker-specific processes for a hand.
 */
public class HandOperations {
  private Hand hand;

  /**
   * Constructs a HandOperations object.
   * @param hand the poker hand
   */
  public HandOperations(Hand hand) {
    if (hand.getCards().size() < 1) {
      throw new IllegalArgumentException("Empty hand given");
    }
    this.hand = new Hand(hand);
  }

  /**
   * Finds the sequence of values of highest size.
   * @return a set of a sequence of values
   */
  public CyclicSet makeSequence() {
    CyclicSet keys = new CyclicSet(hand.allValues().keySet());

    if (keys.contains(14)) {
      keys.add(1);
    }

    CyclicSet sequenceKeys = keys.stream()
            .filter(k -> keys.contains(k - 1) || keys.contains(k + 1))
            .collect(Collectors.toCollection(CyclicSet::new));

    List<CyclicSet> allSequence = new ArrayList<>();

    CyclicSet curSequence = new CyclicSet();
    for (Integer value : sequenceKeys) {
      int next = sequenceKeys.nextItem(value);
      curSequence.add(value);

      if (next != value + 1) {
        allSequence.add(curSequence);
        curSequence = new CyclicSet();
      }
    }

    if (allSequence.size() == 0) {
      return new CyclicSet();
    }

    allSequence.sort(Comparator.comparing(CyclicSet::size));

    CyclicSet result = new CyclicSet(allSequence.get(allSequence.size() - 1));

    while (result.size() > 5) {
      result.remove(result.first());
    }

    if (result.contains(1)) {
      result.remove(1);
      result.add(14);
    }

    return result;
  }

  /**
   * Determines if the sequence consists entirely of the same suit.
   * @param values the target sequence
   * @return if the sequence consists entirely of the same suit.
   */
  public boolean topSuitValueMatch(TreeSet<Integer> values) {
    Hand topHand = new Hand(getSequenceHand(values));

    Map.Entry<Suit, Integer> topSuit = Collections.max(topHand.allSuits().entrySet(),
            Comparator.comparingInt(Map.Entry::getValue));

    return topSuit.getValue() == values.size();
  }

  /**
   * Extracts cards with ranks found in the given sequence.
   * @param values the sequence values
   * @return the hand comprised of a sequence
   */
  public Hand getSequenceHand(TreeSet<Integer> values) {
    List<Card> cards = hand.getCards().stream()
            .filter(card -> values.contains(card.getRank()))
            .collect(Collectors.toCollection(ArrayList::new));

    return new Hand(cards);
  }

  /**
   * Extracts all cards in a sequence that share the same suit.
   * @param values the sequence values
   * @return a hand comprised of a sequence with all cards of the same suit
   */
  public Hand extractSameSuit(TreeSet<Integer> values) {
    Hand sequenceHand = getSequenceHand(values);
    Suit matchSuit = Collections.max(sequenceHand.allSuits().entrySet(),
            Comparator.comparingInt(Map.Entry::getValue)).getKey();

    sequenceHand.getCards().removeIf(card -> card.getSuit() != matchSuit);

    return sequenceHand;
  }

  /**
   * Extracts cards that appear the most.
   * @param count the amount of rank groups to take from
   * @return a hand comprised of the most apparent cards
   */
  public Hand getHandByCount(int count) {
    List<Integer> highRanks = hand.allValues().entrySet().stream()
            .sorted(Collections.reverseOrder(Comparator.<Map.Entry<Integer, Integer>>comparingInt(Map.Entry::getValue)
                    .thenComparingInt(Map.Entry::getKey)))
            .map(Map.Entry::getKey)
            .collect(Collectors.toCollection(ArrayList::new)).subList(0, count);

    LinkedList<Card> cards = hand.getCards().stream()
            .filter(card -> highRanks.contains(card.getRank()))
            .sorted(Comparator.comparingInt(Card::getRank))
            .collect(Collectors.toCollection(LinkedList::new));

    if (cards.size() < 5) {
      fillWithHighestRank(cards, highRanks);
    }

    while (cards.size() > 5) {
      cards.poll();
    }

    return new Hand(cards);
  }

  /**
   * Adds cards with the highest ranks to the hand.
   * @param cards the hand of cards
   * @param highRanks the ranks to ignore
   */
  private void fillWithHighestRank(List<Card> cards, List<Integer> highRanks) {
    LinkedList<Card> order = hand.getCards().stream()
            .filter(card -> !highRanks.contains(card.getRank()))
            .sorted(Comparator.comparingInt(Card::getRank))
            .collect(Collectors.toCollection(LinkedList::new));

    while (order.peek() != null && cards.size() < 5) {
      cards.add(order.pollLast());
    }
  }
}
