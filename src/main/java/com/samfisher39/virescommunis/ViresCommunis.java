package com.samfisher39.virescommunis;

import org.apache.logging.log4j.Logger;

import com.samfisher39.virescommunis.master.GameMaster;
import com.samfisher39.virescommunis.proxy.IProxy;
 
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
 
@Mod(modid = ViresCommunis.MODID, name = ViresCommunis.NAME, version = ViresCommunis.VERSION, acceptedMinecraftVersions = ViresCommunis.MC_VERSION)
public class ViresCommunis {
 
    public static final String MODID = "virescommunis";
    public static final String NAME = "Vires Communis";
    public static final String VERSION = "1.0";
    public static final String MC_VERSION = "[1.12.2]";

    public static final String CLIENT = "com.samfisher39.virescommunis.proxy.ClientProxy";
    public static final String SERVER = "com.samfisher39.virescommunis.proxy.ServerProxy";
    
    //public static final GameMaster gameMaster = new GameMaster();
    
    @SidedProxy(clientSide = ViresCommunis.CLIENT, serverSide = ViresCommunis.SERVER)
    public static IProxy proxy;
 
    public static Logger logger;
 
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }
 
    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info(ViresCommunis.NAME + " salvete!");
        proxy.init(event);
    }
 
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
 
}
