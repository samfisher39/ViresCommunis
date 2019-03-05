package com.samfisher39.virescommunis.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.samfisher39.virescommunis.faction.Faction;
import com.samfisher39.virescommunis.faction.FactionMaster;

import io.netty.handler.codec.AsciiHeadersEncoder.NewlineType;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CommandListBoughtSkills implements ICommand{
	
	private final List<String> aliases;
	
	public CommandListBoughtSkills()
	{
		aliases = new ArrayList<String>();
		aliases.add("listskills");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "listskills";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "listskills";
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
			if (args.length == 0) {
				EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
				Faction faction = FactionMaster.GetFactionOfPlayer(player);
				for (Entry<String, ArrayList<Integer>> skillEntry : faction.controller.skillMap.entrySet()) {
					ITextComponent msg1 = new TextComponentString(skillEntry.getKey()+ ": ");
					ITextComponent msg2;
					if (skillEntry.getValue().get(2) == 1) {
						msg2 = new TextComponentString("activated");
						msg2.setStyle(new Style().setColor(TextFormatting.GREEN));
					} else {
						msg2 = new TextComponentString("deactivated");
						msg2.setStyle(new Style().setColor(TextFormatting.RED));
					}
					player.sendMessage(msg1.appendSibling(msg2));
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
