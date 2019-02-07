import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import game.util.CyclicSet;

import static junit.framework.TestCase.assertEquals;

public class CycleTest {
  private CyclicSet linearCycle;

  @Before
  public void init() {
    linearCycle = new CyclicSet(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  }

  @Test
  public void testNextItem() {
    int nextOne = linearCycle.nextItem(8);
    assertEquals(9, nextOne);

    int loopedItem = linearCycle.nextItem(10);
    assertEquals(0, loopedItem);
  }

  @Test
  public void testPrevItem() {
    int nextOne = linearCycle.previousItem(5);
    assertEquals(4, nextOne);

    int loopedItem = linearCycle.previousItem(0);
    assertEquals(10, loopedItem);
  }

  @Test
  public void testFromStart() {
    int zeroFrom = linearCycle.fromStart(1, 0);
    assertEquals(1 , zeroFrom);

    int firstFrom = linearCycle.fromStart(5, 3);
    assertEquals(8, firstFrom);

    int loopedItem = linearCycle.fromStart(9, 5);
    assertEquals(3, loopedItem);
  }

  @Test
  public void testFromEnd() {
    int zeroFrom = linearCycle.fromEnd(1, 0);
    assertEquals(1 , zeroFrom);

    int firstFrom = linearCycle.fromEnd(5, 3);
    assertEquals(2, firstFrom);

    int loopedItem = linearCycle.fromEnd(2, 5);
    assertEquals(8, loopedItem);
  }
}
