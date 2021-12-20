package edu.whimc.portals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The class to store Destination data.
 */
public class Destination {
    /** Keyword for no destination. */
    public static final String NONE = "none";

    /** The instance of the plugin. */
    private Main plugin;

    /** The list of all destinations. */
    private static List<Destination> destinations = new ArrayList<>();

    /** The name of the destination. */
    private String name;
    /** The location of the destination. */
    private Location location;
    /** The name of the world that the destination is in. */
    private String worldName;

    /** If the destination is valid. */
    private boolean isValid = true;

    /**
     * Creates a Destination.
     *
     * @param plugin The instance of the plugin.
     * @param name The name of the destination.
     * @param location the location of the destination.
     * @return The new Destination.
     */
    public static Destination createDestination(Main plugin, String name, Location location) {
        return new Destination(plugin, name, location, location.getWorld().getName(), true);
    }

    /**
     * Loads a Destination.
     *
     * @param plugin The instance of the plugin.
     * @param name The name of the destination.
     * @param location The location of the destination.
     * @param worldName The name of the world that the destination is in.
     * @return The loaded destination.
     */
    public static Destination loadDestination(Main plugin, String name, Location location, String worldName) {
        return new Destination(plugin, name, location, worldName, false);
    }

    /**
     * Constructs a Destination.
     *
     * @param plugin The instance of the plugin.
     * @param name The name of the destination.
     * @param location The location of the destination.
     * @param worldName The name of the world that the destination is in.
     * @param isNew If the destination is a newly created one.
     */
    private Destination(Main plugin, String name, Location location, String worldName, boolean isNew) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;
        this.worldName = worldName;

        if (location.getWorld() == null) {
            this.isValid = false;
        }

        if (isNew) {
            plugin.getLocationSaver().saveLocation(location, "Destinations." + name);
        }

        destinations.add(this);

    }

    /** @return The list of all destinations. */
    public static List<Destination> getDestinations() {
        return destinations;
    }

    /**
     * Tab-completes the destination name given a hint.
     *
     * @param hint A string to help narrow down the possible names.
     * @return The list of destination names that can be auto-filled.
     */
    public static List<String> getTabCompletedDestinations(String hint) {
        return Destination.getDestinations()
                .stream()
                .map(Destination::getName)
                .filter(v -> v.toLowerCase().startsWith(hint.toLowerCase()))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Finds the destination with the provided name.
     *
     * @param name The name of the destination.
     * @return The destination with the given name.
     */
    public static Destination getDestination(String name) {
        for (Destination dest : destinations) {
            if (dest.getName().equalsIgnoreCase(name)) return dest;
        }

        return null;
    }

    /** @return The list of Portals linked to this Destination. */
    public List<Portal> getLinkedPortals() {
        List<Portal> linked = new ArrayList<>();
        for (Portal portal : Portal.getPortals()) {
            if (portal.getDestination() == this) {
                linked.add(portal);
            }
        }

        return linked;
    }

    /** @return The name of the Destination. */
    public String getName() {
        return name;
    }

    /** @return The location of the Destination. */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the Destination.
     *
     * @param location The location to use.
     */
    public void setLocation(Location location) {
        this.location = location;
        this.worldName = location.getWorld().getName();
        this.isValid = true;
        plugin.getLocationSaver().saveLocation(location, "Destinations." + name);
    }

    /** @return If the Destination is valid or not. */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Teleports the given player to the Destination if it's valid.
     *
     * @param player The player.
     */
    public void teleport(Player player) {
        if (!this.isValid) return;
        player.teleport(this.location);
    }

    /**
     * Remove the Destination and unlink the Portals using it.
     */
    public void remove() {
        // unlink portals using this destination
        for (Portal portal : getLinkedPortals()) {
            portal.setDestination(null);
        }

        // remove the destination
        destinations.remove(this);
        plugin.getPortalData().removeKey("Destinations." + this.name);
        plugin.getPortalData().saveConfig();
        plugin.getPortalData().reloadConfig();
    }

    /** @return The name of the world that this Destination is in. */
    public String getWorldName() {
        return worldName;
    }

    /** @return The x position of the Destination. */
    public double getX() {
        return location.getX();
    }

    /** @return The y position of the Destination. */
    public double getY() {
        return location.getY();
    }

    /** @return The z position of the Destination. */
    public double getZ() {
        return location.getZ();
    }

    /** @return The pitch of the Destination. */
    public float getPitch() {
        return location.getPitch();
    }

    /** @return  The yaw of the Destination. */
    public float getYaw() {
        return location.getYaw();
    }

    /** @return A string representation of the Destination. */
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
