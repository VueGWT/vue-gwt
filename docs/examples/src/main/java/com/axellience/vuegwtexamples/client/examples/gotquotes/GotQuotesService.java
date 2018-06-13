package com.axellience.vuegwtexamples.client.examples.gotquotes;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GotQuotesService {

  private Integer lastQuoteIndex;

  @Inject
  public GotQuotesService() {
  }

  public GotQuote getRandomQuote() {
    int randomIndex = (int) Math.floor(Math.random() * GOT_QUOTES.length);
    if (lastQuoteIndex != null && randomIndex == lastQuoteIndex) {
      randomIndex = (randomIndex + 1) % GOT_QUOTES.length;
    }

    lastQuoteIndex = randomIndex;
    return GOT_QUOTES[randomIndex];
  }

  private static GotQuote[] GOT_QUOTES = {
      new GotQuote("The next time you raise a hand to me will be the last time you have hands.",
          "Daenerys Targaryen", 1, 4),
      new GotQuote(
          "You love your children. It’s your one redeeming quality. That and your cheekbones.",
          "Tyrion Lannister", 2, 1),
      new GotQuote(
          "Power resides where men believe it resides. It’s a trick. A shadow on the wall. And a very small man can cast a very large shadow.",
          "Varys", 2, 3),
      new GotQuote("Chaos isn’t a pit. Chaos is a ladder.", "Petyr Baelish", 3, 6),
      new GotQuote("The Lannisters send their regards.", "Roose Bolton", 3, 9),
      new GotQuote(
          "The world is overflowing with horrible things, but they’re all a tray of cakes next to death.",
          "Olenna Tyrell", 4, 3),
      new GotQuote("It is rare to meet a Lannister who shares my enthusiasm for dead Lannisters.",
          "Oberyn Tyrell", 4, 7),
      new GotQuote("You know nothing, Jon Snow.", "Ygritte", 4, 9),
      new GotQuote("The good lords are dead, and the rest are monsters.", "Brienne of Tarth", 5, 1),
      new GotQuote("I’m not a politician, I’m a queen.", "Daenerys Targaryen", 5, 2),
      new GotQuote(
          "It’s easy to confuse what is with what ought to be, especially when what is has worked out in your favor.",
          "Tyrion Lannister", 5, 9),
      new GotQuote(
          "A wise man once said a true history of the world is a history of great conversations in elegant rooms.",
          "Tyrion Lannister", 6, 3),
      new GotQuote("I wonder if you’re the worst person I’ve ever met.", "Olenna Tyrell", 6, 7),
      new GotQuote(
          "No need to seize the last word, Lord Baelish. I’ll assume it was something clever.",
          "Sansa Stark", 7, 1),
      new GotQuote(
          "I may be small. I may be a girl, but I won’t be knitting by the fire while I have men fight for me.",
          "Lyanna Mormont", 7, 1),
      new GotQuote("Leave one wolf alive and the sheep are never safe.", "Aria Stark", 7, 1)
  };
}
