package game.deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Represents a standard deck of playing cards.
 */
public class StandardDeck implements Deck {

  private List<Card> cards = new ArrayList<>();
  private List<Card> streamCards;
  private Random rand;

  /**
   * Constructs a randomly ordered deck.
   */
  public StandardDeck() {
    this.rand = new Random();
    createDeck();
  }

  /**
   * Constructs a deck with a random seed.
   * @param seed the desired random seed
   */
  public StandardDeck(int seed) {
    this.rand = new Random(seed);
    createDeck();
  }

  /**
   * Creates the deck of cards and shuffles it.
   */
  private void createDeck() {
    List<Rank> values = new ArrayList<>(Arrays.asList(Rank.values()));
    List<Suit> suits = new ArrayList<>(Arrays.asList(Suit.values()));
    values.forEach(val -> suits.forEach(shape -> cards.add(new Card(shape, val))));
    shuffle();
  }

  @Override
  public void shuffle() {
    streamCards = new ArrayList<>();
    streamCards.addAll(cards);
    Collections.shuffle(streamCards, rand);
  }

  @Override
  public List<Hand> dealCards(int players, int amount) {

    if (players < 1) {
      throw new IllegalArgumentException("No players to deal");
    }

    if (players * amount > cards.size()) {
      throw new IllegalArgumentException("Too many players and cards to deal");
    }

    List<Hand> allHands = new ArrayList<>();

    IntStream.range(0, players).forEach(n -> allHands.add(new Hand()));

    IntStream.range(0, amount).forEach(n -> allHands.forEach(hand -> {
      int index = rand.nextInt(streamCards.size());
      Card card = streamCards.get(index);
      hand.addCard(card);
      streamCards.remove(index);
    }));

    return allHands;
  }

  @Override
  public Card drawCard() {
    if (streamCards.size() < 1) {
      throw new IllegalArgumentException("No more cards to draw");
    }

    return streamCards.remove(0);
  }

  @Override
  public void burnCards(int amount) {
    if (streamCards.size() < 1) {
      throw new IllegalArgumentException("No more cards to burn");
    }

    IntStream.range(0, amount).forEach(n -> streamCards.remove(0));
  }

  @Override
  public int remainingCards() {
    return streamCards.size();
  }
}
