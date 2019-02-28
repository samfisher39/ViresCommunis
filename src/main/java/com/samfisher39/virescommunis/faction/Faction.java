package com.samfisher39.virescommunis.faction;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

public class Faction {
	
	private String name;
	private ArrayList<UUID> membersUUIDList = new ArrayList<UUID>();
	private ArrayList<String> membersNameList = new ArrayList<String>();
	private ArrayList<UUID> adminUUIDList = new ArrayList<UUID>();
	public FactionController gameMaster;
	
	public Faction(String nameString, EntityPlayerMP admin){
		this.setName(nameString);
		this.membersUUIDList.add(admin.getUniqueID());
		this.adminUUIDList.add(admin.getUniqueID());
		this.membersNameList.add(admin.getName());
		this.gameMaster = new FactionController(admin);
	}
	
	public Faction(EntityPlayerMP admin){
		this(admin.getName().concat("'s Faction!"), admin);
	}
	
	public ArrayList<String> GetMembers()
	{
		return membersNameList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void KickPlayer(EntityPlayerMP player)
	{
		UUID uuid = player.getUniqueID();
		KickPlayer(uuid);
	}
	
	public void KickPlayer(UUID uuid)
	{
		this.adminUUIDList.remove(uuid);
		this.membersUUIDList.remove(uuid);
		this.membersNameList.remove(Minecraft.getMinecraft().world.getPlayerEntityByUUID(uuid).getName());
	}
	
	public boolean ContainsPlayer(EntityPlayerMP player)
	{
		if (membersUUIDList.contains(player.getUniqueID())) {
			return true;
		} else {
			return false;
		}
	}
}
