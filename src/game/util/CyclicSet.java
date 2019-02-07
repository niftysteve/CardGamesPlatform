package game.util;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 * Represents a set that loops end-to-end.
 */
public class CyclicSet extends TreeSet<Integer> {

  /**
   * Constructs an empty CyclicSet.
   */
  public CyclicSet() {
    super();
  }

  /**
   * Constructs a pre-filled CyclicSet.
   * @param c the preliminary values to fill the set
   */
  public CyclicSet(Collection<Integer> c) {
    super(c);
  }

  /**
   * Retrieves the next item in the set. Loops back to the start if starting from the end.
   * @param start the position in the set to start from
   * @return the next item in the set
   */
  public Integer nextItem(int start) {

    if (higher(start) != null) {
      return higher(start);
    }
    else {
     return first();
    }
  }

  /**
   * Retrieves the previous item in the set. Loops back to the end if starting from the start.
   * @param start the position in the set to start from
   * @return the previous item in the set
   */
  public Integer previousItem(int start) {

    if (lower(start) != null) {
      return lower(start);
    }
    else {
      return last();
    }
  }

  /**
   * Retrieves the item located a given distance after a starting value.
   * @param start the position in the set to start from
   * @param distance the distance after the starting value
   * @return the item in the set a given distance after a starting value
   */
  public Integer fromStart(int start, int distance) {
    int[] result = new int[]{start};
    IntStream.range(0, distance).forEach(n -> result[0] = nextItem(result[0]));
    return result[0];
  }

  /**
   * Retrieves the item located a given distance before a starting value.
   * @param start the position in the set to start from
   * @param distance the distance before the starting value
   * @return the item in the set a given distance before a starting value
   */
  public Integer fromEnd(int start, int distance) {
    int[] result = new int[]{start};
    IntStream.range(0, distance).forEach(n -> result[0] = previousItem(result[0]));
    return result[0];
  }
}
