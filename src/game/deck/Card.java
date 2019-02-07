package game.deck;

/**
 * Represents a standard playing card.
 */
public class Card {
  private String suit;
  private int value;

  /**
   * Constructs a playing card.
   * @param suit the type of suit
   * @param value the numeric value
   */
  public Card(String suit, int value) {
    this.suit = suit;
    this.value = value;
  }

  /**
   * Gets the suit of this card.
   * @return the name of this card's suit
   */
  public String getSuit() {
    return this.suit;
  }

  /**
   * Gets the named value of this card.
   * @return the named value of this card
   */
  public String getNamedValue() {

    switch (value) {
      case 14:
        return "Ace";
      case 13:
        return "King";
      case 12:
        return "Queen";
      case 11:
        return "Jack";
      default:
        return Integer.toString(this.value);
    }
  }

  /**
   * Gets the numeric value of this card.
   * @return the numeric value of this card
   */
  public int getValue() {
    return this.value;
  }
}
