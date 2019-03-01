package com.samfisher39.virescommunis.faction;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.samfisher39.virescommunis.ViresCommunis;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldFaction extends WorldSavedData {

	private static final TreeMap<String, Faction> factionData = FactionMaster.factionList;
	private static final String NAME = ViresCommunis.MODID + "_factionData";
	private int ticker = 40;
	
	public WorldFaction() {
		super(NAME);
	}
	
	public WorldFaction(String name) {
		super(name);
		markDirty();
	}
	
	public void tick(World world) {
        ticker--;
        if (ticker > 0) {
            return;
        }
        ticker = 40;
        System.out.println(" TTTTT IIIII CCCCC KKKKK !!!!!");
        System.out.println("PLAYERLIST: " + FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getCurrentPlayerCount());
        markDirty();
    }
	
	public static WorldFaction get(World world) {
		  MapStorage storage = world.getMapStorage();
		  WorldFaction instance = (WorldFaction) storage.getOrLoadData(WorldFaction.class, NAME);

		  if (instance == null) {
		    instance = new WorldFaction();
		    storage.setData(NAME, instance);
		  }
		  return instance;
		}

	@Override
	public void readFromNBT(NBTTagCompound nbt) { // load previously stored data
		System.out.println(" READ FROM NBT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		NBTTagList list = nbt.getTagList("factionData" , Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound factionNBT = list.getCompoundTagAt(i);
			
			String name = factionNBT.getString("name");
			if (factionNBT.getUniqueId("adminUUID") == null) {
				System.out.println("UUID IS NULL");
			}
			UUID uuid = factionNBT.getUniqueId("adminUUID");
			System.out.println("----------------" + name + " + " + uuid + "----------------");
			Faction faction = new Faction(name, uuid);
			factionData.put(name, faction);
			
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		NBTTagList list = new NBTTagList();
		for (Map.Entry<String, Faction> entry : factionData.entrySet()) {
			NBTTagCompound factionNBT = new NBTTagCompound();
			String name = entry.getKey();
			Faction faction = entry.getValue();
			factionNBT.setString("name", name);
//			if (faction.GetAdmin() == null) {
//				System.out.println("ADMIN IS NULL!!!!");
//			} else {
//				System.out.println("ADMIN IS NOT NULL!!!!");
//			}
			factionNBT.setUniqueId("adminUUID", faction.GetAdmin().getUniqueID());
			list.appendTag(factionNBT);
		}
		
		compound.setTag("factionData", list);
		System.out.println(" WRITE TO NBT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return compound;
	}
	
	
}
