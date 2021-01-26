package edu.whimc.portals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

public class Portal {

    private static List<Portal> portals = new ArrayList<>();
    private static Map<String, Portal> portalData = new HashMap<>();
    private static Material defaultFiller = Material.END_GATEWAY;
    private static final Set<Material> validFillers = new HashSet<>(Arrays.asList(
            Material.AIR,
            Material.WATER,
            Material.LAVA,
            Material.COBWEB,
            Material.END_GATEWAY));

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

        portalForEach(block -> {
            portalData.put(getBlockDataString(block), this);
        });

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
                .collect(Collectors.toList());
    }

    public static List<String> getTabCompletedPermissions(String hint) {
        return Portal.getPortals()
                .stream()
                .map(Portal::getPermission)
                .filter(v -> v != null)
                .distinct()
                .map(Portal::getRawPermission)
                .filter(v -> v.toLowerCase().startsWith(hint.toLowerCase()))
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
        return getPortal(loc.getBlock());
    }

    public static Portal getPortal(Block block) {
        if (block == null) return null;
        String data = getBlockDataString(block);
        return portalData.getOrDefault(data, null);
    }

    public void reshape(World newWorld, Vector newPos1, Vector newPos2) {
        if (this.valid) {
            removeFiller();
        }
        portalData.values().removeIf(v -> v == this);

        this.worldName = newWorld.getName();
        this.pos1 = newPos1;
        this.pos2 = newPos2;
        this.valid = true;

        portalForEach(block -> {
            setFiller(block);
            portalData.put(getBlockDataString(block), this);
        });

        setConfig("world", this.worldName);
        plugin.getLocationSaver().saveVector(newPos1, "Portals." + name + ".pos1");
        plugin.getLocationSaver().saveVector(newPos2, "Portals." + name + ".pos2");
        saveConfig();
    }

    public Material getFiller() {
        return this.filler;
    }

    public void addFiller(){
        if (!this.valid) return;

        portalForEach(block -> {
            setFiller(block);
        });
    }

    private void setFiller(Block block) {
        if (block.isEmpty()) {
            block.setType(this.filler);
            if (block.getType() == Material.END_GATEWAY) {
                EndGateway gw = (EndGateway) block.getState();
                gw.setAge(Long.MAX_VALUE);
                gw.update(true, false);
            }
        }
    }

    public void removeFiller() {
        portalForEach(block -> {
            if (isValidFiller(block.getType())) block.setType(Material.AIR);
        });
    }

    public Location getSafeTeleportLocation() {
        Block res = portalForEach(block -> {
            Block above = block.getRelative(BlockFace.UP);
            return (block.isEmpty() || block.getType() == this.filler) &&
                    (above.isEmpty() || above.getType() == this.filler);
        });
        return res == null ? null : res.getLocation().add(0.5, 0, 0.5);
    }

    private void portalForEach(Consumer<Block> func) {
        portalForEach(block -> {
            func.accept(block);
            return false;
        });
    }

    private Block portalForEach(Function<Block, Boolean> task) {
        Vector max = Vector.getMaximum(pos1, pos2);
        Vector min = Vector.getMinimum(pos1, pos2);
        World world = Bukkit.getWorld(this.worldName);
        for (int x = min.getBlockX(); x <= max.getBlockX();x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ();z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (task.apply(block)) {
                        return block;
                    }
                }
            }
        }
        return null;
    }

    public void remove(){
        if (this.valid) {
            removeFiller();
        }

        removePermission();
        portals.remove(this);
        portalData.values().removeIf(v -> v == this);
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

    public static String getRawPermission(Permission permission) {
        String[] parts = permission.getName().split(Pattern.quote("."));
        return parts[parts.length - 1];
    }

    private void removePermission() {
        if (this.permission == null) {
            return;
        }

        // Unregister the permission if this is the only portal using it
        long count = portals.stream()
                .map(Portal::getPermission)
                .filter(v -> v != null)
                .filter(v -> v.equals(this.permission))
                .count();
        if (count == 1) {
            Bukkit.getPluginManager().removePermission(this.permission);
        }
    }

    private Permission registerOrGetPermission(String formattedPerm) {
        if (formattedPerm == null) {
            return null;
        }
        Permission existing = Bukkit.getPluginManager().getPermission(formattedPerm);
        if (existing != null) {
            return existing;
        }

        Permission res = new Permission(formattedPerm);
        res.addParent(Main.PERM_PREFIX + ".entry.*", true);
        Bukkit.getPluginManager().addPermission(res);

        return res;
    }

    public void setPermission(String permStr) {
        if (this.permission != null) {
            removePermission();
        }

        setConfig("permission", permStr);
        saveConfig();

        this.permission = registerOrGetPermission(formatPermission(permStr));
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
