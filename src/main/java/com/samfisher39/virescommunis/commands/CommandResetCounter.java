package com.samfisher39.virescommunis.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.samfisher39.virescommunis.faction.Faction;
import com.samfisher39.virescommunis.faction.FactionMaster;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandResetCounter implements ICommand{
	
	private final List<String> aliases;
	
	public CommandResetCounter()
	{
		aliases = new ArrayList<String>();
		aliases.add("VC_resetcounter");
		aliases.add("VC_rc");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "VC_resetcounter";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "VC_resetcounter";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		World world = sender.getEntityWorld();
		
		if (world.isRemote) {
			System.out.println("Currently on Client side");
		} else {
			System.out.println("Currently on Server side");
			
			EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
			
			Faction faction = FactionMaster.GetFactionOfPlayer(player);
			
			Map<String, Integer> map = faction.gameMaster.counterMap;
			Map<String, Integer> newMap = new TreeMap<String, Integer>();
			for (Map.Entry<String, Integer> counterEntry : map.entrySet()) {
				newMap.put(counterEntry.getKey(), 0);
			}
			faction.gameMaster.counterMap = newMap;
			faction.gameMaster.UpdateBnsDamage();
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	
	
}
