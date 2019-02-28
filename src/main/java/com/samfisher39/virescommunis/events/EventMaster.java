package com.samfisher39.virescommunis.events;

import com.samfisher39.virescommunis.faction.Faction;
import com.samfisher39.virescommunis.faction.FactionController;
import com.samfisher39.virescommunis.faction.FactionMaster;

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
	
	@SubscribeEvent
	public static void OnEntityKilled(LivingDeathEvent event) 
	{	
		if (event.getSource().getTrueSource() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
			
			System.out.println("here");
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			Entity target = event.getEntity();
			FactionController.UpdateGameMaster(player, target);
			FactionController.PrintTargetCounterInGameToPlayer(player, target);	
		}
	}
	
	@SubscribeEvent
	public static void OnEntityAttacked(AttackEntityEvent event) {
		if (!event.getEntity().getEntityWorld().isRemote) 
		{
			//FactionController.SaveLoadedToFile();
			//FactionController.PrintLoadedToConsole();
		}
	}
	
	@SubscribeEvent
	public static void OnPlayerJoin(PlayerLoggedInEvent event) 
	{
		EntityPlayer player = event.player;	
		EntityPlayerMP playerMP = (EntityPlayerMP) player;
		if (player.isServerWorld()) {
			if (FactionMaster.IsPartOfFaction(playerMP)) {
				
			} else {
				FactionMaster.factionList.put(player.getName().concat("'s Faction!"), new Faction((EntityPlayerMP) event.player));
				FactionMaster.PrintFactionsToFile();
			}
		}
	}
	
	
	
}
