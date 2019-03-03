package com.samfisher39.virescommunis.commands;

import java.util.ArrayList;
import java.util.List;

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

public class CommandOpenFaction implements ICommand{
	
	private final List<String> aliases;
	
	public CommandOpenFaction()
	{
		aliases = new ArrayList<String>();
		aliases.add("openfaction");
		aliases.add("of");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "openfaction";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "openfaction <factionname>";
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
			
			if (args.length == 0 | args.length >= 2) {
				sender.sendMessage(new TextComponentString("Invalid Argument!"));
				return;
			}
			
			EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
			
			if (!FactionMaster.factionList.containsKey(args[0])) {
				if (FactionMaster.GetFactionOfPlayer(player) != null) {
					Faction faction = FactionMaster.GetFactionOfPlayer(player);
					faction.KickPlayer(player);
					faction.controller.playerList.remove(player);
				}
				Faction newFaction = new Faction(args[0], player.getUniqueID());
				newFaction.membersNameList.add(player.getName());
				newFaction.adminsNameList.add(player.getName());
				FactionMaster.factionList.put(args[0], newFaction);
			} else {
				sender.sendMessage(new TextComponentString("Faction already exists!"));
			}
			
			FactionMaster.PrintFactionsToFile();
			
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
