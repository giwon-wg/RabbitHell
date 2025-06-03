package com.example.rabbithell.common.util;

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
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.entity.MonsterEncounter;
import com.example.rabbithell.domain.monster.enums.Rating;
import com.example.rabbithell.domain.monster.repository.MonsterEncounterRepository;
import com.example.rabbithell.domain.monster.repository.MonsterRepository;
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

	@Override
	public void run(String... args) throws Exception {

		Village village1 = new Village("마을1");
		Village village2 = new Village("마을2");
		Village village3 = new Village("마을3");

		villageRepository.save(village1);
		villageRepository.save(village2);
		villageRepository.save(village3);

		String encodedPassword = passwordEncoder.encode("1111");

		User user = new User("name", "email", encodedPassword, User.Role.USER, false);
		userRepository.save(user);

		Clover clover = new Clover("clover", user, 100000L, 100000L, 1L, null);
		cloverRepository.save(clover);

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

		Inventory inventory = new Inventory(clover, 100);
		inventoryRepository.save(inventory);

		Item weapon = new Item(null, "지존킹왕짱당근", ItemType.SWORD, Rarity.COMMON, 0L, 10L, null, null, 3L, 100, false);
		Item armor = new Item(null, "원피스", ItemType.SHIELD, Rarity.COMMON, 0L, 10L, null, null, 3L, 100, false);
		Item accessory = new Item(null, "토끼풀귀걸이", ItemType.SHIELD, Rarity.COMMON, 0L, 10L, null, null, 3L, 100, false);

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

		Monster slime = new Monster(Rating.COMMON, "슬라임", 5000, 150, 10, 200);
		monsterRepository.save(slime);

		Monster goblin = new Monster(Rating.COMMON, "고블린", 5000, 200, 80, 400);
		monsterRepository.save(goblin);

		MonsterEncounter slimeEncounter = new MonsterEncounter(10, slime, BattleFieldType.PLAIN);
		monsterEncounterRepository.save(slimeEncounter);

		MonsterEncounter goblinEncounter = new MonsterEncounter(10, goblin, BattleFieldType.PLAIN);
		monsterEncounterRepository.save(goblinEncounter);

	}
}
