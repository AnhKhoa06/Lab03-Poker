package e3.chapter3;

import static e3.chapter3.Rank.*;
import static e3.chapter3.Suit.*;

import java.util.Comparator;

/** Small driver program to test the poker hand classification and comparison. */
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

	private static void checkType(String pName, Hand pHand, PokerHandType pExpected) {
		PokerHandType actual = pHand.getPokerHandType();
		String result = actual == pExpected ? "OK  " : "FAIL";
		System.out.println(result + " " + pName + ": expected " + pExpected + ", got " + actual);
	}

	/** pExpectedSign: -1 if pHand1 < pHand2, 0 if equal, 1 if pHand1 > pHand2. */
	private static void checkCompare(String pName, Comparator<Hand> pComparator, Hand pHand1, Hand pHand2,
			int pExpectedSign) {
		int actual = Integer.signum(pComparator.compare(pHand1, pHand2));
		String result = actual == pExpectedSign ? "OK  " : "FAIL";
		System.out.println(result + " " + pName + ": expected " + pExpectedSign + ", got " + actual);
	}

	public static void main(String[] args) {
		System.out.println("--- Poker hand types ---");
		checkType("High card", hand(c(ACE, CLUBS), c(THREE, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.HIGH_CARD);
		checkType("One pair", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.ONE_PAIR);
		checkType("Two pair", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(FIVE, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.TWO_PAIR);
		checkType("Three of a kind",
				hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS)),
				PokerHandType.THREE_OF_A_KIND);
		checkType("Straight (ace-low)",
				hand(c(ACE, CLUBS), c(TWO, HEARTS), c(THREE, SPADES), c(FOUR, DIAMONDS), c(FIVE, CLUBS)),
				PokerHandType.STRAIGHT);
		checkType("Straight (9 to K)",
				hand(c(NINE, CLUBS), c(TEN, HEARTS), c(JACK, SPADES), c(QUEEN, DIAMONDS), c(KING, CLUBS)),
				PokerHandType.STRAIGHT);
		checkType("Straight (ace-high)",
				hand(c(TEN, CLUBS), c(JACK, HEARTS), c(QUEEN, SPADES), c(KING, DIAMONDS), c(ACE, CLUBS)),
				PokerHandType.STRAIGHT);
		checkType("Not a straight (Q-K-A-2-3 does not wrap around)",
				hand(c(QUEEN, CLUBS), c(KING, HEARTS), c(ACE, SPADES), c(TWO, DIAMONDS), c(THREE, CLUBS)),
				PokerHandType.HIGH_CARD);
		checkType("Flush", hand(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(KING, HEARTS)),
				PokerHandType.FLUSH);
		checkType("Full house", hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(FIVE, DIAMONDS), c(FIVE, CLUBS)),
				PokerHandType.FULL_HOUSE);
		checkType("Four of a kind",
				hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(TWO, DIAMONDS), c(FIVE, CLUBS)),
				PokerHandType.FOUR_OF_A_KIND);
		checkType("Straight flush (ace-low)",
				hand(c(ACE, SPADES), c(TWO, SPADES), c(THREE, SPADES), c(FOUR, SPADES), c(FIVE, SPADES)),
				PokerHandType.STRAIGHT_FLUSH);
		checkType("Straight flush (ace-high, royal flush)",
				hand(c(TEN, SPADES), c(JACK, SPADES), c(QUEEN, SPADES), c(KING, SPADES), c(ACE, SPADES)),
				PokerHandType.STRAIGHT_FLUSH);

		System.out.println("--- Comparison by hand type only ---");
		Comparator<Hand> byType = Hand.createByPokerHandTypeComparator();
		Hand pair = hand(c(TWO, CLUBS), c(TWO, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS));
		Hand flush = hand(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(KING, HEARTS));
		Hand threeLow = hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS));
		Hand threeHigh = hand(c(KING, CLUBS), c(KING, HEARTS), c(KING, SPADES), c(FIVE, DIAMONDS), c(NINE, CLUBS));
		checkCompare("pair < flush", byType, pair, flush, -1);
		checkCompare("flush > pair", byType, flush, pair, 1);
		checkCompare("three 2s = three Ks (type only)", byType, threeLow, threeHigh, 0);

		System.out.println("--- Comparison by full strength (type, then card values) ---");
		Comparator<Hand> byStrength = Hand.createByStrengthComparator();
		checkCompare("type still decides first: pair of Aces < two pair 3s and 2s", byStrength,
				hand(c(ACE, CLUBS), c(ACE, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS)),
				hand(c(THREE, CLUBS), c(THREE, HEARTS), c(TWO, SPADES), c(TWO, DIAMONDS), c(NINE, SPADES)), -1);
		checkCompare("three Ks > three 2s", byStrength, threeHigh, threeLow, 1);
		checkCompare("full house KKK55 > 222AA", byStrength,
				hand(c(KING, CLUBS), c(KING, HEARTS), c(KING, SPADES), c(FIVE, DIAMONDS), c(FIVE, CLUBS)),
				hand(c(TWO, CLUBS), c(TWO, HEARTS), c(TWO, SPADES), c(ACE, DIAMONDS), c(ACE, CLUBS)), 1);
		checkCompare("two pair KK33 with Ace kicker > KK33 with Queen kicker", byStrength,
				hand(c(KING, CLUBS), c(KING, HEARTS), c(THREE, SPADES), c(THREE, DIAMONDS), c(ACE, CLUBS)),
				hand(c(KING, DIAMONDS), c(KING, SPADES), c(THREE, CLUBS), c(THREE, HEARTS), c(QUEEN, CLUBS)), 1);
		checkCompare("pair of 2s, kicker Ace > pair of 2s, kicker King", byStrength,
				hand(c(TWO, CLUBS), c(TWO, HEARTS), c(ACE, SPADES), c(NINE, DIAMONDS), c(SEVEN, CLUBS)),
				hand(c(TWO, DIAMONDS), c(TWO, SPADES), c(KING, CLUBS), c(NINE, HEARTS), c(SEVEN, SPADES)), 1);
		checkCompare("Ace-high flush > King-high flush", byStrength,
				hand(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(ACE, HEARTS)),
				hand(c(TWO, SPADES), c(FIVE, SPADES), c(SEVEN, SPADES), c(NINE, SPADES), c(KING, SPADES)), 1);
		checkCompare("Ace-high straight > King-high straight", byStrength,
				hand(c(TEN, CLUBS), c(JACK, HEARTS), c(QUEEN, SPADES), c(KING, DIAMONDS), c(ACE, CLUBS)),
				hand(c(NINE, CLUBS), c(TEN, HEARTS), c(JACK, SPADES), c(QUEEN, DIAMONDS), c(KING, CLUBS)), 1);
		checkCompare("ace-low straight (5-high) < 6-high straight", byStrength,
				hand(c(ACE, CLUBS), c(TWO, HEARTS), c(THREE, SPADES), c(FOUR, DIAMONDS), c(FIVE, CLUBS)),
				hand(c(TWO, CLUBS), c(THREE, HEARTS), c(FOUR, SPADES), c(FIVE, DIAMONDS), c(SIX, CLUBS)), -1);
		checkCompare("same ranks, different suits are equal", byStrength,
				hand(c(TWO, CLUBS), c(TWO, HEARTS), c(ACE, SPADES), c(NINE, DIAMONDS), c(SEVEN, CLUBS)),
				hand(c(TWO, DIAMONDS), c(TWO, SPADES), c(ACE, CLUBS), c(NINE, HEARTS), c(SEVEN, SPADES)), 0);
	}
}