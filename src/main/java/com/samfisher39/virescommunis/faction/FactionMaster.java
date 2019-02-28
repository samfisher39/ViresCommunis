package com.samfisher39.virescommunis.faction;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;

public class FactionMaster {
	
	public static TreeMap<String, Faction> factionList;
	
	public FactionMaster() {
		FactionMaster.factionList = new TreeMap<String, Faction>();
	}
	
	public static boolean IsPartOfFaction(EntityPlayerMP player)
	{
		if (GetFactionOfPlayer((EntityPlayerMP) player) == null) {
			return false;
		} else {
			return true;			
		}
	}
	
	public static Faction GetFactionOfPlayer(EntityPlayerMP player) 
	{
		for (Entry<String,Faction> factionEntry : factionList.entrySet()) {
			if (factionEntry.getValue().ContainsPlayer(player)) {
				return factionEntry.getValue();
			}
		}
		return null;
	}

	public static void PrintFactionsToFile() 
	{
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter("loaded_factions.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (writer != null) {
			writer.println("-----------------------------------");
			writer.println("L O A D E D   F A C T I O N S :");
			for (Entry<String,Faction> factionEntry : factionList.entrySet()) {
				writer.println(" - " + factionEntry.getValue().getName());
				ArrayList<String> member = factionEntry.getValue().GetMembers();
				for (String name : member) {
					writer.println("     # " + name);
				}
			}
			writer.println("-----------------------------------");
			writer.close();
		}
	}
	
}
