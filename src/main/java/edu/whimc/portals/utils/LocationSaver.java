package edu.whimc.portals.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import edu.whimc.portals.Main;

/**
 * Handles saving and retrieving location data to and from the portal config.
 */
public class LocationSaver {
    /* The portal data configuration. */
    private MyConfig portalData;

    /**
     * Constructs a LocationSaver.
     *
     * @param plugin The instance of the plugin.
     */
    public LocationSaver(Main plugin) {
        this.portalData = plugin.getPortalData();
    }

    /**
     * Saves the location to the portal data config.
     *
     * @param loc The location to save.
     * @param path The path to the portal data in the config.
     */
    public void saveLocation(Location loc, String path){
        // ensure location exists
        if (loc == null){
            portalData.removeKey(path);
            portalData.saveConfig();
            portalData.reloadConfig();
            return;
        }

        // save the location
        String world;
        double x,y,z;
        float yaw,pitch;
        world = loc.getWorld().getName();
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        yaw = loc.getYaw();
        pitch = loc.getPitch();
        portalData.set(path + ".world", world);
        portalData.set(path + ".x", x);
        portalData.set(path + ".y", y);
        portalData.set(path + ".z", z);
        portalData.set(path + ".pitch", pitch);
        portalData.set(path + ".yaw", yaw);
        portalData.saveConfig();
        portalData.reloadConfig();
    }

    /**
     * Saves the location vector (x, y, z).
     *
     * @param vector The vector of coordinates.
     * @param path The path to the portal data in the config.
     */
    public void saveVector(Vector vector, String path){
        int x,y,z;
        x = vector.getBlockX();
        y = vector.getBlockY();
        z = vector.getBlockZ();
        portalData.set(path + ".x", x);
        portalData.set(path + ".y", y);
        portalData.set(path + ".z", z);
        portalData.saveConfig();
        portalData.reloadConfig();
    }

    /**
     * Gets the location of the specified portal.
     *
     * @param path The path to the portal data in the config.
     * @return The location of the specified portal.
     */
    public Location getLocation(String path){
        String worldName;
        double x, y, z;
        float yaw, pitch;
        try {
            worldName = portalData.getString(path + ".world");
            x = portalData.getDouble(path + ".x");
            y = portalData.getDouble(path + ".y");
            z = portalData.getDouble(path + ".z");
            yaw = portalData.getFloat(path + ".yaw");
            pitch = portalData.getFloat(path + ".pitch");
        } catch (Exception e){
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().info("CAUTION - A location has been loaded with an invalid world!");
        }
        return new Location(world, x, y, z, yaw, pitch);
    }
}
