package me.jackah2.WHIMCPortals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import me.jackah2.WHIMCPortals.utils.LocationSaver;

public class Portal {

	private static List<Portal> portals = new ArrayList<>();
	private static Map<String, Portal> portalData = new HashMap<>();
	private static Material defaultFiller = Material.STATIONARY_WATER;
	private static final Set<Material> validFillers = new HashSet<>(Arrays.asList(
			Material.AIR,
			Material.WATER,
			Material.STATIONARY_WATER,
			Material.LAVA,
			Material.STATIONARY_LAVA,
			Material.PORTAL,
			Material.WEB));

	private String name;
	private String worldName;
	private Vector pos1;
	private Vector pos2;
	private Destination destination;
	private Material filler;

	private boolean valid = true;

	public static Portal createPortal(String name, World world, Vector pos1, Vector pos2) {
		return new Portal(name, world.getName(), pos1, pos2, null, defaultFiller, true);
	}

	public static Portal loadPortal(String name, String worldName, Vector pos1, Vector pos2, Destination destination, Material filler) {
		return new Portal(name, worldName, pos1, pos2, destination, filler, false);
	}

	private Portal(String name, String worldName, Vector pos1, Vector pos2, Destination destination, Material filler, boolean isNew){
		this.name = name;
		this.worldName = worldName;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.destination = destination;
		if (filler == null || !isValidFiller(filler)) {
			this.filler = defaultFiller;
		} else {
			this.filler = filler;
		}

		if(isNew){
			Main.portalData.set("Portals." + name + ".world", worldName);
			LocationSaver.saveVector(pos1, "Portals." + name + ".pos1");
			LocationSaver.saveVector(pos2, "Portals." + name + ".pos2");
			if (this.filler != defaultFiller)
				Main.portalData.set("Portals." + name + ".filler", this.filler.toString());
			Main.portalData.set("Portals." + name + ".destination", destination == null ? Destination.NONE : name);
			Main.portalData.saveConfig();
			Main.portalData.reloadConfig();
		}
		portals.add(this);

		World world = Bukkit.getWorld(worldName);

		if (world == null) {
			Bukkit.getLogger().info("Error loading portal - world does not exist!");
			this.valid = false;
			return;
		}

		Vector max = Vector.getMaximum(pos1, pos2);
		Vector min = Vector.getMinimum(pos1, pos2);
		Block block;
		String data;
		for (int x = min.getBlockX(); x <= max.getBlockX();x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ();z++) {
					block = world.getBlockAt(x,y,z);
					data = getBlockDataString(block);

					portalData.put(data, this);
				}
			}
		}

		if (isNew) {
			addFiller();
		}
	}

	public static List<Portal> getPortals(){
		return portals;
	}

	public static Map<String, Portal> getPortalData() {
		return portalData;
	}

	public static Set<Material> getValidFillers() {
		return validFillers;
	}

	public static boolean isValidFiller(Material mat) {
		return validFillers.contains(mat);
	}

	public static Portal getPortal(String portalName) {
		for (Portal portal : portals) {
			if (portal.getName().equalsIgnoreCase(portalName)) return portal;
		}

		return null;
	}

	public static Portal getPortal(Location loc) {
		Block block = loc.getBlock();
		String data = getBlockDataString(block);
		return portalData.getOrDefault(data, null);
	}

	public void addFiller(){
		if (!this.valid) return;

		Vector max = Vector.getMaximum(pos1, pos2);
		Vector min = Vector.getMinimum(pos1, pos2);
		World world = Bukkit.getWorld(this.worldName);
		Block block;
		for (int x = min.getBlockX(); x <= max.getBlockX();x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ();z++) {
					block = world.getBlockAt(x,y,z);
					if(block.getType() == Material.AIR) block.setType(this.filler);
				}
			}
		}
	}

	public void removeFiller() {
		Vector max = Vector.getMaximum(pos1, pos2);
		Vector min = Vector.getMinimum(pos1, pos2);
		World world = Bukkit.getWorld(this.worldName);
		Block block;
		for (int x = min.getBlockX(); x <= max.getBlockX();x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ();z++) {
					block = world.getBlockAt(x,y,z);
					if(isValidFiller(block.getType())) block.setType(Material.AIR);
				}
			}
		}
	}

	public void remove(){
		if (this.valid) {
			removeFiller();
		}

		portals.remove(this);
		while (portalData.values().remove(this));
		Main.portalData.removeKey("Portals." + this.name);
		Main.portalData.saveConfig();
		Main.portalData.reloadConfig();


	}

	public void setFiller(Material filler) {
		this.removeFiller();
		this.filler = filler;
		this.addFiller();
		
		Main.portalData.set("Portals." + name + ".filler", this.filler.toString());
		Main.portalData.saveConfig();
		Main.portalData.reloadConfig();
	}

	public static void setDefaultFiller(Material filler) {
		defaultFiller = filler;
	}

	public static String getBlockDataString(Block block) {
		return block.getWorld().getName() + "|" + block.getX() + "|" + block.getY() + "|" + block.getZ();
	}

	public boolean hasDestination(){
		return destination != null;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public World getWorld(){
		return Bukkit.getWorld(this.worldName);
	}

	public String getWorldName(){
		return worldName;
	}

	public Vector getPos1(){
		return pos1;
	}

	public void setPos1(Vector pos1){
		this.pos1 = pos1;
	}

	public Vector getPos2(){
		return pos2;
	}

	public void setPos2(Vector pos2){
		this.pos2 = pos2;
	}

	public Destination getDestination(){
		return destination;
	}

	public boolean isValid() {
		return this.valid;
	}

	public void setDestination(Destination destination){
		this.destination = destination;

		Main.portalData.set("Portals." + name + ".destination",
				(destination == null ? Destination.NONE : destination.getName()));
		Main.portalData.saveConfig();
		Main.portalData.reloadConfig();
	}

	private int getMinX(){
		if (!this.valid) return 0;
		return Math.min(pos1.getBlockX(), pos2.getBlockX());
	}

	private int getMinY(){
		if (!this.valid) return 0;
		return Math.min(pos1.getBlockY(), pos2.getBlockY());
	}

	private int getMinZ(){
		if (!this.valid) return 0;
		return Math.min(pos1.getBlockZ(), pos2.getBlockZ());
	}

	private int getMaxX(){
		if (!this.valid) return 0;
		return Math.max(pos1.getBlockX(), pos2.getBlockX());
	}

	private int getMaxY(){
		if (!this.valid) return 0;
		return Math.max(pos1.getBlockY(), pos2.getBlockY());
	}

	private int getMaxZ(){
		if (!this.valid) return 0;
		return Math.max(pos1.getBlockZ(), pos2.getBlockZ());
	}

	public boolean inPortal(Location location){
		if (!this.valid) return false;
		if(!location.getWorld().getName().equals(this.worldName)) return false;
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		if(x < getMinX() || x > getMaxX()) return false;
		if(y < getMinY() || y > getMaxY()) return false;
		if(z < getMinZ() || z > getMaxZ()) return false;
		return true;
	}

	@Override
	public String toString() {
		if (!this.valid) {
			return "&c*&7&o" + this.name + "&7 (world does not exist)";
		}

		if (!this.hasDestination()) {
			return "&7&o" + this.name + "&7 (no destination)";
		}

		return "&f&o" + this.name;
	}

}
