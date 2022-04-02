package edu.whimc.portals;

import edu.whimc.portals.listeners.*;
import net.citizensnpcs.api.CitizensPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import edu.whimc.portals.commands.destination.DestinationCommand;
import edu.whimc.portals.commands.portal.PortalCommand;
import edu.whimc.portals.utils.LocationSaver;
import edu.whimc.portals.utils.MyConfig;
import edu.whimc.portals.utils.MyConfigManager;

/**
 * The main plugin class.
 */
public class Main extends JavaPlugin {
    /** The permissions prefix. */
    public static final String PERM_PREFIX = "whimc-portals";

    /** The instance of the ConfigManager */
    private MyConfigManager manager;
    /** The portals config. */
    private static MyConfig portalData;
    /** The instance of the location saver. */
    private LocationSaver locationSaver;

    @Override
    public void onEnable() {
        manager = new MyConfigManager(this);
        portalData = manager.getNewConfig("portalData.yml");
        locationSaver = new LocationSaver(this);

        Permission parent = new Permission(PERM_PREFIX + ".*");
        Bukkit.getPluginManager().addPermission(parent);

        Permission entry = new Permission(PERM_PREFIX + ".entry.*");
        entry.addParent(parent, true);
        Bukkit.getPluginManager().addPermission(entry);

        initializeConfig();
        registerStuff();
    }

    /** @return The portalData MyConfig. */
    public MyConfig getPortalData() {
        return portalData;
    }

    /** @return The instance of the LocationSaver. */
    public LocationSaver getLocationSaver() {
        return locationSaver;
    }

    /**
     * Registers event listeners and sub-commands.
     */
    private void registerStuff() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PortalEnterListener(), this);
        pm.registerEvents(new ToolSelectListener(), this);
        pm.registerEvents(new PortalBlockChangeListener(), this);
        pm.registerEvents(new PortalDamageListener(), this);

        // check if Citizens is enabled
        if (pm.isPluginEnabled("Citizens")) {
            pm.registerEvents(new PortalEnterCitizensListener(), this);
        }

        PortalCommand pc = new PortalCommand(this);
        getCommand("portal").setExecutor(pc);
        getCommand("portal").setTabCompleter(pc);

        DestinationCommand dc = new DestinationCommand(this);
        getCommand("destination").setExecutor(dc);
        getCommand("destination").setTabCompleter(dc);
    }

    /**
     * Loads Portals and Destinations from the config file.
     */
    private void initializeConfig() {
        if (portalData.contains("Destinations")) {
            String path, destWorldName;
            Location destLoc;
            for (String key : portalData.getConfigurationSection("Destinations").getKeys(false)) {
                path = "Destinations." + key;
                destLoc = locationSaver.getLocation(path);

                path = "Destinations." + key + ".world";
                destWorldName = portalData.getString(path);

                Destination.loadDestination(this, key, destLoc, destWorldName);
            }
        }

        if (portalData.contains("Portals")) {
            for (String key : portalData.getConfigurationSection("Portals").getKeys(false)) {

                String path = "Portals." + key + ".pos1";
                Vector pos1 = new Vector(portalData.getInt(path + ".x"), portalData.getInt(path + ".y"),
                        portalData.getInt(path + ".z"));

                path = "Portals." + key + ".pos2";
                Vector pos2 = new Vector(portalData.getInt(path + ".x"), portalData.getInt(path + ".y"),
                        portalData.getInt(path + ".z"));

                path = "Portals." + key + ".destination";
                Destination dest = Destination.getDestination(portalData.getString(path));

                String permission = portalData.getString("Portals." + key + ".permission", null);

                String portalWorldName = portalData.getString("Portals." + key + ".world");

                String fillerName = portalData.getString("Portals." + key + ".filler", "");
                Material filler = Material.matchMaterial(fillerName);

                Portal.loadPortal(this, key, permission, portalWorldName, pos1, pos2, dest, filler);
            }
        }
    }

}