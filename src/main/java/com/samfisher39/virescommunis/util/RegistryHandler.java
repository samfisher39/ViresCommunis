package com.samfisher39.virescommunis.util;

import com.samfisher39.virescommunis.item.ItemFactionGuiOpener;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {
	
	@SubscribeEvent
	public static void registerBlocks(Register<Block> event) {
		
		final Block[] blocks = {
				
		};
		
		event.getRegistry().registerAll(blocks);
		
	}
	
	@SubscribeEvent
	public static void registerItems(Register<Item> event) {
		
		final Item[] items = {
				new ItemFactionGuiOpener("itemFactionGuiOpener", "factionguiopener_item")
		};
		
		final Item[] itemBlocks = {
				
		};
		
		event.getRegistry().registerAll(items);
		event.getRegistry().registerAll(itemBlocks);
		
	}
	
}
