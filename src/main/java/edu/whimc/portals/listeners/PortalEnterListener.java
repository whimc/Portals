package edu.whimc.portals.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Portal;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * Listens for events where the portal is entered.
 */
public class PortalEnterListener implements Listener {
    /** The list of UUIDs of players with debug mode enabled. */
    private static Set<UUID> debugPlayers = new HashSet<>();

    /**
     * Enable debug mode for the given player.
     *
     * @param player The player.
     */
    public static void addDebugPlayer(Player player) {
        debugPlayers.add(player.getUniqueId());
    }

    /**
     * Disable debug mode for the given player.
     *
     * @param player The player.
     */
    public static void removeDebugPlayer(Player player) {
        debugPlayers.remove(player.getUniqueId());
    }

    /**
     * Check if the passed player is in the list of players with debug mode enabled.
     *
     * @param player The player.
     * @return Whether or not the player is in the list of current player with debug mode enabled.
     */
    public static boolean playerIsDebug(Player player) {
        return debugPlayers.contains(player.getUniqueId());
    }

    /**
     * Send info / teleport if player moves into a portal.
     *
     * @param event The PlayerMoveEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        // return early if the player didn't move a full block.
        Location locTo = event.getTo();
        Location locFrom = event.getFrom();
        if (locFrom.getBlockX() == locTo.getBlockX() && locFrom.getBlockZ() == locTo.getBlockZ() && locFrom.getBlockY() == locTo.getBlockY()) return;

        Portal portal = Portal.getPortal(locTo);
        if (portal == null) return;

        Player player = event.getPlayer();

        // if the player has debug mode enabled, send them portal info instead of teleporting them
        if (debugPlayers.contains(player.getUniqueId())) {
            Messenger.sendPortalInfo(player, portal);
            return;
        }

        // notify player if they do not have sufficient permissions to use the portal and do not teleport
        if (!portal.playerHasPermission(player)) {
            Messenger.msg(player, Message.PORTAL_NO_PERMISSION);
            player.setVelocity(locFrom.toVector().subtract(locTo.toVector()).normalize().multiply(0.5));
            return;
        }

        // notify player if portal has no destination and do not teleport them
        if (!portal.hasDestination()) {
            Messenger.msg(player, Message.PORTAL_NO_DESTINATION);
            return;
        }

        Destination dest = portal.getDestination();

        // notify player if portal's destination is invalid and do not teleport them
        if (!dest.isValid()) {
            Messenger.msg(player, Message.PORTAL_DESTINATION_INVALID);
            Messenger.msg(player, ChatColor.GRAY + "  You may want to delete it with \"&o/destination remove " + dest.getName() + "\"");
            return;
        }

        // teleport player to portal destination
        dest.teleport(player);

        // make cool effects and sounds
        makeCircleEffect(player, 20, 1, 0.5);
        makeCircleEffect(player, 20, 1, 1);
        makeCircleEffect(player, 20, 1, 1.5);
        makeCircleEffect(player, 20, 1, 2);
        player.playSound(portal.getDestination().getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 1f);
    }

    /**
     * Makes a circular particle effect around the player.
     *
     * @param player The player.
     * @param points The number of points to have in the circle.
     * @param size The radius of the circle.
     * @param yOffSet The y-offset of the circle.
     */
    private void makeCircleEffect(Player player, int points, double size, double yOffSet){
        for (int i = 0; i < 360; i += 360/points) {
            double angle = i * Math.PI / 180;
            double x = size * Math.cos(angle);
            double z = size * Math.sin(angle);
            Location loc = player.getLocation().add(0, yOffSet, 0);
            loc.add(x, 0, z);

            player.spawnParticle(Particle.FIREWORKS_SPARK, loc, 0);
        }
    }
}