package com.samfisher39.virescommunis.events;

import com.samfisher39.virescommunis.master.GameMaster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventMaster {
	
	GameMaster gameMaster = new GameMaster();
	
	@SubscribeEvent
	public static void EntityKilled(LivingDeathEvent event) {
		
		GameMaster.Update(event.getSource().getTrueSource(), event.getEntity());
	}
	
	@SubscribeEvent
	public static void DoExtraDamage(AttackEntityEvent event) {
		if (event.getEntity() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
			
			EntityPlayer attacker = (EntityPlayer)event.getEntity();
			if (GameMaster.getSpeedFromAttacking) {
				attacker.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 100, 2));
			}
			
			Entity target = event.getTarget();
			EntityLiving targetLiving = (EntityLiving) target;
			float healthPreHit = targetLiving.getHealth();
			
			float dmg = GameMaster.getDmgBonus(target);
			target.attackEntityFrom(DamageSource.causePlayerDamage(attacker), dmg);
			
			float healthPostHit = targetLiving.getHealth();
			float xtrDmg = healthPreHit - healthPostHit;
			
			System.out.println("Inflicted " + xtrDmg + "/" + dmg + " extra damage on " + target.getName());
		}
	}
}
