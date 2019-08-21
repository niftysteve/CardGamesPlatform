package game.poker.player.decision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a node in a Monte Carlo Tree.
 */
public class PokerNode {
  private PokerState state;
  private PokerNode parent;
  private List<PokerNode> children;

  /**
   * Constructs a PokerNode given the current state of the game.
   * @param state the current state of the game
   */
  public PokerNode(PokerState state) {
    this.state = state;
    this.parent = null;
    this.children = new ArrayList<>();
  }

  /**
   * Constructs a PokerNode by copying another node.
   * @param other the node to copy
   */
  public PokerNode(PokerNode other) {
    this.state = other.state;
    this.parent = other.parent;
    this.children = other.children;
  }

  /**
   * Retrieves the child that has been visited the most.
   * @return the most visited child node
   */
  public PokerNode getBestChild() {
    return Collections.max(children, Comparator.comparing(node -> node.getState().getVisit()));
  }

  /**
   * Retrieves a random child.
   * @return a random child node
   */
  public PokerNode getRandomChild() {
    int bound = this.children.size();
    int selection = (int) (Math.random() * bound);
    return this.children.get(selection);
  }

  /**
   * Determines the score of this node.
   * @return the score
   */
  public double calculateScore() {
    return state.getWinCount() / (double) state.getVisit();
  }

  /**
   * Retrieves the current state.
   * @return the current state
   */
  public PokerState getState() {
    return state;
  }

  /**
   * Sets the current state.
   * @param state the new state
   */
  public void setState(PokerState state) {
    this.state = state;
  }

  /**
   * Retrieves the parent node.
   * @return the parent node
   */
  public PokerNode getParent() {
    return parent;
  }

  /**
   * Sets the parent node.
   * @param parent the new parent
   */
  public void setParent(PokerNode parent) {
    this.parent = parent;
  }

  /**
   * Retrieves all child nodes.
   * @return a list of child nodes
   */
  public List<PokerNode> getChildren() {
    return children;
  }

  /**
   * Sets the child nodes.
   * @param children the new child nodes
   */
  public void setChildren(List<PokerNode> children) {
    this.children = children;
  }

  /**
   * Adds a child node.
   * @param node the child node to add
   */
  public void addChild(PokerNode node) {
    children.add(node);
  }
}
