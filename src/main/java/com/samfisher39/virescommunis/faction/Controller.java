package com.samfisher39.virescommunis.faction;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Controller{

	public Map<String,Integer> counterMap;
	public Map<String,Integer> bnsDamageMap; //string: mob name, integer: bonus damage to this mob
	public int bnsOverallDamage; // this bonus damage affects every mob
	Map<String,Entity> mobsMap;
	public ArrayList<EntityPlayerMP> playerList;
	public Map<String, Integer> skillPriceMap;
	public Map<String, Boolean> skillMap;
	private int money;
	
	public Controller(UUID playerUUID) 
	{	
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
		this.counterMap = new TreeMap<String, Integer>(); // Tree Maps are ordered alphabetically by key
		this.mobsMap = new TreeMap<String, Entity>();
		this.bnsDamageMap = new TreeMap<String, Integer>();
		this.playerList = new ArrayList<EntityPlayerMP>();
		setMoney(0);

		this.skillPriceMap = new TreeMap<String, Integer>();
		this.skillMap = new TreeMap<String, Boolean>();
		this.bnsOverallDamage = 0;

		AddSkillToMaps("dmg5all", 5);
		AddSkillToMaps("dmg10all", 10);
		AddSkillToMaps("dmg20all", 15);
		
		// get all living mobs and register them to the mobsMap
		ForgeRegistries.ENTITIES.getValuesCollection().stream().forEach(s -> {
			Entity tmpEntity = EntityList.createEntityByIDFromName(s.getRegistryName(), world);
			if (tmpEntity.isCreatureType(EnumCreatureType.CREATURE, false) ||
					tmpEntity.isCreatureType(EnumCreatureType.MONSTER, false)) {
				mobsMap.put(s.getName(), tmpEntity);
				counterMap.put(s.getName(), 0);
				bnsDamageMap.put(s.getName(), 0);
				
				AddSkillToMaps("dmg5" + s.getName(), 5); // add "bns damage"-skill for this mob
				
			}
			tmpEntity.setDead();
		});
		if (!world.isRemote) {
			playerList.add((EntityPlayerMP) world.getPlayerEntityByUUID(playerUUID));
		}
	}

	public void ListAvailableSkills(EntityPlayerMP player)
	{
		for (Map.Entry<String, Integer> skillPriceEntry : skillPriceMap.entrySet()) {
			player.sendMessage(new TextComponentString(skillPriceEntry.getKey() + ": " + skillPriceEntry.getValue()));
		}
	}
	
	public void AddSkillToMaps(String name, int cost)
	{
		skillPriceMap.put(name, cost);
		skillMap.put(name, false);
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	public void tradeCountForMoney()
	{
		Map<String, Integer> newMap = new TreeMap<String, Integer>();
		
		for (Map.Entry<String, Integer> counterEntry : this.counterMap.entrySet()) {
			addMoney(counterEntry.getValue());
			newMap.put(counterEntry.getKey(), 0);
		}
		
		this.counterMap = newMap;
		UpdateBnsDamage();
	}
	
	public void addMoney(int income)
	{
		this.setMoney(this.getMoney() + income);
	}
	
	public void subtractMoney(int expenses)
	{
		this.setMoney(this.getMoney() - expenses);
	}
	
	public void RemovePlayer(EntityPlayerMP player)
	{
		for (EntityPlayerMP player2 : this.playerList) {
			if (player2.getUniqueID() == player.getUniqueID()) {
				this.playerList.remove(player2);
			}
		}
	}
	
	public void AddPlayer(EntityPlayerMP player)
	{
		
	}
	
	
//	public void PrintLoadedToConsole() 
//	{
//		System.out.println("-----------------------------------");
//		System.out.println("L O A D E D   E N T I T I E S :");
//		for (Entry<String, Entity> entry : mobsMap.entrySet()) {
//			String key = entry.getKey();			
//			System.out.println(key);
//		}
//		if (playerList == null || playerList.isEmpty()) {
//			if (playerList == null) {
//				System.out.println("!!! PLAYERLIST IS NULL !!!");
//			}
//			else {
//				System.out.println("!!! PLAYERLIST IS EMPTY !!!");
//			}
//		}
//		else {
//			System.out.println();
//			System.out.println("O N L I N E   P L A Y E R S :");
//			for (EntityPlayerMP playerMP : playerList) {
//				String name = playerMP.getName();
//				UUID uuid = playerMP.getUniqueID();
//				System.out.println(name + " - " + uuid);
//			}
//		}
//		System.out.println("-----------------------------------");
//	}
	
	public void SaveLoadedToFile() 
	{
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter("loaded_mobs.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (writer != null) {
			writer.println("-----------------------------------");
			writer.println("L O A D E D   E N T I T I E S :");
			for (Entry<String, Entity> entry : mobsMap.entrySet()) {
				String key = entry.getKey();
				Entity entity = entry.getValue();
				
				writer.println(key + " - " + entity);
			}
			writer.println("\n\nO N L I N E   P L A Y E R S :");
			for (EntityPlayerMP playerMP : playerList) {
				String name = playerMP.getName();
				UUID uuid = playerMP.getUniqueID();
				
				writer.println(name + " - " + uuid);
			}
			writer.println("-----------------------------------");
			writer.close();
		}
	}

	public void UpdateGameMaster(Entity source, Entity target) 
	{
		if (source instanceof EntityPlayerMP || source instanceof EntityPlayer) {
			IncreaseMobCountOf(target);
		}
	}
	
	public void PrintCounterMapInGame(EntityPlayer player)
	{
		for (Entry<String, Integer> entry : counterMap.entrySet()) {
			String mobName = entry.getKey();
			int mobCounter = entry.getValue();
			
			player.sendMessage(new TextComponentString(mobName + ": " + mobCounter));
			
		}
	}
	
	public void IncreaseMobCountOf(Entity mob)
	{
		if (mobsMap.containsKey(mob.getName())) {
			int preKillCount = counterMap.get(mob.getName());
			System.out.println("Prekill: " + preKillCount);
			counterMap.remove(mob.getName());
			counterMap.put(mob.getName(), preKillCount + 1);
			System.out.println("Postkill: " + counterMap.get(mob.getName()));
			
			UpdateBnsDamage(mob);
		}		
	}
	
	public void UpdateBnsDamage(Entity mob)
	{

		if (counterMap.get(mob.getName()) == 0) {
			bnsDamageMap.remove(mob.getName());
			bnsDamageMap.put(mob.getName(), 0);
		}
		if (counterMap.get(mob.getName()) >= 1) {
			bnsDamageMap.remove(mob.getName());
			bnsDamageMap.put(mob.getName(), 10);
		}
		if (counterMap.get(mob.getName()) >= 3) {
			bnsDamageMap.remove(mob.getName());
			bnsDamageMap.put(mob.getName(), 20);
		}
		if (counterMap.get(mob.getName()) >= 5) {
			bnsDamageMap.remove(mob.getName());
			bnsDamageMap.put(mob.getName(), 50);
		}
		if (counterMap.get(mob.getName()) >= 10) {
			bnsDamageMap.remove(mob.getName());
			bnsDamageMap.put(mob.getName(), 100);
		}
		
	}
	
	public void UpdateBnsDamage()
	{
		for (Map.Entry<String, Integer> counterEntry: counterMap.entrySet()) {
			String mobName = counterEntry.getKey();
			if (counterMap.get(mobName) == 0) {
				bnsDamageMap.replace(mobName, 0);
			}
			if (counterMap.get(mobName) >= 1) {
				bnsDamageMap.replace(mobName, 10);
			}
			if (counterMap.get(mobName) >= 3) {
				bnsDamageMap.replace(mobName, 20);
			}
			if (counterMap.get(mobName) >= 5) {
				bnsDamageMap.replace(mobName, 50);
			}
			if (counterMap.get(mobName) >= 10) {
				bnsDamageMap.replace(mobName, 100);
			}
		}
	}
	
	public int GetBnsDamage(Entity mob)
	{
		return this.bnsDamageMap.get(mob.getName());
	}

	public void PrintTargetCounterInGameToPlayer(EntityPlayer player, Entity target) 
	{	
		player.sendMessage(new TextComponentString(target.getName() + ": " + counterMap.get(target.getName())));
	}
}
