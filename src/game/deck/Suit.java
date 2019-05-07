package game.deck;

public enum Suit {
  Clubs("♣"), Diamonds("♦"), Hearts("♥"), Spades("♠");

  private String symbol;

  Suit(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return this.symbol;
  }
}
