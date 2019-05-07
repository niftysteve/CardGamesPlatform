package game.poker.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import game.deck.Card;
import game.deck.Hand;
import game.deck.Suit;
import game.util.CyclicSet;

/**
 * Handles the logic behind finding a Poker hand.
 */
public class CardLogic {
  private Hand hand;
  private HashMap<Suit, Integer> suitCount;
  private TreeMap<Integer, Integer> valueCount;
  private List<Card> focusHand;

  public CardLogic(Hand hand) {
    if (hand.getCards().size() < 1) {
      throw new IllegalArgumentException("Empty hand given");
    }
    this.hand = hand;
    suitCount = hand.allSuits();
    valueCount = hand.allValues();
    focusHand = new ArrayList<>(hand.getCards());
  }

  /**
   * Finds the highest ranked hand.
   * @return an enum of the highest ranked hand
   */
  public HandRank findRank() {
    CyclicSet sequence = makeSequence();
    List<Integer> cutValues = new ArrayList<>(valueCount.values());
    cutValues.remove(Collections.max(cutValues));

    if (sequence.last() == 14 && sequence.previousItem(14) == 13
            && sequence.size() == 5 && topSuitValueMatch(sequence)) {
      return HandRank.Royal_Flush;
    }
    else if (sequence.size() == 5 && topSuitValueMatch(sequence)) {
      return HandRank.Straight_Flush;
    }
    else if (valueCount.containsValue(4)) {
      return HandRank.Four_Kind;
    }
    else if (valueCount.containsValue(3) && targetAtLeast(2, cutValues)) {
      return HandRank.Full_House;
    }
    else if (targetAtLeast(5, suitCount.values())) {
      return HandRank.Flush;
    }
    else if (sequence.size() == 5) {
      return HandRank.Straight;
    }
    else if (valueCount.containsValue(3)) {
      return HandRank.Three_Kind;
    }
    else if (Collections.frequency(valueCount.values(), 2) >= 2) {
      return HandRank.Two_Pair;
    }
    else if (valueCount.containsValue(2)) {
      return HandRank.Pair;
    }
    else {
      return HandRank.High_Card;
    }
  }

  /**
   * Retrieves the exact hand that obtained the highest hand rank.
   * @return the hand with the highest hand rank
   */
  public Hand rankedHand() {
    HandRank rank = findRank();
    switch (rank) {
      case Royal_Flush:
        straightFlushHand();
        break;
      case Straight_Flush:
        straightFlushHand();
        break;
      case Four_Kind:
        topValue(1, 1);
        break;
      case Full_House:
        topValue(2, 0);
        break;
      case Flush:
        flushHand();
        break;
      case Straight:
        straightHand();
        break;
      case Three_Kind:
        topValue(1, 2);
        break;
      case Two_Pair:
        topValue(1, 1);
        break;
      case Pair:
        topValue(1, 3);
        break;
      case High_Card:
        topValue(0, 5);
        break;
    }

    while (focusHand.size() > 5) {
      int lowestVal = Collections.min(focusHand.stream().map(Card::getRank)
              .collect(Collectors.toList()));
      for (int i = 0; i < focusHand.size(); i++) {
        if (focusHand.get(i).getRank() == lowestVal) {
          focusHand.remove(i);
          break;
        }
      }
    }
    return new Hand(focusHand);
  }

  /**
   * Finds the sequence of values of highest size.
   * @return a set of a sequence of values
   */
  private CyclicSet makeSequence() {
    CyclicSet keys = new CyclicSet(valueCount.keySet());

    if (keys.contains(14)) {
      keys.add(1);
    }

    CyclicSet sequenceKeys = keys.stream()
            .filter(k -> keys.contains(k - 1) || keys.contains(k + 1))
            .collect(Collectors.toCollection(CyclicSet::new));

    if (sequenceKeys.size() == 0) {
      return keys;
    }

    ArrayList<HashSet<Integer>> allSequence = new ArrayList<>();
    HashSet<Integer> curSequence = new HashSet<>();
    for (Integer value : sequenceKeys) {
      int next = sequenceKeys.nextItem(value);
      curSequence.add(value);

      if (next != value + 1) {
        allSequence.add(curSequence);
        curSequence = new HashSet<>();
      }
    }

    int maxSize = Collections.max(allSequence.stream()
            .map(HashSet::size).collect(Collectors.toList()));

    allSequence.removeIf(s -> s.size() != maxSize);

    CyclicSet result = new CyclicSet(allSequence.get(0));

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
  private boolean topSuitValueMatch(TreeSet<Integer> values) {
    Suit mostSuit = suitCount.entrySet().stream()
            .filter(set -> set.getValue().equals(Collections.max(suitCount.values())))
            .collect(Collectors.toList()).get(0).getKey();
    ArrayList<Card> validCards = new ArrayList<>(hand.getCards());
    validCards.removeIf(card -> !card.getSuit().equals(mostSuit));

    HashSet<Integer> validValues = validCards.stream().map(Card::getRank)
            .collect(Collectors.toCollection(HashSet::new));

    TreeSet<Integer> copyValues = new TreeSet<>(values);
    copyValues.removeIf(validValues::contains);

    return copyValues.size() == 0;
  }

  /**
   * Retrieves values with the highest quantity.
   * @param count the amount of values to retrieve
   * @return a set of values with the highest quantity
   */
  private HashSet<Integer> highestQuantity(int count) {
    HashSet<Integer> highKeys = new HashSet<>();
    CyclicSet cycle = new CyclicSet(valueCount.values());
    int maxQuantity = Collections.max(cycle);

    for (int i = 0; i < count; i ++) {
      for (Integer value : valueCount.keySet()) {
        if (valueCount.get(value) == maxQuantity) {
          highKeys.add(value);
        }
      }
      maxQuantity = cycle.previousItem(maxQuantity);
    }

    return highKeys;
  }

  /**
   * Removes from the ranked hand cards that do not have the highest quantity or value.
   * @param count the amount of highest quantity cards that should be considered
   * @param extra the amount of highest value cards that should be considered
   */
  private void topValue(int count, int extra) {
    HashSet<Integer> highKeys = highestQuantity(count);
    CyclicSet cycle = new CyclicSet(valueCount.keySet());
    int maxValue = valueCount.lastKey();

    boolean encountered = false;
    for (int i = 0; i < extra; i++)  {
      if (!encountered && highKeys.contains(maxValue)) {
        maxValue = cycle.nextItem(maxValue);
        encountered = true;
        i--;
        continue;
      }
      highKeys.add(maxValue);
      maxValue = cycle.previousItem(maxValue);
      encountered = false;
    }

    focusHand.removeIf(c -> !highKeys.contains(c.getRank()));
  }

  /**
   * Removes from the ranked hand cards whose suit count does not equal at least five.
   */
  private void flushHand() {
    Suit straightSuit = suitCount.entrySet().stream().filter(k -> k.getValue() >= 5)
            .collect(Collectors.toList()).get(0).getKey();

    focusHand.removeIf(s -> !s.getSuit().equals(straightSuit));
  }

  /**
   * Removes from the ranked hand cards not in the highest numerical sequence.
   */
  private void straightHand() {
    focusHand.removeIf(s -> !makeSequence().contains(s.getRank()));
  }

  /**
   * Removes from the ranked hand cards whose suit count does not equal at least five and are not
   * the highest value.
   */
  private void straightFlushHand() {
    flushHand();
    straightHand();
  }

  /**
   * Determines if a suit appears at least a specific number of times
   * @param floor the minimum number of times the suit should appear
   * @return if a suit appeared at least a specific number of times
   */
  private boolean targetAtLeast(int floor, Collection<Integer> target) {

    ArrayList<Integer> copyVals = new ArrayList<>(target);
    copyVals.removeIf(n -> n < floor);

    return copyVals.size() > 0;
  }
}
