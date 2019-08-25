package game.poker.rules;

/**
 * Represents all possible Poker hand ranks.
 */
public enum HandRank {
  Royal_Flush(10), Straight_Flush(9), Four_Kind(8), Full_House(7), Flush(6),
  Straight(5), Three_Kind(4), Two_Pair(3), Pair(2), High_Card(1);

  private int value;

  /**
   * Constructs a HandRank with a numeric value assigned to it.
   * @param value the numeric value of the HandRank
   */
  HandRank(int value) {
    this.value = value;
  }

  /**
   * Retrieves the numeric value of the HandRank.
   * @return the numeric value of the HandRank
   */
  public int getValue() {
    return value;
  }


}
