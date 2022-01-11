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

/**
 * The class to store Portal data.
 */
public class Portal {

    /** The list of all portals. */
    private static List<Portal> portals = new ArrayList<>();
    /** Map of serialized locations to their respective Portal. Allows for fast lookups. */
    private static Map<String, Portal> portalData = new HashMap<>();
    /** The default material to fill the portal with. */
    private static Material defaultFiller = Material.END_GATEWAY;
    /** The set of valid portal fill materials. */
    private static final Set<Material> validFillers = new HashSet<>(Arrays.asList(
            Material.AIR,
            Material.WATER,
            Material.LAVA,
            Material.COBWEB,
            Material.END_GATEWAY));

    /** The instance of the plugin. */
    private Main plugin;
    /** The name of the portal. */
    private String name;
    /** The name of the word that the portal is in */
    private String worldName;
    /** The permission level required to use the portal. */
    private Permission permission;
    /** The first position defining the portal. */
    private Vector pos1;
    /** The second position defining the portal. */
    private Vector pos2;
    /** The destination that the portal will take you to. */
    private Destination destination;
    /** The filler materials of the portal. */
    private Material filler;

    /** If the portal is valid. */
    private boolean valid = true;

    /**
     * Creates a Portal.
     *
     * @param plugin The instance of the plugin.
     * @param name The name of the portal.
     * @param permission The permission level required to use the portal.
     * @param world The world that the portal is in.
     * @param pos1 The first position defining the portal.
     * @param pos2 The second position defining the portal.
     * @return The new Portal.
     */
    public static Portal createPortal(Main plugin, String name, String permission, World world, Vector pos1, Vector pos2) {
        return new Portal(plugin, name, permission, world.getName(), pos1, pos2, null, defaultFiller, true);
    }

    /**
     * Loads a Portal.
     *
     * @param plugin The instance of the plugin.
     * @param name The name of the portal.
     * @param permission The permission level required to use the portal.
     * @param worldName The world that the portal is in.
     * @param pos1 The first position defining the portal.
     * @param pos2 The second position defining the portal.
     * @param destination The destination that the portal will take you to.
     * @param filler The filler materials of the portal.
     * @return The loaded portal.
     */
    public static Portal loadPortal(Main plugin, String name, String permission, String worldName, Vector pos1, Vector pos2, Destination destination, Material filler) {
        return new Portal(plugin, name, permission, worldName, pos1, pos2, destination, filler, false);
    }

    /**
     * Constructs a Portal.
     *
     * @param plugin The instance of the plugin.
     * @param name The name of the portal.
     * @param permission The permission level required to use the portal.
     * @param worldName The world that the portal is in.
     * @param pos1 The first position defining the portal.
     * @param pos2 The second position defining the portal.
     * @param destination The destination that the portal will take you to.
     * @param filler The filler materials of the portal.
     * @param isNew If the portal is a newly created one.
     */
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

        if(isNew) {
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

    /** @return The list of all portals. */
    public static List<Portal> getPortals() {
        return portals;
    }

    /**
     * Tab-completes the portal name given a hint.
     *
     * @param hint A string to help narrow down the possible names.
     * @return The list of destination names that can be auto-filled.
     */
    public static List<String> getTabCompletedPortals(String hint) {
        return Portal.getPortals()
                .stream()
                .map(Portal::getName)
                .filter(v -> v.toLowerCase().startsWith(hint.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Tab-completes the permission level name given a hint.
     *
     * @param hint A string to help narrow down the possible names.
     * @return The list of permission level names that can be auto-filled.
     */
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

    /** @return The map of portal data. */
    public static Map<String, Portal> getPortalData() {
        return portalData;
    }

    /** @return The set of valid portal filler materials. */
    public static Set<Material> getValidFillers() {
        return validFillers;
    }

    /**
     * Check if the provided material is a valid portal filler.
     *
     * @param mat The material to check.
     * @return Whether or not the provided material is a valid portal filler.
     */
    public static boolean isValidFiller(Material mat) {
        return validFillers.contains(mat);
    }

    /**
     * Finds the portal with the provided name.
     *
     * @param portalName The name of the portal.
     * @return The portal with the given name.
     */
    public static Portal getPortal(String portalName) {
        for (Portal portal : portals) {
            if (portal.getName().equalsIgnoreCase(portalName)) return portal;
        }

        return null;
    }

    /**
     * Finds the portal at the provided location.
     *
     * @param loc The location of the portal.
     * @return The portal at the provided location.
     */
    public static Portal getPortal(Location loc) {
        return getPortal(loc.getBlock());
    }

    /**
     * Finds the portal with the provided block.
     *
     * @param block The block.
     * @return The portal with the provided block.
     */
    public static Portal getPortal(Block block) {
        if (block == null) return null;
        String data = getBlockDataString(block);
        return portalData.getOrDefault(data, null);
    }

    /**
     * Reshapes the portal to the new world and positions.
     *
     * @param newWorld The new world to place the portal in.
     * @param newPos1 The new first position of the portal boundary.
     * @param newPos2 The new second position of the portal boundary.
     */
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

    /** @return The filler material of the portal. */
    public Material getFiller() {
        return this.filler;
    }

    /** Adds filler to the portal. */
    public void addFiller() {
        if (!this.valid) return;

        portalForEach(block -> {
            setFiller(block);
        });
    }

    /**
     * Sets the filler of the portal to the specified block.
     *
     * @param block The block to fill the portal with.
     */
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

    /** Removes the filler from the portal. */
    public void removeFiller() {
        portalForEach(block -> {
            if (isValidFiller(block.getType())) block.setType(Material.AIR);
        });
    }

    /** @return A location that is safe for the player to be teleported to. */
    public Location getSafeTeleportLocation() {
        Block res = portalForEach(block -> {
            Block above = block.getRelative(BlockFace.UP);
            return (block.isEmpty() || block.getType() == this.filler) &&
                    (above.isEmpty() || above.getType() == this.filler);
        });
        return res == null ? null : res.getLocation().add(0.5, 0, 0.5);
    }

    /**
     * Iterates through every block in the portal and applies the given function.
     *
     * @param func The function to apply to each block.
     */
    private void portalForEach(Consumer<Block> func) {
        portalForEach(block -> {
            func.accept(block);
            return false;
        });
    }

    /**
     * Iterates through every block in the portal and applies the given task.
     *
     * @param task The task to perform on each block.
     * @return The block that the task was applied to.
     */
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

    /** Removes this portal. */
    public void remove() {
        if (this.valid) {
            removeFiller();
        }

        removePermission();
        portals.remove(this);
        portalData.values().removeIf(v -> v == this);
        plugin.getPortalData().removeKey("Portals." + this.name);
        saveConfig();
    }

    /**
     * Sets the filler of the portal to the specified material.
     *
     * @param filler The material to fill the portal with.
     */
    public void setFiller(Material filler) {
        this.removeFiller();
        this.filler = filler;
        this.addFiller();

        setConfig("filler", this.filler.toString());
        saveConfig();
    }

    /**
     * Sets the default filler material.
     *
     * @param filler The new default filler material.
     */
    public static void setDefaultFiller(Material filler) {
        defaultFiller = filler;
    }

    /**
     * Formats a String of the provided block's data in the form "name | x | y | z".
     *
     * @param block The block to take data from.
     * @return The formatted String of the block's data.
     */
    public static String getBlockDataString(Block block) {
        return block.getWorld().getName() + "|" + block.getX() + "|" + block.getY() + "|" + block.getZ();
    }

    /** @returns If the portal has a destination. */
    public boolean hasDestination() {
        return destination != null;
    }

    /** @returns The name of the portal. */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the portal.
     *
     * @param name The new name of the portal.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** @returns The world that the portal is in. */
    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    /** @returns The name of the world that the portal is in. */
    public String getWorldName() {
        return worldName;
    }

    /** @returns The first position defining the portal. */
    public Vector getPos1() {
        return pos1;
    }

    /**
     * Set the first position defining the portal.
     *
     * @param pos1 The new position.
     */
    public void setPos1(Vector pos1) {
        this.pos1 = pos1;
    }

    /** @returns The second position defining the portal. */
    public Vector getPos2() {
        return pos2;
    }

    /**
     * Set the second position defining the portal.
     *
     * @param pos2 The new position.
     */
    public void setPos2(Vector pos2) {
        this.pos2 = pos2;
    }

    /** @returns The destination of the portal. */
    public Destination getDestination() {
        return destination;
    }

    /** @returns Whether or not the portal is valid. */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * Sets the destination of the portal.
     *
     * @param destination The destination of the portal.
     */
    public void setDestination(Destination destination) {
        this.destination = destination;

        setConfig("destination", destination == null ? Destination.NONE : destination.getName());
        saveConfig();
    }

    /**
     * Check if the current player has permission to use this portal.
     *
     * @param player The player.
     * @return Whether or not the player has permission to use this portal.
     */
    public boolean playerHasPermission(Player player) {
        return this.permission == null || player.hasPermission(this.permission);
    }

    /** @return The permission level required to use this portal. */
    public Permission getPermission() {
        return this.permission;
    }

    /**
     * Formats the permission.
     *
     * @param node The permission node.
     * @return The formatted permission node path.
     */
    public static String formatPermission(String node) {
        if (node == null) return null;
        return Main.PERM_PREFIX + ".entry." + node;
    }

    /**
     * Get the raw permission node name.
     *
     * @param permission The permission.
     * @return The permission node name.
     */
    public static String getRawPermission(Permission permission) {
        String[] parts = permission.getName().split(Pattern.quote("."));
        return parts[parts.length - 1];
    }

    /** Removes the required permission level from the portal. */
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

    /**
     * Get permission level required for portal if it already exists.
     * Otherwise, register a new permission to the portal.
     *
     * @param formattedPerm The formatted permission.
     * @return The permission level (either pre-existing or newly registered).
     */
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

    /**
     * Set the permission level required to use this portal.
     *
     * @param permStr The new permission level required for this portal.
     */
    public void setPermission(String permStr) {
        if (this.permission != null) {
            removePermission();
        }

        setConfig("permission", permStr);
        saveConfig();

        this.permission = registerOrGetPermission(formatPermission(permStr));
    }

    /** @return The minimum X value of the portal boundary. */
    private int getMinX() {
        if (!this.valid) return 0;
        return Math.min(pos1.getBlockX(), pos2.getBlockX());
    }

    /** @return The minimum Y value of the portal boundary. */
    private int getMinY() {
        if (!this.valid) return 0;
        return Math.min(pos1.getBlockY(), pos2.getBlockY());
    }

    /** @return The minimum Z value of the portal boundary. */
    private int getMinZ() {
        if (!this.valid) return 0;
        return Math.min(pos1.getBlockZ(), pos2.getBlockZ());
    }

    /** @return The maximum X value of the portal boundary. */
    private int getMaxX() {
        if (!this.valid) return 0;
        return Math.max(pos1.getBlockX(), pos2.getBlockX());
    }

    /** @return The maximum Y value of the portal boundary. */
    private int getMaxY() {
        if (!this.valid) return 0;
        return Math.max(pos1.getBlockY(), pos2.getBlockY());
    }

    /** @return The maximum Z value of the portal boundary. */
    private int getMaxZ() {
        if (!this.valid) return 0;
        return Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    }

    /**
     * Checks if the passed location is within the portal boundaries.
     *
     * @param location The location to check.
     * @return Whether or not the location is within the portal boundaries.
     */
    public boolean inPortal(Location location) {
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

    /**
     * Sets a value in the config for this portal.
     *
     * @param key The key for the data to set in the configuration.
     * @param value The value of the data for the specified key.
     */
    private void setConfig(String key, Object value) {
        plugin.getPortalData().set("Portals." + this.name + "." + key, value);
    }

    /** Save the current portal data to the config. */
    private void saveConfig() {
        plugin.getPortalData().saveConfig();
        plugin.getPortalData().reloadConfig();
    }

    /** @return The portal's name and status (valid, no destination) in a formatted String. */
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
