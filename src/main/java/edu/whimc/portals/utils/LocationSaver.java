package edu.whimc.portals.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import edu.whimc.portals.Main;

public class LocationSaver {

	public static void saveLocation(Location loc, String path){
		if(loc == null){
			Main.portalData.removeKey(path);
			Main.portalData.saveConfig();
			Main.portalData.reloadConfig();
			return;
		}
		String world;
		double x,y,z;
		float yaw,pitch;
		world = loc.getWorld().getName();
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		yaw = loc.getYaw();
		pitch = loc.getPitch();
		Main.portalData.set(path + ".world", world);
		Main.portalData.set(path + ".x", x);
		Main.portalData.set(path + ".y", y);
		Main.portalData.set(path + ".z", z);
		Main.portalData.set(path + ".pitch", pitch);
		Main.portalData.set(path + ".yaw", yaw);
		Main.portalData.saveConfig();
		Main.portalData.reloadConfig();
	}

	public static void saveVector(Vector vector, String path){
		int x,y,z;
		x = vector.getBlockX();
		y = vector.getBlockY();
		z = vector.getBlockZ();
		Main.portalData.set(path + ".x", x);
		Main.portalData.set(path + ".y", y);
		Main.portalData.set(path + ".z", z);
		Main.portalData.saveConfig();
		Main.portalData.reloadConfig();
	}

	public static Location getLocation(String path){
		String worldName;
		double x,y,z;
		float yaw,pitch;
		try{
			worldName = Main.portalData.getString(path + ".world");
			x = Main.portalData.getDouble(path + ".x");
			y = Main.portalData.getDouble(path + ".y");
			z = Main.portalData.getDouble(path + ".z");
			yaw = Main.portalData.getFloat(path + ".yaw");
			pitch = Main.portalData.getFloat(path + ".pitch");
		}catch(Exception e){
			return null;
		}
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			Bukkit.getLogger().info("CAUTION - A location has been loaded with an invalid world!");
		}
		return new Location(world, x, y, z, yaw, pitch);
	}
}
