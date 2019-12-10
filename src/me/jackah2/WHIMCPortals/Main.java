package me.jackah2.WHIMCPortals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import me.jackah2.WHIMCPortals.commands.DestinationCommand;
import me.jackah2.WHIMCPortals.commands.PortalCommand;
import me.jackah2.WHIMCPortals.listeners.PortalEnterListener;
import me.jackah2.WHIMCPortals.listeners.ToolSelectListener;
import me.jackah2.WHIMCPortals.listeners.WaterMoveListener;
import me.jackah2.WHIMCPortals.utils.LocationSaver;
import me.jackah2.WHIMCPortals.utils.MyConfig;
import me.jackah2.WHIMCPortals.utils.MyConfigManager;

public class Main extends JavaPlugin{
	public MyConfigManager manager;
	public static MyConfig portalData;
	
	@Override
	public void onEnable(){
		manager = new MyConfigManager(this);
		portalData = manager.getNewConfig("portalData.yml");
		initializeConfig();
		registerStuff();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
	@Override
	public void onDisable(){
		ToolSelectListener.leftClicks.clear();
		ToolSelectListener.rightClicks.clear();
	}
	
	private void registerStuff(){
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PortalEnterListener(), this);
		pm.registerEvents(new ToolSelectListener(), this);
		pm.registerEvents(new WaterMoveListener(), this);
		getCommand("portal").setExecutor(new PortalCommand());
		getCommand("destination").setExecutor(new DestinationCommand());
	}

	private void initializeConfig(){
		if (portalData.contains("Destinations")) {
			String path, destWorldName;
			Location destLoc;
			for (String key : portalData.getConfigurationSection("Destinations").getKeys(false)) {
				path = "Destinations." + key;
				destLoc = LocationSaver.getLocation(path);
				
				path = "Destinations." + key + ".world";
				destWorldName = portalData.getString(path);
				
				Destination.loadDestination(key, destLoc, destWorldName);
			}
		}
		
		if(portalData.contains("Portals")){
			String path, portalWorldName, fillerName;
			Destination dest;
			Vector pos1, pos2;
			Material filler;
			for(String key : portalData.getConfigurationSection("Portals").getKeys(false)){
				
				path = "Portals." + key + ".pos1";
				pos1 = new Vector(portalData.getInt(path+".x"), portalData.getInt(path+".y"), portalData.getInt(path+".z"));
				
				path = "Portals." + key + ".pos2";
				pos2 = new Vector(portalData.getInt(path+".x"), portalData.getInt(path+".y"), portalData.getInt(path+".z"));
				
				path = "Portals." + key + ".destination";
				dest = Destination.getDestination(portalData.getString(path));
				
				portalWorldName = portalData.getString("Portals." + key + ".world");
				
				fillerName = portalData.getString("Portals." + key + ".filler");
				if (fillerName == null) filler = null;
				else filler = Material.matchMaterial(fillerName);
				
				Portal.loadPortal(key, portalWorldName, pos1, pos2, dest, filler);
			}
		}
	}
	
}