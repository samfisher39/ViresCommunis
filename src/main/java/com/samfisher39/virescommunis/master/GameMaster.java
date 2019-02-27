package com.samfisher39.virescommunis.master;

import java.sql.Ref;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class GameMaster{
	
	static int spiderCount;
	static int skeletonCount;
	static int zombieCount;
	static int othersCount;
	static int overallCount;

	static float bnsDmgSpider;
	static float bnsDmgSkeleton;
	static float bnsDmgZombie;
	static float bnsDmgOthers;
	
	public static boolean getSpeedFromAttacking;
	
	public GameMaster() {
		GameMaster.spiderCount = 0;
		GameMaster.skeletonCount = 0;
		GameMaster.zombieCount = 0;
		GameMaster.othersCount = 0;
		GameMaster.overallCount = 0;

		GameMaster.bnsDmgSpider = 0f;
		GameMaster.bnsDmgSkeleton = 0f;
		GameMaster.bnsDmgZombie = 0f;
		GameMaster.bnsDmgOthers = 0f;
		
		GameMaster.getSpeedFromAttacking = false;
	}
	
	public static void AddSpiderCount() {
		GameMaster.spiderCount++;
		GameMaster.overallCount++;
	}
	
	public static void AddSkeletonCount() {
		GameMaster.skeletonCount++;
		GameMaster.overallCount++;
	}
	
	public static void AddZombieCount() {
		GameMaster.zombieCount++;
		GameMaster.overallCount++;
	}
	
	public static void AddOthersCount() {
		GameMaster.othersCount++;
		GameMaster.overallCount++;
	}
	
	public static void Update(Entity source, Entity target) {
		if (source instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) source;
			
			if (!player.getEntityWorld().isRemote) {

				System.out.println("Killed a " + target.getName());
				
				switch (target.getName()) {
				case "Spider":
					GameMaster.AddSpiderCount();
					switch (GameMaster.spiderCount) {
					case 1:
						GameMaster.bnsDmgSpider += 5f;
						break;
					case 3:
						GameMaster.bnsDmgSpider += 10f;
						break;
					case 5:
						GameMaster.bnsDmgSpider += 15f;
						break;
					default:
						break;
					}
					break;
				case "Skeleton":
					GameMaster.AddSkeletonCount();
					switch (GameMaster.skeletonCount) {
					case 1:
						GameMaster.bnsDmgSkeleton += 5f;
						break;
					case 3:
						GameMaster.bnsDmgSkeleton += 10f;
						break;
					case 5:
						GameMaster.bnsDmgSkeleton += 15f;
						break;
					default:
						break;
					}
					break;
				case "Zombie":
					GameMaster.AddZombieCount();
					switch (GameMaster.zombieCount) {
					case 1:
						GameMaster.bnsDmgZombie += 5f;
						break;
					case 3:
						GameMaster.bnsDmgZombie += 10f;
						break;
					case 5:
						GameMaster.bnsDmgZombie += 15f;
						break;
					default:
						break;
					}
					break;
				default:
					GameMaster.AddOthersCount();
					switch (GameMaster.othersCount) {
					case 1:
						GameMaster.bnsDmgOthers += 5f;
						break;
					case 3:
						GameMaster.bnsDmgOthers += 10f;
						break;
					case 5:
						GameMaster.bnsDmgOthers += 15f;
						break;
					default:
						break;
					}
					break;
				}

				if (GameMaster.overallCount >= 5) {
					GameMaster.getSpeedFromAttacking = true;
				}
				PrintStats(player);
			}
		}
	}


	public static float getDmgBonus(Entity target) {
		switch (target.getName()) {
		case "Spider":
			return GameMaster.bnsDmgSpider;
		case "Skeleton":
			return GameMaster.bnsDmgSkeleton;
		case "Zombie":
			return GameMaster.bnsDmgZombie;
		default:
			return GameMaster.bnsDmgOthers;
		}
	}
	
	public static void PrintStats(EntityPlayer player) {
		player.sendMessage(new TextComponentString("-----------------------------------------"));
		player.sendMessage(new TextComponentString("Killed spiders: " + GameMaster.spiderCount + " (+ " + GameMaster.bnsDmgSpider + " attack)"));
		player.sendMessage(new TextComponentString("Killed skeletons: " + GameMaster.skeletonCount + " (+ " + GameMaster.bnsDmgSkeleton + " attack)"));
		player.sendMessage(new TextComponentString("Killed zombies: " + GameMaster.zombieCount + " (+ " + GameMaster.bnsDmgZombie+ " attack)"));
		player.sendMessage(new TextComponentString("Killed other mobs: " + GameMaster.othersCount + " (+ " + GameMaster.bnsDmgOthers + " attack)"));
		player.sendMessage(new TextComponentString("Overall killed mobs: " + GameMaster.overallCount));
		player.sendMessage(new TextComponentString(""));
		player.sendMessage(new TextComponentString("Learned abilities:"));
		if (GameMaster.getSpeedFromAttacking) {
			player.sendMessage(new TextComponentString("  - Speed from attacking"));
		}
		player.sendMessage(new TextComponentString("-----------------------------------------"));
	}
}
