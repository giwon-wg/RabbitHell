package com.example.rabbithell.common.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.item.enums.Rarity;
import com.example.rabbithell.domain.item.repository.ItemRepository;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.kingdom.repository.KingdomRepository;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.entity.MonsterEncounter;
import com.example.rabbithell.domain.monster.enums.Rating;
import com.example.rabbithell.domain.monster.repository.MonsterEncounterRepository;
import com.example.rabbithell.domain.monster.repository.MonsterRepository;
import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.specie.repository.SpecieRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.domain.village.entity.Village;
import com.example.rabbithell.domain.village.repository.VillageConnectionRepository;
import com.example.rabbithell.domain.village.repository.VillageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CloverRepository cloverRepository;

	@Autowired
	private CharacterRepository characterRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private InventoryItemRepository inventoryItemRepository;

	@Autowired
	private VillageRepository villageRepository;

	@Autowired
	private VillageConnectionRepository villageConnectionRepository;

	@Autowired
	private MonsterRepository monsterRepository;
	@Autowired
	private MonsterEncounterRepository monsterEncounterRepository;

	@Autowired
	private SpecieRepository specieRepository;

	@Autowired
	private KingdomRepository kingdomRepository;

	@Override
	public void run(String... args) throws Exception {

		Village village1 = new Village("마을1");
		Village village2 = new Village("마을2");
		Village village3 = new Village("마을3");

		villageRepository.save(village1);
		villageRepository.save(village2);
		villageRepository.save(village3);

		Specie LopEared = Specie.builder()
			.speciesName("롭이어")
			.speciesDetail("위대한 롭이어! 아래로 늘어진 귀, 평야에서 살아남기 위해 민쳡해 졌다.")
			.build();

		Specie angora = Specie.builder()
			.speciesName("앙고라")
			.speciesDetail("풍성한 털은 일족의 자존심!, 복실복실한 털로 인해 방어력이 높다")
			.build();

		Specie Dwarf = Specie.builder()
			.speciesName("드워프")
			.speciesDetail("대양의 주인!, 바다에 익숙하며 체력이 높다")
			.build();

		specieRepository.save(LopEared);
		specieRepository.save(angora);
		specieRepository.save(Dwarf);

		Kingdom LobbitKingdom = Kingdom.builder()
			.kingdomName("LobbitKingdom")
			.kingdomDetail("드넓은 평야의 롭이어의 왕국")
			.villages(List.of(village1))
			.build();

		Kingdom Angoland = Kingdom.builder()
			.kingdomName("Angoland")
			.kingdomDetail("혹한의 산맥에 형성된 부족 국가")
			.villages(List.of(village2))
			.build();

		Kingdom Dwarfines = Kingdom.builder()
			.kingdomName("Dwarfines")
			.kingdomDetail("크레센트 열도에 위치한 해상무역 중심 도시 연맹")
			.villages(List.of(village3))
			.build();

		kingdomRepository.save(LobbitKingdom);
		kingdomRepository.save(Angoland);
		kingdomRepository.save(Dwarfines);

		String encodedPassword = passwordEncoder.encode("1111");

		User user = new User("name", "email", encodedPassword, User.Role.USER, false);
		User user2 = new User("name", "email2", encodedPassword, User.Role.USER, false);
		User user3 = new User("name", "email3", encodedPassword, User.Role.USER, false);

		userRepository.save(user);
		userRepository.save(user2);
		userRepository.save(user3);

		Clover clover = new Clover("clover", user, LobbitKingdom, LopEared, 100000L, 100000L, 1L, null);
		Clover clover2 = new Clover("clover2", user2, Angoland, angora, 100000L, 100000L, 2L, null);
		Clover clover3 = new Clover("clover3", user3, Dwarfines, Dwarf, 100000L, 100000L, 3L, null);

		cloverRepository.save(clover);
		cloverRepository.save(clover2);
		cloverRepository.save(clover3);

		GameCharacter character1 = new GameCharacter(user, clover, "토끼1", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character2 = new GameCharacter(user, clover, "토끼2", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character3 = new GameCharacter(user, clover, "토끼3", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character4 = new GameCharacter(user, clover, "토끼4", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		characterRepository.save(character1);
		characterRepository.save(character2);
		characterRepository.save(character3);
		characterRepository.save(character4);

		GameCharacter character5 = new GameCharacter(user2, clover2, "토끼5", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character6 = new GameCharacter(user2, clover2, "토끼6", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character7 = new GameCharacter(user2, clover2, "토끼7", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character8 = new GameCharacter(user2, clover2, "토끼8", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		characterRepository.save(character5);
		characterRepository.save(character6);
		characterRepository.save(character7);
		characterRepository.save(character8);

		GameCharacter character9 = new GameCharacter(user3, clover3, "토끼9", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character10 = new GameCharacter(user3, clover3, "토끼10", Job.INCOMPETENT, 50, 4900, 1000, 1000,
			1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character11 = new GameCharacter(user3, clover3, "토끼11", Job.INCOMPETENT, 50, 4900, 1000, 1000,
			1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character12 = new GameCharacter(user3, clover3, "토끼12", Job.INCOMPETENT, 50, 4900, 1000, 1000,
			1000,
			1000, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		characterRepository.save(character9);
		characterRepository.save(character10);
		characterRepository.save(character11);
		characterRepository.save(character12);

		Inventory inventory = new Inventory(clover, 100);
		inventoryRepository.save(inventory);

		Item weapon = new Item(null, "지존킹왕짱당근", "당근쵝오", ItemType.SWORD, Rarity.COMMON, 0L, 10L, null, null, 3L, 100,
			false);
		Item armor = new Item(null, "원피스", "예쁜원피스", ItemType.SHIELD, Rarity.COMMON, 0L, 10L, null, null, 3L, 100,
			false);
		Item accessory = new Item(null, "토끼풀귀걸이", "행운이깃든귀걸이", ItemType.SHIELD, Rarity.COMMON, 0L, 10L, null, null, 3L,
			100, false);

		itemRepository.save(weapon);
		itemRepository.save(armor);
		itemRepository.save(accessory);

		InventoryItem inventoryWeapon1 = new InventoryItem(inventory, weapon, character1, 100, Slot.HAND);
		InventoryItem inventoryArmor1 = new InventoryItem(inventory, armor, character1, 100, Slot.BODY);
		InventoryItem inventoryAccessory1 = new InventoryItem(inventory, accessory, character1, 100, Slot.HEAD);

		InventoryItem inventoryWeapon2 = new InventoryItem(inventory, weapon, character2, 100, Slot.HAND);
		InventoryItem inventoryArmor2 = new InventoryItem(inventory, armor, character2, 100, Slot.BODY);
		InventoryItem inventoryAccessory2 = new InventoryItem(inventory, accessory, character2, 100, Slot.HEAD);

		InventoryItem inventoryWeapon3 = new InventoryItem(inventory, weapon, character3, 100, Slot.HAND);
		InventoryItem inventoryArmor3 = new InventoryItem(inventory, armor, character3, 100, Slot.BODY);
		InventoryItem inventoryAccessory3 = new InventoryItem(inventory, accessory, character3, 100, Slot.HEAD);

		InventoryItem inventoryWeapon4 = new InventoryItem(inventory, weapon, character4, 100, Slot.HAND);
		InventoryItem inventoryArmor4 = new InventoryItem(inventory, armor, character4, 100, Slot.BODY);
		InventoryItem inventoryAccessory4 = new InventoryItem(inventory, accessory, character4, 100, Slot.HEAD);

		inventoryItemRepository.save(inventoryWeapon1);
		inventoryItemRepository.save(inventoryArmor1);
		inventoryItemRepository.save(inventoryAccessory1);
		inventoryItemRepository.save(inventoryWeapon2);
		inventoryItemRepository.save(inventoryArmor2);
		inventoryItemRepository.save(inventoryAccessory2);
		inventoryItemRepository.save(inventoryWeapon3);
		inventoryItemRepository.save(inventoryArmor3);
		inventoryItemRepository.save(inventoryAccessory3);
		inventoryItemRepository.save(inventoryWeapon4);
		inventoryItemRepository.save(inventoryArmor4);
		inventoryItemRepository.save(inventoryAccessory4);

		Monster slime = new Monster(Rating.COMMON, "슬라임", 5000, 150, 10, 200, 30);
		monsterRepository.save(slime);

		Monster goblin = new Monster(Rating.COMMON, "고블린", 5000, 200, 80, 400, 40);
		monsterRepository.save(goblin);

		MonsterEncounter slimeEncounter = new MonsterEncounter(10, slime, BattleFieldType.PLAIN);
		monsterEncounterRepository.save(slimeEncounter);

		MonsterEncounter goblinEncounter = new MonsterEncounter(10, goblin, BattleFieldType.PLAIN);
		monsterEncounterRepository.save(goblinEncounter);

	}
}
