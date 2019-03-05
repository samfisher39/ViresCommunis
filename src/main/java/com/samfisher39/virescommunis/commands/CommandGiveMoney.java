package com.samfisher39.virescommunis.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class CommandGiveMoney implements ICommand{
	
	private final List<String> aliases;
	
	public CommandGiveMoney()
	{
		aliases = new ArrayList<String>();
		aliases.add("givemoney");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "givemoney";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "givemoney";
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
			Faction faction;
			if (args.length == 1) 
			{
				faction = FactionMaster.GetFactionOfPlayer(player);
				faction.controller.addMoney(Integer.parseInt(args[0]));
			} else if (args.length == 2 && FactionMaster.factionList.containsKey(args[1])) {
				faction = FactionMaster.factionList.get(args[1]);
				faction.controller.addMoney(Integer.parseInt(args[0]));
			} else {
				player.sendMessage(new TextComponentString("Not a valid number of arguments! -> /givemoney amount [faction]"));
				return;
			}
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
