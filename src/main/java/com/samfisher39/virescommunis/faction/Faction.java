package com.samfisher39.virescommunis.faction;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Faction {
	
	private String name;
	private ArrayList<UUID> membersUUIDList = new ArrayList<UUID>();
	private ArrayList<UUID> adminsUUIDList = new ArrayList<UUID>();
	public ArrayList<String> membersNameList = new ArrayList<String>();
	public ArrayList<String> adminsNameList = new ArrayList<String>();
	public Controller controller;
	public UUID adminUUID;
	
	public Faction(String nameString, UUID adminUUID){
		this.setName(nameString);
		this.membersUUIDList.add(adminUUID);
		this.adminsUUIDList.add(adminUUID);
		this.adminUUID = adminUUID;
		this.controller = new Controller(adminUUID);
	}
	
	public Faction(UUID adminUUID)
	{
		this(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getPlayerEntityByUUID(adminUUID).getName().concat("'s Faction!"), adminUUID);
	}
	
	public EntityPlayer GetAdmin() {
		if (Minecraft.getMinecraft().world.isRemote) {
			System.out.println("WORLD IS REMOTE");
			if (this.adminsNameList.isEmpty()) {
				System.out.println("adminListEmptyTriggered");
				return null;
			}
			return Minecraft.getMinecraft().world.getPlayerEntityByUUID(this.adminUUID);
		}
		System.out.println("WORLD IS NOT REMOTE");
		return null;
	}
	
	public ArrayList<String> GetMembers()
	{
		return membersNameList;
	}
	
	public ArrayList<String> GetAdmins()
	{
		return adminsNameList;
	}
	
	public boolean isEmpty()
	{
		if (membersNameList.isEmpty() || adminsNameList.isEmpty()) {
			return true;
		}
		return false;
	}

	public String getName() 
	{
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
		this.adminsUUIDList.remove(uuid);
		this.membersUUIDList.remove(uuid);
		this.membersNameList.remove(Minecraft.getMinecraft().world.getPlayerEntityByUUID(uuid).getName());
		this.adminsNameList.remove(Minecraft.getMinecraft().world.getPlayerEntityByUUID(uuid).getName());
	}
	
	public void AddPlayer(EntityPlayerMP player)
	{
		UUID uuid = player.getUniqueID();
		AddPlayer(uuid);
	}
	
	public void AddPlayer(UUID uuid)
	{
		EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
		if (!this.ContainsPlayer(player)) {
			this.adminsUUIDList.add(uuid);
			this.membersUUIDList.add(uuid);
			this.membersNameList.add(player.getName());
			this.adminsNameList.add(player.getName());	
		}
	}

	public boolean ContainsPlayer(EntityPlayerMP player)
	{
		if (membersUUIDList.contains(player.getUniqueID())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean ContainsPlayer(EntityPlayer player)
	{
		if (membersUUIDList.contains(player.getUniqueID())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean IsAdmin(EntityPlayerMP player)
	{
		if (adminsUUIDList.contains(player.getUniqueID())) {
			return true;
		} else {
			return false;
		}
	}
}
