package com.samfisher39.virescommunis.events;

import com.samfisher39.virescommunis.faction.GameMaster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@EventBusSubscriber
public class EventMaster {
	
//	static GameMaster gameMasterE = ViresCommunis.gameMaster;
	static GameMaster gameMasterE;

	@SubscribeEvent
	public static void onEntityKilled(LivingDeathEvent event) 
	{	
		if (event.getSource().getTrueSource() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
			
			System.out.println("here");
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			Entity target = event.getEntity();
			GameMaster.UpdateGameMaster(player, target);
			GameMaster.PrintTargetCounterInGameToPlayer(player, target);	
		}
	}
	
	@SubscribeEvent
	public static void onEntityAttacked(AttackEntityEvent event) {
		if (!event.getEntity().getEntityWorld().isRemote) {
			
//			if (GameMaster.getSpeedFromAttacking) {
//				attacker.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 100, 2));
//			}
//			
//			Entity target = event.getTarget();
//			EntityLiving targetLiving = (EntityLiving) target;
//			float healthPreHit = targetLiving.getHealth();
//			
//			float dmg = GameMaster.getDmgBonus(target);
//			target.attackEntityFrom(DamageSource.causePlayerDamage(attacker), dmg);
//			
//			float healthPostHit = targetLiving.getHealth();
//			float xtrDmg = healthPreHit - healthPostHit;
//			
//			System.out.println("Inflicted " + xtrDmg + "/" + dmg + " extra damage on " + target.getName());
			
			GameMaster.SaveLoadedToFile();
			GameMaster.PrintLoadedToConsole();
		}
	}
	
//	@SubscribeEvent
//	public static void onWorldLoad(WorldEvent.Load event) {
//		gameMasterE = new GameMaster(event.getWorld());
//    	
//	}
	
	@SubscribeEvent
	public static void onPlayerJoin(PlayerLoggedInEvent event) {
		if (event.player.isServerWorld()) {
			gameMasterE = new GameMaster((EntityPlayerMP) event.player);
		}
	}
}
