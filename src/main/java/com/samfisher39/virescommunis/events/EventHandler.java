package com.samfisher39.virescommunis.events;

import java.io.IOException;

import com.samfisher39.virescommunis.faction.Faction;
import com.samfisher39.virescommunis.faction.FactionMaster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
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
				Faction faction = FactionMaster.GetFactionOfPlayer(playerMP);
				faction.membersNameList.add(playerMP.getName());
				if (faction.IsAdmin(playerMP)) {
					faction.adminsNameList.add(playerMP.getName());
				}
				faction.gameMaster.UpdateBnsDamage();
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
			
			EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
			Faction faction = FactionMaster.GetFactionOfPlayer(player);
			Entity target = event.getEntity();
			faction.gameMaster.UpdateGameMaster(player, target);
			faction.gameMaster.PrintTargetCounterInGameToPlayer(player, target);
			
		}
	}
	
	@SubscribeEvent
	public static void OnEntityAttacked(AttackEntityEvent event) 
	{
		if (!event.getEntity().getEntityWorld().isRemote) 
		{
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
			Faction faction = FactionMaster.GetFactionOfPlayer(player);
			Entity target = event.getTarget();
			int bonusDamage = faction.gameMaster.GetBnsDamage(target);
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), bonusDamage);
			player.sendMessage(new TextComponentString("Caused +" + bonusDamage + " extra damage to " + target.getName()));
		}
	}	
}
