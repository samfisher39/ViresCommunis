package com.samfisher39.virescommunis.faction;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.samfisher39.virescommunis.ViresCommunis;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

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
	public void readFromNBT(NBTTagCompound nbt) // load previously stored data
	{ 
		NBTTagList list = nbt.getTagList("factionData" , Constants.NBT.TAG_COMPOUND);
		
		for (int i = 0; i < list.tagCount(); i++) 
		{
			NBTTagCompound factionNBT = list.getCompoundTagAt(i);
			String name = factionNBT.getString("name");
			UUID uuid = factionNBT.getUniqueId("adminUUID");
			Faction faction = new Faction(name, uuid);
			
			for (Map.Entry<String,Integer> mobCounterEntry : faction.controller.counterMap.entrySet()) 
			{	
				if (factionNBT.getInteger(mobCounterEntry.getKey()) != 0) 
				{
					mobCounterEntry.setValue(factionNBT.getInteger(mobCounterEntry.getKey()));
				}
			}
			factionData.put(name, faction);
			
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) { // store loaded data
		NBTTagList list = new NBTTagList();
		
		for (Map.Entry<String, Faction> entryFaction : factionData.entrySet()) {
			NBTTagCompound factionNBT = new NBTTagCompound();
			String name = entryFaction.getKey();
			Faction faction = entryFaction.getValue();
			factionNBT.setString("name", name);
			
			if (!faction.isEmpty()) 
			{
				factionNBT.setUniqueId("adminUUID", faction.GetAdmin().getUniqueID());
				
				for (Map.Entry<String,Integer> mobCounterEntry : faction.controller.counterMap.entrySet()) 
				{
					String mobName = mobCounterEntry.getKey();
					int mobCount = mobCounterEntry.getValue();
					factionNBT.setInteger(mobName, mobCount);
					list.appendTag(factionNBT);
				}
			}
		}
		compound.setTag("factionData", list);
		FactionMaster.PrintFactionsToFile();
		return compound;
	}
	
}
