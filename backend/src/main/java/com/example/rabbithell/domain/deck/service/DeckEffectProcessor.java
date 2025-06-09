package com.example.rabbithell.domain.deck.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.deck.entity.Deck;
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.enums.CardRanking;
import com.example.rabbithell.domain.pawcard.enums.CardEmblem;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeckEffectProcessor {

	public void processAllEffects(List<Deck> equippedDecks, PawCardEffect effect, List<EffectDetail> existingDetails) {
		if (equippedDecks == null || equippedDecks.isEmpty())
			return;

		List<Integer> cardNumbers = equippedDecks.stream()
			.map(deck -> deck.getPawCard().getCardNumber())
			.sorted()
			.toList();

		List<CardEmblem> emblems = equippedDecks.stream()
			.map(deck -> deck.getPawCard().getCardEmblem())
			.toList();

		// 스트레이트/플러시/스트레이트 플러시/로열 스트레이트 플러시 우선 판별
		if (equippedDecks.size() >= 4) {
			CardRanking flushRanking = evaluateFlushStraight(cardNumbers, emblems);
			if (flushRanking != null) {
				effect.markCardRanking(flushRanking);
				existingDetails.get(0).updateEffectDetail(mapStatType(flushRanking), flushRanking.getRankingRatio());
				existingDetails.get(1).updateEffectDetail(null, null);
				return;
			}
		}

		// Pair 계열 판단
		Map<Integer, List<Deck>> byCardNumber = equippedDecks.stream()
			.collect(Collectors.groupingBy(d -> d.getPawCard().getCardNumber()));

		List<List<Deck>> groups = byCardNumber.values().stream()
			.filter(group -> group.size() >= 2)
			.sorted(Comparator.comparingInt((List<?> group) -> group.size()).reversed())
			.toList();

		CardRanking ranking = switch (groups.size()) {
			case 2 -> CardRanking.TWO_PAIR;
			case 1 -> switch (groups.get(0).size()) {
				case 2 -> CardRanking.ONE_PAIR;
				case 3 -> CardRanking.THREE_OF_A_KIND;
				case 4 -> CardRanking.FOUR_OF_A_KIND;
				default -> null;
			};
			default -> null;
		};

		if (ranking != null) {
			effect.markCardRanking(ranking);

			if (groups.size() == 1) {
				Deck deck = groups.get(0).get(0);
				StatType statType = deck.getPawCard().getStatType();
				Integer value = ranking.getRankingRatio() * deck.getPawCard().getRatioPercent();
				existingDetails.get(0).updateEffectDetail(statType, value);
				existingDetails.get(1).updateEffectDetail(null, null);
			} else {
				Deck deck1 = groups.get(0).get(0);
				Deck deck2 = groups.get(1).get(0);

				StatType s1 = deck1.getPawCard().getStatType();
				StatType s2 = deck2.getPawCard().getStatType();
				Integer v1 = ranking.getRankingRatio() * deck1.getPawCard().getRatioPercent();
				Integer v2 = ranking.getRankingRatio() * deck2.getPawCard().getRatioPercent();

				existingDetails.get(0).updateEffectDetail(s1, v1);
				existingDetails.get(1).updateEffectDetail(s1, v2);
			}
		}
	}

	private CardRanking evaluateFlushStraight(List<Integer> numbers, List<CardEmblem> emblems) {
		boolean isFlush = emblems.stream().distinct().count() == 1;
		boolean isStraight = isConsecutive(numbers);
		boolean isRoyal = new HashSet<>(numbers).equals(Set.of(1, 11, 12, 13));

		if (isRoyal && isFlush)
			return CardRanking.ROYAL_STRAIGHT_FLUSH;
		if (isFlush && isStraight)
			return CardRanking.STRAIGHT_FLUSH;
		if (isFlush)
			return CardRanking.FLUSH;
		if (isStraight)
			return CardRanking.STRAIGHT;
		return null;
	}

	private boolean isConsecutive(List<Integer> numbers) {
		List<Integer> sorted = new ArrayList<>(numbers);
		sorted.sort(Comparator.naturalOrder());
		for (int i = 0; i < sorted.size() - 1; i++) {
			if (sorted.get(i + 1) - sorted.get(i) != 1)
				return false;
		}
		return true;
	}

	private StatType mapStatType(CardRanking ranking) {
		return switch (ranking) {
			case STRAIGHT -> StatType.STRAIGHT_STAT;
			case FLUSH -> StatType.FLUSH_STAT;
			case STRAIGHT_FLUSH -> StatType.STRAIGHT_FLUSH_STAT;
			case ROYAL_STRAIGHT_FLUSH -> StatType.ROYAL_STRAIGHT_FLUSH_STAT;
			default -> throw new IllegalArgumentException("Invalid CardRanking: " + ranking);
		};
	}
}
