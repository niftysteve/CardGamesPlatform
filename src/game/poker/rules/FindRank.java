package game.poker.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.deck.Card;
import game.deck.Hand;
import game.deck.Suit;
import game.util.CyclicSet;

/**
 * Facilitates hand rank determination.
 */
public class FindRank {
  private HandOperations logic;
  private Hand hand;
  private RankLogic rank;

  public FindRank() {
    this.hand = null;
    this.logic = null;
    this.rank = null;
  }
  /**
   * Constructs a FindRank object.
   * @param hand the poker hand
   */
  public FindRank(Hand hand) {
    setHand(hand);
  }

  public void setHand(Hand hand) {
    this.hand = hand;
    this.logic = new HandOperations(hand);
    this.rank = logicResult();
  }

  public HandRank getRank(Hand base, List<Card> board) {
    List<Card> allCards = Stream.concat(base.getCards().stream(), board.stream()).collect(Collectors.toList());
    Hand current = new Hand(allCards);

    this.hand = current;
    this.logic = new HandOperations(current);

    RankLogic rank = logicResult();

    this.hand = null;
    this.logic = null;

    return rank.getRank();
  }

  /**
   * Determines the highest rank of the hand.
   * @return the highest rank of the hand
   */
  private RankLogic logicResult() {
    return getSuite().stream()
            .filter(RankLogic::validRank)
            .max(Comparator.comparing(logic -> logic.getRank().getValue())).orElse(null);
  }

  /**
   * Retrieves the hand rank itself.
   * @return the hand rank
   */
  public HandRank getRank() {
    return rank.getRank();
  }

  /**
   * Retrieves the actual contents of the ranked hand.
   * @return the optimal card hand given the initial cards
   */
  public Hand getRankedHand() {
    return rank.findRankedHand();
  }

  /**
   * Collects all rank finding processes for easy processing.
   * @return
   */
  private List<RankLogic> getSuite() {
   List<RankLogic> suite = new ArrayList<>();
   suite.add(new RoyalFlush());
   suite.add(new StraightFlush());
   suite.add(new FourKind());
   suite.add(new FullHouse());
   suite.add(new Flush());
   suite.add(new Straight());
   suite.add(new ThreeKind());
   suite.add(new TwoPair());
   suite.add(new Pair());
   suite.add(new HighCard());

   return suite;
  }

  /**
   * Handles process for finding a Royal Flush.
   */
  private class RoyalFlush implements RankLogic {
    CyclicSet sequence = logic.makeSequence();

    @Override
    public boolean validRank() {
      return sequence.size() == 5
              && sequence.last() == 14
              && sequence.previousFrom(14) == 13
              && logic.topSuitValueMatch(sequence);
    }

    @Override
    public Hand findRankedHand() {
      return logic.extractSameSuit(sequence);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Royal_Flush;
    }
  }

  /**
   * Handles process for finding a Straight Flush.
   */
  private class StraightFlush implements RankLogic {
    CyclicSet sequence = logic.makeSequence();

    @Override
    public boolean validRank() {
      return sequence.size() == 5 && logic.topSuitValueMatch(sequence);
    }

    @Override
    public Hand findRankedHand() {
      return logic.extractSameSuit(sequence);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Straight_Flush;
    }
  }

  /**
   * Handles process for finding a Four-of-a-kind.
   */
  private class FourKind implements RankLogic {

    @Override
    public boolean validRank() {
      return hand.allValues().containsValue(4);
    }

    @Override
    public Hand findRankedHand() {
      return logic.getHandByCount(1);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Four_Kind;
    }
  }

  /**
   * Handles process for finding a Full House.
   */
  private class FullHouse implements RankLogic {

    @Override
    public boolean validRank() {
      return hand.allValues().containsValue(3)
              && hand.allValues().values().stream().filter(count -> count >= 2).count() > 1;
    }

    @Override
    public Hand findRankedHand() {
      return logic.getHandByCount(2);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Full_House;
    }
  }

  /**
   * Handles process for finding a Flush.
   */
  private class Flush implements RankLogic {

    @Override
    public boolean validRank() {
      return hand.allSuits().values().stream().anyMatch(count -> count >= 5);
    }

    @Override
    public Hand findRankedHand() {
      List<Suit> straightSuits = hand.allSuits().entrySet().stream()
              .filter(entry -> entry.getValue() >= 5)
              .map(Map.Entry::getKey)
              .collect(Collectors.toList());

      Suit bestSuit = hand.getCards().stream()
              .filter(card -> straightSuits.contains(card.getSuit()))
              .max(Comparator.comparingInt(Card::getRank))
              .orElseThrow(() -> new IllegalArgumentException("Invalid hand"))
              .getSuit();

      return new Hand(hand.getCards().stream()
              .filter(card -> card.getSuit() == bestSuit)
              .collect(Collectors.toList()).subList(0, 5));
    }

    @Override
    public HandRank getRank() {
      return HandRank.Flush;
    }
  }

  /**
   * Handles process for finding a Straight.
   */
  private class Straight implements RankLogic {
    CyclicSet sequence = logic.makeSequence();

    @Override
    public boolean validRank() {
      return sequence.size() == 5;
    }

    @Override
    public Hand findRankedHand() {
      return logic.getSequenceHand(sequence);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Straight;
    }
  }

  /**
   * Handles process for finding a Three-of-a-kind.
   */
  private class ThreeKind implements RankLogic {

    @Override
    public boolean validRank() {
      return hand.allValues().containsValue(3);
    }

    @Override
    public Hand findRankedHand() {
      return logic.getHandByCount(1);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Three_Kind;
    }
  }

  /**
   * Handles process for finding a Two Pair.
   */
  private class TwoPair implements RankLogic {

    @Override
    public boolean validRank() {
      return Collections.frequency(hand.allValues().values(), 2) >= 2;
    }

    @Override
    public Hand findRankedHand() {
      return logic.getHandByCount(2);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Two_Pair;
    }
  }

  /**
   * Handles process for finding a Pair.
   */
  private class Pair implements RankLogic {

    @Override
    public boolean validRank() {
      return hand.allValues().containsValue(2);
    }

    @Override
    public Hand findRankedHand() {
      return logic.getHandByCount(1);
    }

    @Override
    public HandRank getRank() {
      return HandRank.Pair;
    }
  }

  /**
   * Handles process for finding a High Card.
   */
  private class HighCard implements RankLogic {

    @Override
    public boolean validRank() {
      return true;
    }

    @Override
    public Hand findRankedHand() {
      return logic.getHandByCount(5);
    }

    @Override
    public HandRank getRank() {
      return HandRank.High_Card;
    }
  }
}
