package com.samfisher39.virescommunis.item;


import com.samfisher39.virescommunis.ViresCommunis;
import com.samfisher39.virescommunis.client.gui.FactionGui;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemFactionGuiOpener extends Item {
	
	public ItemFactionGuiOpener(String unloclizedName, String registryName) {

		
		setUnlocalizedName(ViresCommunis.MODID + "." + unloclizedName);
		setRegistryName(registryName);
		setCreativeTab(CreativeTabs.MISC);
		
	}
	
	

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			
		} else {
			playerIn.sendMessage(new TextComponentString("opened gui"));
			Minecraft.getMinecraft().displayGuiScreen(new FactionGui(playerIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	
	
}
