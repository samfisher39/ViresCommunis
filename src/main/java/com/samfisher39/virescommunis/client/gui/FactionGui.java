package com.samfisher39.virescommunis.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.samfisher39.virescommunis.ViresCommunis;
import com.samfisher39.virescommunis.faction.Faction;
import com.samfisher39.virescommunis.faction.FactionMaster;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FactionGui extends GuiScreen {
	
	final ResourceLocation texture = new ResourceLocation(ViresCommunis.MODID, "textures/gui/factiongui.png");
	int guiHeight = 256;
	int guiWidth = 256;
	int buttonOffsetX = 11; 
	int buttonWidth = 50;
	int buttonHeight = 20;
	int sliderHeight = 10;
	int centerX;
	int centerY;
	int page = 0; // 0 = main page, 1 = 
	Faction faction;
	int defaultColor = 0xDDDDDD;
	int scroll = 0;
	int maxPosY;

	// EXIT BUTTON
	GuiButton button1;
	final int button1id = 0;
	int button1offsetX = buttonOffsetX;
	int button1offsetY = guiHeight - 31;
	int button1posX;
	int button1posY;
	String button1Title = "Exit";
	
	GuiButton slider1;
	final int slider1id = 1;
	int slider1offsetX = guiWidth - 21;
	int slider1offsetY = 29;
	int slider1posX;
	int slider1posY;
	int slider1minY;
	int slider1maxY;
	String slider1Title = "";
	
	String title;
	
	public FactionGui(EntityPlayer player) {
		this.faction = FactionMaster.GetFactionOfPlayer(player);
		this.title = this.faction.getName() + " - Faction Manager";
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		centerX = (width - guiWidth) / 2;
		centerY = (height - guiHeight) / 2;
		drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
		drawCenteredString(fontRenderer, title, width/2, centerY+11, defaultColor);
		
		switch (page) {
		case 0:
			drawMainPage();
			break;

		default:
			break;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		updateButtons();
		buttonList.add(button1 = new GuiButton(button1id, button1posX, button1posY, buttonWidth, buttonHeight, button1Title));
		buttonList.add(slider1 = new GuiButton(slider1id, slider1posX, slider1posY, 10, sliderHeight, ""));
		
		super.initGui();
	}
	
	public void drawMainPage()
	{
		int currPosY = centerY + 30 - this.scroll;
		drawString(fontRenderer, "Money: " + String.valueOf(faction.controller.getMoney()), centerX + 75, currPosY, defaultColor);
		drawString(fontRenderer, "Admins:", centerX + 75, currPosY += 15, defaultColor);
		int adminCount = faction.adminsNameList.size();
		int memberCount = faction.membersNameList.size();
		for (int i = 0; i < adminCount; i++) {
			if (currPosY >= centerY + 30 && currPosY <= maxPosY) {
				drawString(fontRenderer, "- " + faction.adminsNameList.get(i), centerX + 95, currPosY += 15, defaultColor);
			}
		}
		//------------------------------------------------
		if (currPosY >= centerY + 30 && currPosY <= maxPosY) {
			drawString(fontRenderer, "Members:", centerX + 75, currPosY += 15, defaultColor);
		}
		for (int j = 0; j < memberCount; j++) {
			String playerName = faction.membersNameList.get(j);
			String playerOnline = " (";
			if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(playerName) != null) {
				playerOnline += "online)";
			} else {
				playerOnline += "offline)";
			}
			if (currPosY >= centerY + 30 && currPosY <= maxPosY) {
				drawString(fontRenderer, "- " + playerName + playerOnline, centerX + 95, currPosY += 15, defaultColor);
			}
		}
		//------------------------------------------------
		drawString(fontRenderer, "Unlocked Skills:", centerX + 75, currPosY += 15, defaultColor);
		for (Map.Entry<String, ArrayList<Integer>> skillEntry : faction.controller.skillMap.entrySet()) {
			//if (skillEntry.getValue().get(2).equals(1)) {
				if (currPosY >= centerY + 30 && currPosY <= maxPosY) {
					drawString(fontRenderer, "- " + skillEntry.getKey(), centerX + 95, currPosY += 15, defaultColor);
				}
			//}
		}
		
		
		
	}
	
	public void updateButtons()
	{
		centerX = (width - guiWidth) / 2;
		centerY = (height - guiHeight) / 2;
		button1posX = centerX + button1offsetX;
		button1posY = centerY + button1offsetY;
		slider1posX = centerX + slider1offsetX;
		slider1posY = centerY + slider1offsetY;
		slider1minY = slider1posY;
		slider1maxY = slider1posY + 200;
		maxPosY = centerY + 220;
	}
	
	public void exitGui()
	{
		mc.displayGuiScreen(null);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		switch (button.id) {
		case button1id:
			exitGui();;
			break;

		default:
			break;
		}
		updateButtons();
		super.actionPerformed(button);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}
}