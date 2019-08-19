package game.deck;

/**
 * Represents a standard playing card.
 */
public class Card {
  private Suit suit;
  private Rank rank;

  /**
   * Constructs a playing card.
   * @param suit the type of suit
   * @param rank the numeric rank
   */
  public Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

  public Card(Card other) {
    this.suit = other.suit;
    this.rank = other.rank;
  }

  /**
   * Gets the suit of this card.
   * @return the name of this card's suit
   */
  public Suit getSuit() {
    return this.suit;
  }

  /**
   * Gets the named rank of this card.
   * @return the named rank of this card
   */
  public String getNamedValue() {

    switch (rank) {
      case Ace:
        return "Ace";
      case King:
        return "King";
      case Queen:
        return "Queen";
      case Jack:
        return "Jack";
      default:
        return Integer.toString(rank.getValue());
    }
  }

  /**
   * Gets the numeric rank of this card.
   * @return the numeric rank of this card
   */
  public int getRank() {
    return rank.getValue();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    if (suit != card.suit) return false;
    return rank == card.rank;

  }

  @Override
  public int hashCode() {
    int result = suit.hashCode();
    result = 31 * result + rank.hashCode();
    return result;
  }
}
