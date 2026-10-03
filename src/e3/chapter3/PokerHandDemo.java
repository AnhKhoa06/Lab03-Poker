package e3.chapter3;

import static e3.chapter3.Rank.*;
import static e3.chapter3.Suit.*;

import java.util.Comparator;

/** Small driver program to test the poker hand classification. */
public class PokerHandDemo {

	private static Hand hand(Card... pCards) {
		Hand hand = new Hand(5);
		for (Card card : pCards) {
			hand.add(card);
		}
		return hand;
	}

	private static Card c(Rank pRank, Suit pSuit) {
		return new Card(pRank, pSuit);
	}

	private static void check(String pName, Hand pHand, PokerHandType pExpected) {
		PokerHandType actual = pHand.getPokerHandType();
		String result = actual == pExpected ? "OK  " : "FAIL";
		System.out.println(result + " " + pName + ": expected " + pExpected + ", got " + actual);
	}

	public static void main(String[] args) {
		check("High card", hand(c(ACE, CLUBS), c(THREE, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.HIGH_CARD);
		check("One pair", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.ONE_PAIR);
		check("Two pair", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(FIVE, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.TWO_PAIR);
		check("Three of a kind", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.THREE_OF_A_KIND);
		check("Straight (ace-low)", hand(c(ACE, CLUBS), c(TWO, HEARTS), c(THREE, SPADES), c(FOUR, DIAMONDS), c(FIVE, CLUBS)),
				PokerHandType.STRAIGHT);
		check("Straight (9 to K)", hand(c(NINE, CLUBS), c(TEN, HEARTS), c(JACK, SPADES), c(QUEEN, DIAMONDS), c(KING, CLUBS)),
				PokerHandType.STRAIGHT);
		check("Ace-high is NOT a straight (simplification)",
				hand(c(TEN, CLUBS), c(JACK, HEARTS), c(QUEEN, SPADES), c(KING, DIAMONDS), c(ACE, CLUBS)),
				PokerHandType.HIGH_CARD);
		check("Flush", hand(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(KING, HEARTS)),
				PokerHandType.FLUSH);
		check("Full house", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(FIVE, DIAMONDS), c(FIVE, CLUBS)),
				PokerHandType.FULL_HOUSE);
		check("Four of a kind", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(TWO, DIAMONDS), c(FIVE, CLUBS)),
				PokerHandType.FOUR_OF_A_KIND);
		check("Straight flush", hand(c(ACE, SPADES), c(TWO, SPADES), c(THREE, SPADES), c(FOUR, SPADES), c(FIVE, SPADES)),
				PokerHandType.STRAIGHT_FLUSH);

		// Comparison by hand type only
		Comparator<Hand> comparator = Hand.createByPokerHandTypeComparator();
		Hand pair = hand(c(TWO, CLUBS), c(TWO, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS));
		Hand flush = hand(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(KING, HEARTS));
		Hand threeLow = hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS));
		Hand threeHigh = hand(c(KING, CLUBS), c(KING, HEARTS), c(KING, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS));
		System.out.println("pair vs flush (expect < 0): " + comparator.compare(pair, flush));
		System.out.println("flush vs pair (expect > 0): " + comparator.compare(flush, pair));
		System.out.println("three 2s vs three Ks (expect 0): " + comparator.compare(threeLow, threeHigh));
	}
}