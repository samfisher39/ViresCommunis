package com.samfisher39.virescommunis.util;

import com.samfisher39.virescommunis.init.ViresCommunisItems;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ModelRegistryHandler {
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) 
	{
		registerModel(ViresCommunisItems.FACTIONGUIOPENER_ITEM);
	}
	
	public static void registerModel(Item item) 
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
}
