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
import net.minecraft.world.World;

public class CommandJoinFaction implements ICommand{
	
	private final List<String> aliases;
	
	public CommandJoinFaction()
	{
		aliases = new ArrayList<String>();
		aliases.add("joinfaction");
		aliases.add("jf");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "joinfaction";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "joinfaction";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		World world = sender.getEntityWorld();
		
		if (!world.isRemote) {
			
			EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
			
			if (args.length == 1) {
				
				Faction currFaction = FactionMaster.GetFactionOfPlayer(player);
				Faction nextFaction = FactionMaster.factionList.get(args[0]);
				
				currFaction.KickPlayer(player);
				nextFaction.AddPlayer(player);
				
				currFaction.controller.playerList.remove(player);
				nextFaction.controller.playerList.add(player);
				
				for (EntityPlayerMP player2 : currFaction.controller.playerList) {
					System.out.println("PreFaction " + player2.getName());
				}
				System.out.println(nextFaction.controller.playerList.size());
				int i = 0;
				for (EntityPlayerMP player3 : nextFaction.controller.playerList) {
					if (player3 == null) {
						System.out.println("player " + i + " is null");
						nextFaction.controller.playerList.remove(player3);
						break;
					}
					System.out.println("PostFaction " + player3.getName());
					i++;
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
