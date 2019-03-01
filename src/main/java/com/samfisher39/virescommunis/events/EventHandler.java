package com.samfisher39.virescommunis.events;

import java.io.File;
import java.io.IOException;

import com.samfisher39.virescommunis.faction.Faction;
import com.samfisher39.virescommunis.faction.GameMaster;
import com.samfisher39.virescommunis.faction.FactionMaster;
import com.samfisher39.virescommunis.faction.WorldFaction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

@Mod.EventBusSubscriber
public class EventHandler {
	

	@SubscribeEvent
	public static void OnPlayerJoin(PlayerLoggedInEvent event) throws ClassNotFoundException, IOException
	{
		System.out.println(event.player.getName() + "   J O I N E D ! ! ! ! ! ! !");
		
		EntityPlayer player = event.player;	
		EntityPlayerMP playerMP = (EntityPlayerMP) player;
		if (player.isServerWorld()) {
			if (FactionMaster.IsPartOfFaction(playerMP)) {
				
			} else {
				FactionMaster.factionList.put(player.getName().concat("'s Faction!"), new Faction(event.player.getUniqueID()));
				FactionMaster.PrintFactionsToFile();
			}
		}
	}
	
	@SubscribeEvent
	public static void OnPlayerLeave(PlayerLoggedOutEvent event) throws IOException
	{
		System.out.println(event.player.getName() + "   L E F T ! ! ! ! ! ! !");
	}

	@SubscribeEvent
	public static void OnEntityKilled(LivingDeathEvent event) 
	{	
		if (event.getSource().getTrueSource() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
			
			WorldFaction.get(event.getEntity().getEntityWorld());
			System.out.println("here");
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			Entity target = event.getEntity();
			GameMaster.UpdateGameMaster(player, target);
			GameMaster.PrintTargetCounterInGameToPlayer(player, target);	
		}
	}
	
	@SubscribeEvent
	public static void OnEntityAttacked(AttackEntityEvent event) 
	{
		if (!event.getEntity().getEntityWorld().isRemote) 
		{
			//FactionController.SaveLoadedToFile();
			//FactionController.PrintLoadedToConsole();
		}
	}

//	@SubscribeEvent
//	public void OnEntityKilled(LivingDeathEvent event) 
//	{	
//		if (event.getSource().getTrueSource() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
//			
//			System.out.println("here");
//			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
//			Entity target = event.getEntity();
//			ViresCommunis.factionMaster.UpdateGameMaster(player, target);
//			FactionController.PrintTargetCounterInGameToPlayer(player, target);	
//		}
//	}

//	
//	@SubscribeEvent
//	public static void OnPlayerJoin(PlayerLoggedInEvent event) 
//	{
//		EntityPlayer player = event.player;	
//		EntityPlayerMP playerMP = (EntityPlayerMP) player;
//		if (player.isServerWorld()) {
//			if (FactionMaster.IsPartOfFaction(playerMP)) {
//				
//			} else {
//				FactionMaster.factionList.put(player.getName().concat("'s Faction!"), new Faction((EntityPlayerMP) event.player));
//				FactionMaster.PrintFactionsToFile();
//			}
//		}
//	}
	
	
	
}
