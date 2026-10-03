package e3.chapter3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Hand implements Iterable<Card>, Comparable<Hand> {

	private List<Card> aCards = new ArrayList<>();
	private int aMaxCards;

	/**
	 * Creates a new, empty hand, which can hold a maximum of pMaxCards.
	 * 
	 * @param pMaxCards The maximum number of cards allowed in this hand.
	 * @pre pMaxCards > 0;
	 */
	public Hand(int pMaxCards) {
		assert pMaxCards > 0;
		aMaxCards = pMaxCards;
	}

	/**
	 * Add pCard to the hand.
	 * 
	 * @param pCard The card to add.
	 * @pre !isFull()
	 * @pre pCard != null;
	 */
	public void add(Card pCard) {
		assert pCard != null;
		assert !isFull();
		aCards.add(pCard);
	}

	/**
	 * @return True if the number of cards in the hand is the maximum number of
	 *         cards allowable, as specified in the constructor.
	 */
	public boolean isFull() {
		return aCards.size() == aMaxCards;
	}

	/**
	 * @return True if there are no cards in this hand.
	 */
	public boolean isEmpty() {
		return aCards.isEmpty();
	}

	/**
	 * Removes pCards if it is in the hand. If it is not in the hand, does nothing.
	 * 
	 * @param pCard The card to remove.
	 * @pre pCards != null;
	 */
	public void remove(Card pCard) {
		assert pCard != null;
		aCards.remove(pCard);
	}

	/**
	 * @param pCard A card to check for containment.
	 * @return True if pCard is a card in this hand.
	 * @pre pCard != null
	 */
	public boolean contains(Card pCard) {
		assert pCard != null;
		return aCards.contains(pCard);
	}

	public Iterator<Card> iterator() {
		return aCards.iterator();
	}

	public int compareTo(Hand pHand) {
		return size() - pHand.size();
	}

	public static Comparator<Hand> createAscendingComparator() {
		return new Comparator<Hand>() {
			public int compare(Hand pHand1, Hand pHand2) {
				return pHand1.aCards.size() - pHand2.aCards.size();
			}
		};
	}

	public static Comparator<Hand> createDescendingComparator() {
		return new Comparator<Hand>() {
			public int compare(Hand pHand1, Hand pHand2) {
				return Integer.compare(pHand2.aCards.size(), pHand1.aCards.size());
			}
		};
	}

	/**
	 * @return The number of cards currently in the hand.
	 */
	public int size() {
		return aCards.size();
	}

	/**
	 * Creates a comparator that compares hands in terms of ascending number of
	 * cards of rank pRank in the hand.
	 * 
	 * @param pRank The rank to test against.
	 * @return A new Comparator instance that can compare by number of cards of the
	 *         specified rank.
	 */
	public static Comparator<Hand> createByRankComparator(Rank pRank) {

		return new Comparator<Hand>() {

			public int compare(Hand pHand1, Hand pHand2) {
				return countCards(pHand1, pRank) - countCards(pHand2, pRank);
			}

			private int countCards(Hand pHand, Rank pRank) {
				int total = 0;
				for (Card card : pHand) {
					if (card.rank() == pRank) {
						total++;
					}
				}
				return total;
			}
		};
	}

	/* ---------------- Exercise 12: poker hands ---------------- */

	/**
	 * Determines the poker hand type of this hand. Simplification: only ace-low
	 * straights are recognized (Ace, Two, Three, Four, Five is a straight, but
	 * Ten, Jack, Queen, King, Ace is not).
	 * 
	 * @return The poker hand type of this hand.
	 * @pre size() == 5
	 */
	public PokerHandType getPokerHandType() {
		assert size() == 5;
		boolean flush = isFlush();
		boolean straight = isStraight();
		if (straight && flush) {
			return PokerHandType.STRAIGHT_FLUSH;
		}
		int[] counts = rankCounts(); // sorted in ascending order
		int max = counts[counts.length - 1];
		int second = counts[counts.length - 2];
		if (max == 4) {
			return PokerHandType.FOUR_OF_A_KIND;
		}
		if (max == 3 && second == 2) {
			return PokerHandType.FULL_HOUSE;
		}
		if (flush) {
			return PokerHandType.FLUSH;
		}
		if (straight) {
			return PokerHandType.STRAIGHT;
		}
		if (max == 3) {
			return PokerHandType.THREE_OF_A_KIND;
		}
		if (max == 2 && second == 2) {
			return PokerHandType.TWO_PAIR;
		}
		if (max == 2) {
			return PokerHandType.ONE_PAIR;
		}
		return PokerHandType.HIGH_CARD;
	}

	/** @return The number of cards of each rank, sorted in ascending order. */
	private int[] rankCounts() {
		int[] counts = new int[Rank.values().length];
		for (Card card : aCards) {
			counts[card.rank().ordinal()]++;
		}
		Arrays.sort(counts);
		return counts;
	}

	private boolean isFlush() {
		Suit suit = aCards.get(0).suit();
		for (Card card : aCards) {
			if (card.suit() != suit) {
				return false;
			}
		}
		return true;
	}

	/** Ace-low straights only. */
	private boolean isStraight() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		boolean[] seen = new boolean[Rank.values().length];
		for (Card card : aCards) {
			int ordinal = card.rank().ordinal();
			if (seen[ordinal]) {
				return false; // a repeated rank rules out a straight
			}
			seen[ordinal] = true;
			min = Math.min(min, ordinal);
			max = Math.max(max, ordinal);
		}
		return max - min == 4;
	}

	/**
	 * Creates a comparator that compares hands by the strength of their poker
	 * hand type only (card values are not used to break ties).
	 * 
	 * @return A new Comparator instance.
	 * @pre Both hands compared must contain exactly five cards.
	 */
	public static Comparator<Hand> createByPokerHandTypeComparator() {
		return new Comparator<Hand>() {
			public int compare(Hand pHand1, Hand pHand2) {
				return pHand1.getPokerHandType().compareTo(pHand2.getPokerHandType());
			}
		};
	}
}