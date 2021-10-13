package edu.whimc.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import edu.whimc.portals.commands.destination.DestinationCommand;
import edu.whimc.portals.commands.portal.PortalCommand;
import edu.whimc.portals.listeners.PortalBlockChangeListener;
import edu.whimc.portals.listeners.PortalDamageListener;
import edu.whimc.portals.listeners.PortalEnterListener;
import edu.whimc.portals.listeners.ToolSelectListener;
import edu.whimc.portals.utils.LocationSaver;
import edu.whimc.portals.utils.MyConfig;
import edu.whimc.portals.utils.MyConfigManager;

public class Main extends JavaPlugin {

    public static final String PERM_PREFIX = "whimc-portals";
    public static final Material TOOL_MATERIAL = Material.WOODEN_SWORD;

    private static MyConfig portalData;
    private LocationSaver locationSaver;

    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

    @Override
    public void onEnable() {
        MyConfigManager manager = new MyConfigManager(this);
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

    public MyConfig getPortalData() {
        return portalData;
    }

    public LocationSaver getLocationSaver() {
        return locationSaver;
    }

    private void registerStuff() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PortalEnterListener(), this);
        pm.registerEvents(new ToolSelectListener(), this);
        pm.registerEvents(new PortalBlockChangeListener(), this);
        pm.registerEvents(new PortalDamageListener(), this);

        PortalCommand pc = new PortalCommand();
        PluginCommand ppc = getCommand("portal");
        if (ppc == null) {
            getLogger().severe("The portal command was not properly registered.");
        } else {
            ppc.setExecutor(pc);
            ppc.setTabCompleter(pc);
        }

        DestinationCommand dc = new DestinationCommand();
        PluginCommand dpc = getCommand("destination");
        if (dpc == null) {
            getLogger().severe("The destination command was not properly registered");
        } else {
            dpc.setExecutor(dc);
            dpc.setTabCompleter(dc);
        }
    }

    private void initializeConfig() {
        if (portalData.contains("Destinations")) {
            String path, destWorldName;
            Location destLoc;
            for (String key : portalData.getConfigurationSection("Destinations").getKeys(false)) {
                path = "Destinations." + key;
                destLoc = locationSaver.getLocation(path);

                path = "Destinations." + key + ".world";
                destWorldName = portalData.getString(path);

                Destination.loadDestination(key, destLoc, destWorldName);
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

                Portal.loadPortal(key, permission, portalWorldName, pos1, pos2, dest, filler);
            }
        }
    }

}