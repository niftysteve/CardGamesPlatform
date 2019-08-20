package game.poker.player.decision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PokerNode {
  private PokerState state;
  private PokerNode parent;
  private List<PokerNode> children;

  public PokerNode(PokerState state) {
    this.state = state;
    this.parent = null;
    this.children = new ArrayList<>();
  }

  public PokerNode(PokerNode other) {
    this.state = other.state;
    this.parent = other.parent;
    this.children = other.children;
  }

  public PokerNode getBestChild() {
    return Collections.max(children, Comparator.comparingDouble(node -> node.getState().calculateScore()));
  }

  public PokerNode getRandomChild() {
    int possibleMoves = this.children.size();
    int selection = (int) (Math.random() * possibleMoves);
    return this.children.get(selection);
  }

  public PokerState getState() {
    return state;
  }

  public void setState(PokerState state) {
    this.state = state;
  }

  public PokerNode getParent() {
    return parent;
  }

  public void setParent(PokerNode parent) {
    this.parent = parent;
  }

  public List<PokerNode> getChildren() {
    return children;
  }

  public void setChildren(List<PokerNode> children) {
    this.children = children;
  }

  public void addChild(PokerNode node) {
    children.add(node);
  }
}
