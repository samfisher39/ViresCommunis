package com.samfisher39.virescommunis.faction;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FactionTickHandler {

	public static FactionTickHandler instance = new FactionTickHandler();
	public static int guiTick = -1;

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
    	
        if (evt.phase == TickEvent.Phase.START) {
            return;
        }
        World world = evt.world;
        WorldFaction.get(world).tick(world);
    }
	
}
