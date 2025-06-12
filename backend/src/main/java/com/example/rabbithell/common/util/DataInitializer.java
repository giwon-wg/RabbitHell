package com.example.rabbithell.common.util;

import java.math.BigDecimal;
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
import com.example.rabbithell.domain.deck.entity.EffectDetail;
import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.deck.enums.EffectDetailSlot;
import com.example.rabbithell.domain.deck.repository.PawCardEffectRepository;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.entity.InventoryItem;
import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.inventory.repository.InventoryItemRepository;
import com.example.rabbithell.domain.inventory.repository.InventoryRepository;
import com.example.rabbithell.domain.item.entity.Effect;
import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.enums.EffectType;
import com.example.rabbithell.domain.item.enums.ItemType;
import com.example.rabbithell.domain.item.enums.Rarity;
import com.example.rabbithell.domain.item.repository.EffectRepository;
import com.example.rabbithell.domain.item.repository.ItemRepository;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.kingdom.repository.KingdomRepository;
import com.example.rabbithell.domain.monster.entity.DropRate;
import com.example.rabbithell.domain.monster.entity.Monster;
import com.example.rabbithell.domain.monster.entity.MonsterEncounter;
import com.example.rabbithell.domain.monster.enums.Rating;
import com.example.rabbithell.domain.monster.repository.DropRateRepository;
import com.example.rabbithell.domain.monster.repository.MonsterEncounterRepository;
import com.example.rabbithell.domain.monster.repository.MonsterRepository;
import com.example.rabbithell.domain.shop.entity.Shop;
import com.example.rabbithell.domain.shop.repository.ShopRepository;
import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.specie.repository.SpecieRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.domain.village.entity.Village;
import com.example.rabbithell.domain.village.entity.VillageConnection;
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
	private EffectRepository effectRepository;
	@Autowired
	private InventoryItemRepository inventoryItemRepository;

	@Autowired
	private VillageRepository villageRepository;

	@Autowired
	private VillageConnectionRepository villageConnectionRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private MonsterRepository monsterRepository;
	@Autowired
	private MonsterEncounterRepository monsterEncounterRepository;

	@Autowired
	private SpecieRepository specieRepository;

	@Autowired
	private KingdomRepository kingdomRepository;
	@Autowired
	private DropRateRepository dropRateRepository;

	@Autowired
	private PawCardEffectRepository pawCardEffectRepository;

	@Override
	public void run(String... args) throws Exception {

		Village village1 = new Village("마을1");
		Village village2 = new Village("마을2");
		Village village3 = new Village("마을3");
		Village village4 = new Village("마을4");

		villageRepository.save(village1);
		villageRepository.save(village2);
		villageRepository.save(village3);
		villageRepository.save(village4);

		VillageConnection villageConnection1 = new VillageConnection(1L, 2L);
		VillageConnection villageConnection2 = new VillageConnection(1L, 4L);
		VillageConnection villageConnection3 = new VillageConnection(2L, 1L);
		VillageConnection villageConnection4 = new VillageConnection(2L, 3L);
		VillageConnection villageConnection5 = new VillageConnection(3L, 2L);
		VillageConnection villageConnection6 = new VillageConnection(3L, 4L);
		VillageConnection villageConnection7 = new VillageConnection(4L, 3L);
		VillageConnection villageConnection8 = new VillageConnection(4L, 1L);

		villageConnectionRepository.save(villageConnection1);
		villageConnectionRepository.save(villageConnection2);
		villageConnectionRepository.save(villageConnection3);
		villageConnectionRepository.save(villageConnection4);
		villageConnectionRepository.save(villageConnection5);
		villageConnectionRepository.save(villageConnection6);
		villageConnectionRepository.save(villageConnection7);
		villageConnectionRepository.save(villageConnection8);

		Shop shop1 = new Shop(village1, "상점1", false);
		Shop shop2 = new Shop(village2, "상점2", false);
		Shop shop3 = new Shop(village3, "상점3", false);
		Shop shop4 = new Shop(village4, "상점4", false);

		shopRepository.save(shop1);
		shopRepository.save(shop2);
		shopRepository.save(shop3);
		shopRepository.save(shop4);

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

		User user = new User("name", "rabbithelldev1@gmail.com", "KAKAO", User.Role.USER, false);
		User user2 = new User("name", "rabbithelldev2@gmail.com", "KAKAO", User.Role.USER, false);
		User user3 = new User("name", "rabbithelldev3@gmail.com", "KAKAO", User.Role.USER, false);

		userRepository.save(user);
		userRepository.save(user2);
		userRepository.save(user3);

		User admin = new User("admin", "admin", encodedPassword, User.Role.ADMIN, false);
		userRepository.save(admin);

		Clover clover = new Clover("clover", user, LobbitKingdom, LopEared, 100000L, 100000L, 1L, null);
		Clover clover2 = new Clover("clover2", user2, Angoland, angora, 100000L, 100000L, 2L, null);
		Clover clover3 = new Clover("clover3", user3, Dwarfines, Dwarf, 100000L, 100000L, 3L, null);

		cloverRepository.save(clover);
		cloverRepository.save(clover2);
		cloverRepository.save(clover3);

		GameCharacter character1 = new GameCharacter(user, clover, "토끼1", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character2 = new GameCharacter(user, clover, "토끼2", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character3 = new GameCharacter(user, clover, "토끼3", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character4 = new GameCharacter(user, clover, "토끼4", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		characterRepository.save(character1);
		characterRepository.save(character2);
		characterRepository.save(character3);
		characterRepository.save(character4);

		GameCharacter character5 = new GameCharacter(user2, clover2, "토끼5", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character6 = new GameCharacter(user2, clover2, "토끼6", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character7 = new GameCharacter(user2, clover2, "토끼7", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character8 = new GameCharacter(user2, clover2, "토끼8", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		characterRepository.save(character5);
		characterRepository.save(character6);
		characterRepository.save(character7);
		characterRepository.save(character8);

		GameCharacter character9 = new GameCharacter(user3, clover3, "토끼9", Job.INCOMPETENT, 50, 4900, 1000, 1000, 1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character10 = new GameCharacter(user3, clover3, "토끼10", Job.INCOMPETENT, 50, 4900, 1000, 1000,
			1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character11 = new GameCharacter(user3, clover3, "토끼11", Job.INCOMPETENT, 50, 4900, 1000, 1000,
			1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		GameCharacter character12 = new GameCharacter(user3, clover3, "토끼12", Job.INCOMPETENT, 50, 4900, 1000, 1000,
			1000,
			1000, 100, 100, 100, 100, 100, 0, 0, 0, 0, 0, 0);
		characterRepository.save(character9);
		characterRepository.save(character10);
		characterRepository.save(character11);
		characterRepository.save(character12);

		Inventory inventory = new Inventory(clover, 100);
		inventoryRepository.save(inventory);

		PawCardEffect pawCardEffect = PawCardEffect.builder().clover(clover).build();
		EffectDetail effectDetail1 = EffectDetail.builder()
			.effectDetailSlot(EffectDetailSlot.EFFECT_DETAIL_SLOT_1)
			.build();
		EffectDetail effectDetail2 = EffectDetail.builder()
			.effectDetailSlot(EffectDetailSlot.EFFECT_DETAIL_SLOT_2)
			.build();

		pawCardEffect.addEffectDetail(effectDetail1);
		pawCardEffect.addEffectDetail(effectDetail2);

		pawCardEffectRepository.save(pawCardEffect);

		Effect hpEffect = new Effect(EffectType.HP, 10L, false);
		Effect strengthEffect = new Effect(EffectType.STRENGTH, 10L, false);
		Effect agilityEffect = new Effect(EffectType.AGILITY, 10L, false);
		Effect intelligenceEffect = new Effect(EffectType.INTELLIGENCE, 10L, false);
		Effect focusEffect = new Effect(EffectType.FOCUS, 10L, false);
		Effect luckEffect = new Effect(EffectType.LUCK, 10L, false);

		effectRepository.save(hpEffect);
		effectRepository.save(strengthEffect);
		effectRepository.save(agilityEffect);
		effectRepository.save(intelligenceEffect);
		effectRepository.save(focusEffect);
		effectRepository.save(luckEffect);

		Item weapon = new Item(shop1, strengthEffect, "지존킹왕짱당근", "당근쵝오", ItemType.SWORD, Rarity.COMMON, 100L, 20L, 20L, 3L, 3L, 100, false);
		Item armor = new Item(shop1, hpEffect, "원피스", "예쁜원피스", ItemType.ARMOR, Rarity.COMMON, 200L, 20L, 20L, 3L, 3L, 100, false);
		Item accessory = new Item(shop1, luckEffect, "토끼풀귀걸이", "행운이깃든귀걸이", ItemType.ACCESSORY, Rarity.COMMON, 300L, 20L, 20L, 3L, 3L, 100, false);

		Item hpPotion = new Item(shop2, null, "HP 포션", "HP를 채워줍니당.", ItemType.HP, Rarity.COMMON, 100L, 0L, 0L, 0L, 0L, 10000, false);
		Item mpPotion = new Item(shop2, null, "MP 포션", "MP를 채워줍니당.", ItemType.MP, Rarity.COMMON, 100L, 0L, 0L, 0L, 0L, 10000, false);

		Item feverRemedy = new Item(shop3, null, "해열제", "기원님 열좀 내리게 해주세요", ItemType.ETC, Rarity.COMMON, 500L, 0L, 0L, 0L, 0L, 10000, false);
		Item somiGun = new Item(shop3, focusEffect, "소미의 총", "소미님의 총은 백발백중", ItemType.BOW, Rarity.LEGENDARY, 1000L, 100L, 50L, 10L, 5L, 10000, false);
		Item fourCard = new Item(shop3, intelligenceEffect, "포카드", "효성님은 포카드 하는중", ItemType.ACCESSORY, Rarity.MYTH, 5000L, 90L, 89L, 19L, 4L, 10000, false);
		Item airplaneTicket = new Item(shop3, agilityEffect, "제주도행 비행기 표", "지윤님 잘 다녀오세요.", ItemType.DAGGER, Rarity.UNIQUE, 8000L, 100L, 30L, 10L, 4L, 10000, false);
		Item wakeUp = new Item(shop3, null, "잠깨는 약", "전화왔어요 일어나세요!!", ItemType.ETC, Rarity.RARE, 3000L, 0L, 0L, 0L, 0L, 10000, false);
		Item slimeBell = new Item(shop4, null, "슬라임의 방울", "쫀득하니 맛있어요", ItemType.ETC, Rarity.RARE, 1500L, 0L, 0L, 0L, 0L, 10000, false);
		Item tuxedo = new Item(shop4, hpEffect, "턱시도", "멋쟁이", ItemType.ARMOR, Rarity.RARE, 4000L, 10L, 10L, 3L, 3L, 10000, false);

		itemRepository.save(weapon);
		itemRepository.save(armor);
		itemRepository.save(accessory);
		itemRepository.save(hpPotion);
		itemRepository.save(mpPotion);
		itemRepository.save(feverRemedy);
		itemRepository.save(airplaneTicket);
		itemRepository.save(wakeUp);
		itemRepository.save(somiGun);
		itemRepository.save(fourCard);
		itemRepository.save(slimeBell);
		itemRepository.save(tuxedo);

		InventoryItem inventoryWeapon1 = new InventoryItem(inventory, weapon, character1, 20L, 100, 100, 3L, Slot.HAND,
			false);
		InventoryItem inventoryArmor1 = new InventoryItem(inventory, armor, character1, 20L, 100, 100, 3L, Slot.BODY,
			false);
		InventoryItem inventoryAccessory1 = new InventoryItem(inventory, accessory, character1, 20L, 100, 100, 3L,
			Slot.HEAD, false);

		InventoryItem inventoryWeapon2 = new InventoryItem(inventory, weapon, character2, 20L, 100, 100, 3L, Slot.HAND,
			false);
		InventoryItem inventoryArmor2 = new InventoryItem(inventory, armor, character2, 20L, 100, 100, 3L, Slot.BODY,
			false);
		InventoryItem inventoryAccessory2 = new InventoryItem(inventory, accessory, character2, 20L, 100, 100, 3L,
			Slot.HEAD, false);

		InventoryItem inventoryWeapon3 = new InventoryItem(inventory, weapon, character3, 20L, 100, 100, 3L, Slot.HAND,
			false);
		InventoryItem inventoryArmor3 = new InventoryItem(inventory, armor, character3, 20L, 100, 100, 3L, Slot.BODY,
			false);
		InventoryItem inventoryAccessory3 = new InventoryItem(inventory, accessory, character3, 20L, 100, 100, 3L,
			Slot.HEAD, false);

		InventoryItem inventoryWeapon4 = new InventoryItem(inventory, weapon, character4, 20L, 100, 100, 3L, Slot.HAND,
			false);
		InventoryItem inventoryArmor4 = new InventoryItem(inventory, armor, character4, 20L, 100, 100, 3L, Slot.BODY,
			false);
		InventoryItem inventoryAccessory4 = new InventoryItem(inventory, accessory, character4, 20L, 100, 100, 3L,
			Slot.HEAD, false);

		InventoryItem iHpPotion = new InventoryItem(inventory, hpPotion, null, 0L, 10000, 10000, 0L, null, false);
		InventoryItem iMpPotion = new InventoryItem(inventory, mpPotion, null, 0L, 10000, 10000, 0L, null, false);
		InventoryItem iFeverRemedy = new InventoryItem(inventory, feverRemedy, null, 0L, 10000, 10000, 0L, null, false);
		InventoryItem iSomiGun = new InventoryItem(inventory, somiGun, null, 80L, 10000, 10000, 8L, null, false);
		InventoryItem iFourCard = new InventoryItem(inventory, fourCard, null, 80L, 10000, 10000, 10L, null, false);
		InventoryItem iAirplaneTicket = new InventoryItem(inventory, airplaneTicket, null, 0L, 10000, 10000, 0L, null,
			false);
		InventoryItem iWakeUp = new InventoryItem(inventory, wakeUp, null, 0L, 10000, 10000, 0L, null, false);
		InventoryItem iSlimeBell = new InventoryItem(inventory, slimeBell, null, 0L, 10000, 10000, 0L, null, false);
		InventoryItem iTuxedo = new InventoryItem(inventory, tuxedo, null, 10L, 10000, 10000, 3L, null, false);

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
		inventoryItemRepository.save(iHpPotion);
		inventoryItemRepository.save(iMpPotion);
		inventoryItemRepository.save(iFeverRemedy);
		inventoryItemRepository.save(iSomiGun);
		inventoryItemRepository.save(iFourCard);
		inventoryItemRepository.save(iAirplaneTicket);
		inventoryItemRepository.save(iWakeUp);
		inventoryItemRepository.save(iSlimeBell);
		inventoryItemRepository.save(iTuxedo);

		// Common Monsters
		List<Monster> commonMonsters = List.of(
			createAndSaveMonster(Rating.COMMON, "슬라임", 5000, 150, 10, 200, 30),
			createAndSaveMonster(Rating.COMMON, "고블린", 5000, 200, 80, 400, 40),
			createAndSaveMonster(Rating.COMMON, "들쥐", 5000, 130, 20, 300, 20),
			createAndSaveMonster(Rating.COMMON, "거미", 4642, 43, 20, 200, 30),
			createAndSaveMonster(Rating.COMMON, "멧돼지", 3000, 40, 40, 40, 40),
			createAndSaveMonster(Rating.COMMON, "뱀", 3333, 43, 43, 43, 43),
			createAndSaveMonster(Rating.COMMON, "비틀", 3232, 32, 32, 32, 32),
			createAndSaveMonster(Rating.COMMON, "늑대", 4242, 42, 42, 42, 42),
			createAndSaveMonster(Rating.COMMON, "원숭이", 3454, 33, 34, 53, 21),
			createAndSaveMonster(Rating.COMMON, "회중시계", 3333, 33, 33, 33, 33)
		);
		commonMonsters.forEach(monster -> createMonsterEncounter(10, monster, BattleFieldType.PLAIN));

		// Rare Monsters
		List<Monster> rareMonsters = List.of(
			createAndSaveMonster(Rating.RARE, "풀의 정령", 4030, 23, 23, 77, 33),
			createAndSaveMonster(Rating.RARE, "골렘", 5000, 40, 40, 40, 40),
			createAndSaveMonster(Rating.RARE, "병사2호", 2222, 22, 22, 22, 22),
			createAndSaveMonster(Rating.RARE, "병사3호", 3333, 33, 33, 33, 33),
			createAndSaveMonster(Rating.RARE, "병사4호", 4444, 44, 44, 44, 44)
		);
		rareMonsters.forEach(monster -> createMonsterEncounter(3, monster, BattleFieldType.PLAIN));

		// Special Monster
		Monster goldenToad = createAndSaveMonster(Rating.SPECIAL, "황금 두꺼비", 777, 7, 7, 7, 77);
		createMonsterEncounter(3, goldenToad, BattleFieldType.PLAIN);

		Monster dragon = createAndSaveMonster(Rating.BOSS, "드래곤", 9999, 999, 999, 999, 999);
		createMonsterEncounter(10, dragon, BattleFieldType.RIFT);

		Monster queen = createAndSaveMonster(Rating.BOSS, "퀸", 777, 142, 30, 94, 211);
		createMonsterEncounter(3, queen, BattleFieldType.MOUNTAIN);

		createMonsterEncounter(3, goldenToad, BattleFieldType.CAVE);

		createMonsterEncounter(3, goldenToad, BattleFieldType.GOLDEN_FIELD);
		createMonsterEncounter(3, goldenToad, BattleFieldType.MAGIC_VALLEY);
		createMonsterEncounter(3, goldenToad, BattleFieldType.CRYSTAL_CAVE);
		createMonsterEncounter(3, goldenToad, BattleFieldType.DRAGON_NEST);
		createMonsterEncounter(3, goldenToad, BattleFieldType.TRIAL_FIELD);
		createMonsterEncounter(3, goldenToad, BattleFieldType.SAGE_FOREST);
		createMonsterEncounter(3, goldenToad, BattleFieldType.SPIRIT_TEMPLE);
		createMonsterEncounter(3, goldenToad, BattleFieldType.VOID_ARENA);
		createMonsterEncounter(3, goldenToad, BattleFieldType.LOOT_MEADOW);
		createMonsterEncounter(3, goldenToad, BattleFieldType.DIM_CRACK);
		createMonsterEncounter(3, goldenToad, BattleFieldType.TWILIGHT_CRACK);
		createMonsterEncounter(3, goldenToad, BattleFieldType.ETHER_CRACK);
		createMonsterEncounter(3, goldenToad, BattleFieldType.NEXUS_CRACK);
		createMonsterEncounter(3, goldenToad, BattleFieldType.DREAM_CRACK);
		createMonsterEncounter(3, goldenToad, BattleFieldType.CENTER_CRACK);

		DropRate slimeBellDrop = new DropRate(commonMonsters.get(0), slimeBell, BigDecimal.valueOf(0.5));
		dropRateRepository.save(slimeBellDrop);

		DropRate slimeBellDrop2 = new DropRate(goldenToad, slimeBell, BigDecimal.valueOf(0.5));
		dropRateRepository.save(slimeBellDrop2);

	}

	private Monster createAndSaveMonster(Rating rating, String name, int hp, int atk, int def, int spd, int exp) {
		Monster monster = new Monster(rating, name, hp, atk, def, spd, exp);
		monsterRepository.save(monster);
		return monster;
	}

	private void createMonsterEncounter(int rate, Monster monster, BattleFieldType type) {
		MonsterEncounter encounter = new MonsterEncounter(rate, monster, type);
		monsterEncounterRepository.save(encounter);
	}

}
