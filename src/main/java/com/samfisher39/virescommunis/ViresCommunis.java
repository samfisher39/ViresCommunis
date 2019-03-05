package com.samfisher39.virescommunis;

import org.apache.logging.log4j.Logger;

import com.samfisher39.virescommunis.client.gui.RenderGuiHandler;
import com.samfisher39.virescommunis.commands.CommandBuySkill;
import com.samfisher39.virescommunis.commands.CommandFactionGUI;
import com.samfisher39.virescommunis.commands.CommandGiveMoney;
import com.samfisher39.virescommunis.commands.CommandJoinFaction;
import com.samfisher39.virescommunis.commands.CommandListBoughtSkills;
import com.samfisher39.virescommunis.commands.CommandOpenFaction;
import com.samfisher39.virescommunis.commands.CommandResetCounter;
import com.samfisher39.virescommunis.commands.CommandShowFaction;
import com.samfisher39.virescommunis.faction.FactionMaster;
import com.samfisher39.virescommunis.faction.FactionTickHandler;
import com.samfisher39.virescommunis.proxy.IProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
 
@Mod(modid = ViresCommunis.MODID, name = ViresCommunis.NAME, version = ViresCommunis.VERSION, acceptedMinecraftVersions = ViresCommunis.MC_VERSION)
public class ViresCommunis {
 
    public static final String MODID = "virescommunis";
    public static final String NAME = "Vires Communis";
    public static final String VERSION = "1.0";
    public static final String MC_VERSION = "[1.12.2]";

    public static final String CLIENT = "com.samfisher39.virescommunis.proxy.ClientProxy";
    public static final String SERVER = "com.samfisher39.virescommunis.proxy.ServerProxy";
    
    public FactionMaster factionMaster = new FactionMaster();
        
    @SidedProxy(clientSide = ViresCommunis.CLIENT, serverSide = ViresCommunis.SERVER)
    public static IProxy proxy;
 
    public static Logger logger;
 
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new CommandOpenFaction());
    	event.registerServerCommand(new CommandShowFaction());
    	event.registerServerCommand(new CommandJoinFaction());
    	event.registerServerCommand(new CommandResetCounter());
    	event.registerServerCommand(new CommandBuySkill());
    	event.registerServerCommand(new CommandListBoughtSkills());
    	event.registerServerCommand(new CommandGiveMoney());
    	event.registerServerCommand(new CommandFactionGUI());
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        
        MinecraftForge.EVENT_BUS.register(FactionTickHandler.instance);
    }
 
    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info(ViresCommunis.NAME + " salvete!");
        proxy.init(event);
    }
 
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    	MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
    }
 
}
