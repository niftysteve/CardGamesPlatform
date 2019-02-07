package game.deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Represents a standard deck of playing cards.
 */
public class StandardDeck implements Deck {

  private ArrayList<Card> cards = new ArrayList<>();
  private ArrayList<Card> streamCards;
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
    ArrayList<Integer> values = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
            12, 13, 14));
    ArrayList<String> suits = new ArrayList<>(Arrays.asList("Clubs", "Hearts", "Spades", "Diamonds"));
    //CLUBS("♣"), DIAMONDS("♦"), HEARTS("♥"), SPADES("♠");
    for (String shape : suits) {
      for (Integer value : values) {
        Card card = new Card(shape, value);
        cards.add(card);
      }
    }
    shuffle();
  }

  @Override
  public void shuffle() {
    streamCards = new ArrayList<>();
    streamCards.addAll(cards);
    Collections.shuffle(streamCards, rand);
  }

  @Override
  public ArrayList<Hand> dealCards(int players, int amount) {

    if (players < 1) {
      throw new IllegalArgumentException("No players to deal");
    }

    if (players * amount > cards.size()) {
      throw new IllegalArgumentException("Too many players and cards to deal");
    }

    ArrayList<Hand> allHands = new ArrayList<>();

    for (int i = 0; i < players; i++) {
      allHands.add(new Hand());
    }

    for (int j = 0; j < amount; j++) {
      for (Hand curHand : allHands) {
        int index = rand.nextInt(streamCards.size());
        Card card = streamCards.get(index);
        curHand.addCard(card);
        streamCards.remove(index);
      }
    }

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
