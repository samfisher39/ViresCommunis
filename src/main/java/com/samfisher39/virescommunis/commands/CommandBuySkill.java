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

public class CommandBuySkill implements ICommand{
	
	private final List<String> aliases;
	
	public CommandBuySkill()
	{
		aliases = new ArrayList<String>();
		aliases.add("buyskill");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "buyskill";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "buyskill";
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
			
			if (args.length == 1) 
			{
				for (Map.Entry<String, Integer> skillPriceEntry : faction.controller.skillPriceMap.entrySet()) {
					if (args[0].equalsIgnoreCase(skillPriceEntry.getKey())) 
					{	
						if (faction.controller.getMoney() >= skillPriceEntry.getValue()) {
							faction.controller.skillMap.replace(skillPriceEntry.getKey(), true);
							player.sendMessage(new TextComponentString("Bought the skill " + skillPriceEntry.getKey()));
							return;
						} else {
							player.sendMessage(new TextComponentString("Not enough money!"));
							return;
						}
					} 
				}
				player.sendMessage(new TextComponentString("This is not an available skill!"));
				return;
			} else if (args.length == 0) {
				faction.controller.ListAvailableSkills(player);
				return;
			} else {
				player.sendMessage(new TextComponentString("Not a valid number of arguments! -> /buyskill [skill]"));
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
