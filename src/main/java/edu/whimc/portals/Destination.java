package edu.whimc.portals;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import edu.whimc.portals.utils.LocationSaver;

public class Destination {
	
	public static final String NONE = "none";

	private static List<Destination> destinations = new ArrayList<>();
	
	private String name;
	private Location location;
	private String worldName;
	
	private boolean isValid = true;
	
	public static Destination createDestination(String name, Location location) {
		return new Destination(name, location, location.getWorld().getName(), true);
	}
	
	public static Destination loadDestination(String name, Location location, String worldName) {
		return new Destination(name, location, worldName, false);
	}
	
	private Destination(String name, Location location, String worldName, boolean isNew) {
		this.name = name;
		this.location = location;
		this.worldName = worldName;
		
		if (location.getWorld() == null) {
			this.isValid = false;
		}
		
		if (isNew) {
			LocationSaver.saveLocation(location, "Destinations." + name);
		}
		
		destinations.add(this);
		
	}
	
	public static List<Destination> getDestinations() {
		return destinations;
	}
	
	public static Destination getDestination(String name) {
		for (Destination dest : destinations) {
			if (dest.getName().equalsIgnoreCase(name)) return dest;
		}
		
		return null;
	}
	
	public List<Portal> getLinkedPortals() {
		List<Portal> linked = new ArrayList<>();
		for (Portal portal : Portal.getPortals()) {
			if (portal.getDestination() == this) {
				linked.add(portal);
			}
		}
		
		return linked;
	}
	
	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
		LocationSaver.saveLocation(location, "Destinations." + name);
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void teleport(Player player) {
		if (!this.isValid) return;
		player.teleport(this.location);
	}
	
	public void remove() {
		for (Portal portal : getLinkedPortals()) {
			portal.setDestination(null);
		}
		
		destinations.remove(this);
		Main.portalData.removeKey("Destinations." + this.name);
		Main.portalData.saveConfig();
		Main.portalData.reloadConfig();
	}
	
	public String getWorldName() {
		return worldName;
	}
	
	public double getX() {
		return location.getX();
	}
	
	public double getY() {
		return location.getY();
	}
	
	public double getZ() {
		return location.getZ();
	}
	
	public float getPitch() {
		return location.getPitch();
	}
	
	public float getYaw() {
		return location.getYaw();
	}
	
	@Override
	public String toString() {
		if (!this.isValid) {
			return "&c*&7&o" + this.name + "&7 (world does not exist)";
		}
		
		if (this.getLinkedPortals().size() == 0) {
			return "&7&o" + this.name + "&7 (no linked portals)";
		}
		
		return "&f&o" + this.getName();
		
	}
}
