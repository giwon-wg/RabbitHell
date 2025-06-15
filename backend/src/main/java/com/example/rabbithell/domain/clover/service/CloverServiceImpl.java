package com.example.rabbithell.domain.clover.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.clover.dto.response.CloverNameResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverPublicResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverResponse;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.enums.EffectDetailSlot;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.kingdom.repository.KingdomRepository;
import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.specie.repository.SpecieRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloverServiceImpl implements CloverService {

	private final CloverRepository cloverRepository;
	private final UserRepository userRepository;
	private final KingdomRepository kingdomRepository;
	private final SpecieRepository specieRepository;
	private final InventoryRepository inventoryRepository;

	@Transactional(readOnly = true)
	@Override
	public CloverResponse getMyClover(Long userId) {
		Clover clover = cloverRepository.findByUserIdOrElseThrow(userId);
		return CloverResponse.from(clover);
	}

	@Transactional(readOnly = true)
	@Override
	public CloverPublicResponse getCloverById(Long cloverId) {
		Clover clover = cloverRepository.findByIdOrElseThrow(cloverId);
		return CloverPublicResponse.from(clover);

	}

	@Transactional(readOnly = true)
	@Override
	public List<CloverNameResponse> getAllCloverNames() {
		return cloverRepository.findAll().stream()
			.map(c -> new CloverNameResponse(c.getId(), c.getName()))
			.toList();
	}

	@Override
	public Map<String, Object> getCloverInfoForMiniToken(Long userId) {

		User user = userRepository.findByIdOrElseThrow(userId);

		Map<String, Object> result = new HashMap<>();

		result.put("hasClover", cloverRepository.existsByUserId(userId));
		result.put("nickname", user.getName());

		cloverRepository.findByUserId(userId)
			.ifPresent(clover -> result.put("cloverName", clover.getName()));

		return result;
	}

	public Clover createClover(User user, String cloverName, Long userId, Long kingdomId) {


		Clover clover = cloverRepository.findByUserId(userId).orElse(null);

		if (clover == null) {
			Kingdom kingdom = kingdomRepository.findByIdOrElseThrow(kingdomId);
			Specie specie = specieRepository.findByIdOrElseThrow(kingdomId);

			// Clover 생성
			clover = Clover.builder()
				.user(user)
				.name(cloverName)
				.cash(0L)
				.kingdom(kingdom)
				.specie(specie)
				.currentVillage(kingdomId)
				.saving(0L)
				.build();

			// PawCard 생성
			PawCardEffect pawCardEffect = PawCardEffect.builder().build();
			EffectDetail effectDetail1 = EffectDetail.builder().effectDetailSlot(EffectDetailSlot.EFFECT_DETAIL_SLOT_1).build();
			EffectDetail effectDetail2 = EffectDetail.builder().effectDetailSlot(EffectDetailSlot.EFFECT_DETAIL_SLOT_2).build();
			pawCardEffect.addEffectDetail(effectDetail1);
			pawCardEffect.addEffectDetail(effectDetail2);
			clover.addPawCardEffect(pawCardEffect);

			// 인벤토리 생성
			Inventory inventory = Inventory.builder()
				.clover(clover)
				.capacity(100)
				.build();

			cloverRepository.save(clover);

			inventoryRepository.save(inventory);
		}
		return clover;
	}
}
