package com.samfisher39.virescommunis.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.samfisher39.virescommunis.client.gui.FactionGui;
import com.samfisher39.virescommunis.faction.Faction;
import com.samfisher39.virescommunis.faction.FactionMaster;
import com.samfisher39.virescommunis.faction.FactionTickHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandFactionGUI implements ICommand{
	
	private final List<String> aliases;
	
	public CommandFactionGUI()
	{
		aliases = new ArrayList<String>();
		aliases.add("factiongui");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "factiongui";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "factiongui";
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
			FactionTickHandler.guiTick = 1;
			
		} else {
			System.out.println("Currently on Server side");
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
