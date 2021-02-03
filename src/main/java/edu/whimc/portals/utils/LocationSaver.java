package edu.whimc.portals.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import edu.whimc.portals.Main;

public final class LocationSaver {

    private MyConfig portalData;

    public LocationSaver(Main plugin) {
        this.portalData = plugin.getPortalData();
    }

    public void saveLocation(Location loc, String path) {

        if (loc == null) {
            portalData.removeKey(path);
            portalData.saveConfig();
            portalData.reloadConfig();
            return;
        }
        if (loc.getWorld() == null) {
            Main.getInstance().getLogger().severe("When saving a location, the world could not be found");
            return;
        }

        String world;
        double x, y, z;
        float yaw, pitch;
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

    public void saveVector(Vector vector, String path) {
        int x, y, z;
        x = vector.getBlockX();
        y = vector.getBlockY();
        z = vector.getBlockZ();
        portalData.set(path + ".x", x);
        portalData.set(path + ".y", y);
        portalData.set(path + ".z", z);
        portalData.saveConfig();
        portalData.reloadConfig();
    }

    public Location getLocation(String path) {
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
        } catch (Exception e) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().info("CAUTION - A location has been loaded with an invalid world!");
        }
        return new Location(world, x, y, z, yaw, pitch);
    }
}
