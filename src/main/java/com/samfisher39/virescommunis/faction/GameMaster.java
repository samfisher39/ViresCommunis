package com.samfisher39.virescommunis.faction;

import java.awt.font.TextAttribute;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GameMaster {

	static Map<String,Integer> counterMap;
	static Map<String,Entity> mobsMap;
	static ArrayList<EntityPlayerMP> playerList;
	
	
	public GameMaster(EntityPlayerMP player) 
	{	
		World world = player.world;
		counterMap = new TreeMap<String, Integer>(); // Tree Maps are ordered alphabetically by key
		mobsMap = new TreeMap<String, Entity>();
		playerList = new ArrayList<EntityPlayerMP>();
		
		// get all living mobs and register them to the mobsMap
		ForgeRegistries.ENTITIES.getValuesCollection().stream().forEach(s -> {
			Entity tmpEntity = EntityList.createEntityByIDFromName(s.getRegistryName(), world);
			if (tmpEntity.isCreatureType(EnumCreatureType.CREATURE, false) ||
					tmpEntity.isCreatureType(EnumCreatureType.MONSTER, false)) {
				mobsMap.put(s.getName(), tmpEntity);
				counterMap.put(s.getName(), 0);
			}
			tmpEntity.setDead();
		});
		if (!player.getEntityWorld().isRemote) {
			playerList.add(player);
		}
	}
	
	public static void PrintLoadedToConsole() 
	{
		System.out.println("-----------------------------------");
		System.out.println("L O A D E D   E N T I T I E S :");
		for (Entry<String, Entity> entry : mobsMap.entrySet()) {
			String key = entry.getKey();			
			System.out.println(key);
		}
		if (playerList == null || playerList.isEmpty()) {
			if (playerList == null) {
				System.out.println("!!! PLAYERLIST IS NULL !!!");
			}
			else {
				System.out.println("!!! PLAYERLIST IS EMPTY !!!");
			}
		}
		else {
			System.out.println();
			System.out.println("O N L I N E   P L A Y E R S :");
			for (EntityPlayerMP playerMP : playerList) {
				String name = playerMP.getName();
				UUID uuid = playerMP.getUniqueID();
				System.out.println(name + " - " + uuid);
			}
		}
		System.out.println("-----------------------------------");
	}
	
	public static void SaveLoadedToFile() 
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

	public static void UpdateGameMaster(Entity source, Entity target) 
	{
		if (source instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) source;
			if (!player.getEntityWorld().isRemote) {
				IncreaseMobCountOf(target);
			}
		}
	}
	
	public static void PrintCounterMapInGame(EntityPlayer player)
	{
		for (Entry<String, Integer> entry : counterMap.entrySet()) {
			String mobName = entry.getKey();
			int mobCounter = entry.getValue();
			
			player.sendMessage(new TextComponentString(mobName + ": " + mobCounter));
			
		}
	}
	
	public static void IncreaseMobCountOf(Entity mob)
	{
		int preKillCount = counterMap.get(mob.getName());
		counterMap.remove(mob.getName());
		counterMap.put(mob.getName(), preKillCount + 1);
	}


	public static void PrintTargetCounterInGameToPlayer(EntityPlayer player, Entity target) 
	{	
		player.sendMessage(new TextComponentString(target.getName() + ": " + counterMap.get(target.getName())));
	}
	
}

//	public static void Update(Entity source, Entity target) {
//		if (source instanceof EntityPlayer) {
//
//			EntityPlayer player = (EntityPlayer) source;
//			
//			if (!player.getEntityWorld().isRemote) {
//
//				System.out.println("Killed a " + target.getName());
//				
//				switch (target.getName()) {
//				case "Spider":
//					GameMaster.AddSpiderCount();
//					switch (GameMaster.spiderCount) {
//					case 1:
//						GameMaster.bnsDmgSpider += 5f;
//						break;
//					case 3:
//						GameMaster.bnsDmgSpider += 10f;
//						break;
//					case 5:
//						GameMaster.bnsDmgSpider += 15f;
//						break;
//					default:
//						break;
//					}
//					break;
//				case "Skeleton":
//					GameMaster.AddSkeletonCount();
//					switch (GameMaster.skeletonCount) {
//					case 1:
//						GameMaster.bnsDmgSkeleton += 5f;
//						break;
//					case 3:
//						GameMaster.bnsDmgSkeleton += 10f;
//						break;
//					case 5:
//						GameMaster.bnsDmgSkeleton += 15f;
//						break;
//					default:
//						break;
//					}
//					break;
//				case "Zombie":
//					GameMaster.AddZombieCount();
//					switch (GameMaster.zombieCount) {
//					case 1:
//						GameMaster.bnsDmgZombie += 5f;
//						break;
//					case 3:
//						GameMaster.bnsDmgZombie += 10f;
//						break;
//					case 5:
//						GameMaster.bnsDmgZombie += 15f;
//						break;
//					default:
//						break;
//					}
//					break;
//				default:
//					GameMaster.AddOthersCount();
//					switch (GameMaster.othersCount) {
//					case 1:
//						GameMaster.bnsDmgOthers += 5f;
//						break;
//					case 3:
//						GameMaster.bnsDmgOthers += 10f;
//						break;
//					case 5:
//						GameMaster.bnsDmgOthers += 15f;
//						break;
//					default:
//						break;
//					}
//					break;
//				}
//
//				if (GameMaster.overallCount >= 5) {
//					GameMaster.getSpeedFromAttacking = true;
//				}
//				PrintStats(player);
//			}
//		}
//	}
//
//
//	public static float getDmgBonus(Entity target) {
//		switch (target.getName()) {
//		case "Spider":
//			return GameMaster.bnsDmgSpider;
//		case "Skeleton":
//			return GameMaster.bnsDmgSkeleton;
//		case "Zombie":
//			return GameMaster.bnsDmgZombie;
//		default:
//			return GameMaster.bnsDmgOthers;
//		}
//	}
//	
//	public static void PrintStats(EntityPlayer player) {
//		player.sendMessage(new TextComponentString("-----------------------------------------"));
//		player.sendMessage(new TextComponentString("Killed spiders: " + GameMaster.spiderCount + " (+ " + GameMaster.bnsDmgSpider + " attack)"));
//		player.sendMessage(new TextComponentString("Killed skeletons: " + GameMaster.skeletonCount + " (+ " + GameMaster.bnsDmgSkeleton + " attack)"));
//		player.sendMessage(new TextComponentString("Killed zombies: " + GameMaster.zombieCount + " (+ " + GameMaster.bnsDmgZombie+ " attack)"));
//		player.sendMessage(new TextComponentString("Killed other mobs: " + GameMaster.othersCount + " (+ " + GameMaster.bnsDmgOthers + " attack)"));
//		player.sendMessage(new TextComponentString("Overall killed mobs: " + GameMaster.overallCount));
//		player.sendMessage(new TextComponentString(""));
//		player.sendMessage(new TextComponentString("Learned abilities:"));
//		if (GameMaster.getSpeedFromAttacking) {
//			player.sendMessage(new TextComponentString("  - Speed from attacking"));
//		}
//		player.sendMessage(new TextComponentString("-----------------------------------------"));
//	}
