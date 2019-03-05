package com.samfisher39.virescommunis.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.Attributes.Name;

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
import scala.tools.nsc.backend.icode.Primitives.ArrayLength;

@Mod.EventBusSubscriber
public class EventHandler {
	

	@SubscribeEvent
	public static void OnPlayerJoin(PlayerLoggedInEvent event) throws ClassNotFoundException, IOException
	{		
		EntityPlayer player = event.player;	
		EntityPlayerMP playerMP = (EntityPlayerMP) player;
		if (player.isServerWorld()) {
			if (FactionMaster.IsPartOfFaction(playerMP)) {
				Faction faction = FactionMaster.GetFactionOfPlayer(playerMP);
				faction.membersNameList.add(playerMP.getName());
				if (faction.IsAdmin(playerMP)) {
					faction.adminsNameList.add(playerMP.getName());
				}
				//faction.controller.UpdateBnsDamage();
			} else {
				FactionMaster.factionList.put(player.getName().concat("'s Faction!"), new Faction(event.player.getUniqueID()));
				FactionMaster.PrintFactionsToFile();
			}
		}
	}
	
	@SubscribeEvent
	public static void OnPlayerLeave(PlayerLoggedOutEvent event) throws IOException
	{
	}

	@SubscribeEvent
	public static void OnEntityKilled(LivingDeathEvent event) 
	{	
		if (event.getSource().getTrueSource() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();
			Faction faction = FactionMaster.GetFactionOfPlayer(player);
			Entity target = event.getEntity();
			faction.controller.UpdateGameMaster(player, target);
			faction.controller.PrintTargetCounterInGameToPlayer(player, target);
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
			int bonusDamage = 0;
			
			for (Map.Entry<String, ArrayList<Integer>> skillEntry : faction.controller.skillMap.entrySet()) {
				String mobName = skillEntry.getKey().substring(0, skillEntry.getKey().length()-6);
				Boolean isSameMob = mobName.equalsIgnoreCase(target.getName()) ? true : false;
				
				int damage = skillEntry.getValue().get(0);
				boolean enabled = skillEntry.getValue().get(2) == 1 ? true : false;
				
				if (enabled && isSameMob || enabled && mobName.equalsIgnoreCase("all") ) {
					player.sendMessage(new TextComponentString(" ===== Bonus dmg to " + mobName + ": +" + damage + " ===== "));
					bonusDamage += damage;
				}
			}
			
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), bonusDamage);
			player.sendMessage(new TextComponentString("+" + bonusDamage + " damage to " + target.getName()));
		}
	}	
}
