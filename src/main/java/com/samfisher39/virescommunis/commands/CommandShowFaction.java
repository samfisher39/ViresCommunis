package com.samfisher39.virescommunis.commands;

import java.util.ArrayList;
import java.util.List;

import com.samfisher39.virescommunis.faction.FactionMaster;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandShowFaction implements ICommand{
	
	private final List<String> aliases;
	
	public CommandShowFaction()
	{
		aliases = new ArrayList<String>();
		aliases.add("showfaction");
		aliases.add("sf");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "showfaction";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "showfaction";
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
			
			if (args.length == 0) {
				if (FactionMaster.GetFactionOfPlayer(player) == null) {
					sender.sendMessage(new TextComponentString("User is not in a faction!"));
				} else {
					sender.sendMessage(new TextComponentString(FactionMaster.GetFactionOfPlayer(player).getName()));
				return;
				}
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
