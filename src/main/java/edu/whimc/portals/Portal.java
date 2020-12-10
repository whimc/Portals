package edu.whimc.portals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

public class Portal {

    private static List<Portal> portals = new ArrayList<>();
    private static Map<String, Portal> portalData = new HashMap<>();
    private static Material defaultFiller = Material.WATER;
    private static final Set<Material> validFillers = new HashSet<>(Arrays.asList(
            Material.AIR,
            Material.WATER,
            Material.LAVA,
            Material.COBWEB));

    private Main plugin;
    private String name;
    private String worldName;
    private Permission permission;
    private Vector pos1;
    private Vector pos2;
    private Destination destination;
    private Material filler;

    private boolean valid = true;

    public static Portal createPortal(Main plugin, String name, String permission, World world, Vector pos1, Vector pos2) {
        return new Portal(plugin, name, permission, world.getName(), pos1, pos2, null, defaultFiller, true);
    }

    public static Portal loadPortal(Main plugin, String name, String permission, String worldName, Vector pos1, Vector pos2, Destination destination, Material filler) {
        return new Portal(plugin, name, permission, worldName, pos1, pos2, destination, filler, false);
    }

    private Portal(Main plugin, String name, String permission, String worldName, Vector pos1, Vector pos2, Destination destination, Material filler, boolean isNew){
        this.plugin = plugin;
        this.name = name;
        this.worldName = worldName;
        setPermission(permission);
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.destination = destination;
        if (filler == null || !isValidFiller(filler)) {
            this.filler = defaultFiller;
        } else {
            this.filler = filler;
        }

        if(isNew){
            setConfig("world", worldName);
            plugin.getLocationSaver().saveVector(pos1, "Portals." + name + ".pos1");
            plugin.getLocationSaver().saveVector(pos2, "Portals." + name + ".pos2");
            if (this.filler != defaultFiller)
                setConfig("filler", this.filler.toString());
            setConfig("permission", permission);
            setConfig("destination", destination == null ? Destination.NONE : name);
            saveConfig();
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

    public static List<String> getTabCompletedPortals(String hint) {
        return Portal.getPortals()
                .stream()
                .map(Portal::getName)
                .filter(v -> v.toLowerCase().startsWith(hint.toLowerCase()))
                .sorted()
                .collect(Collectors.toList());
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

    public Material getFiller() {
        return this.filler;
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
        plugin.getPortalData().removeKey("Portals." + this.name);
        saveConfig();
    }

    public void setFiller(Material filler) {
        this.removeFiller();
        this.filler = filler;
        this.addFiller();

        setConfig("filler", this.filler.toString());
        saveConfig();
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

        setConfig("destination", destination == null ? Destination.NONE : destination.getName());
        saveConfig();
    }

    public boolean playerHasPermission(Player player) {
        return this.permission == null || player.hasPermission(this.permission);
    }

    public Permission getPermission() {
        return this.permission;
    }

    public static String formatPermission(String node) {
        if (node == null) return null;
        return Main.PERM_PREFIX + ".entry." + node;
    }

    public void setPermission(String permStr) {
        if (this.permission != null) {
            Bukkit.getPluginManager().removePermission(this.permission);
        }

        setConfig("permission", permStr);
        saveConfig();

        String formattedPerm = formatPermission(permStr);
        if (formattedPerm != null) {
            Permission perm = new Permission(formattedPerm);
            perm.addParent(Main.PERM_PREFIX + ".entry.*", true);
            Bukkit.getPluginManager().addPermission(perm);
            this.permission = perm;
        }
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

    private void setConfig(String key, Object value) {
        plugin.getPortalData().set("Portals." + this.name + "." + key, value);
    }

    private void saveConfig() {
        plugin.getPortalData().saveConfig();
        plugin.getPortalData().reloadConfig();
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
