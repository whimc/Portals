package edu.whimc.portals.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Portal;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

public class PortalEnterListener implements Listener{

	private static Set<UUID> debugPlayers = new HashSet<>();

	public static void addDebugPlayer(Player player) {
		debugPlayers.add(player.getUniqueId());
	}

	public static void removeDebugPlayer(Player player) {
		debugPlayers.remove(player.getUniqueId());
	}

	public static boolean playerIsDebug(Player player) {
		return debugPlayers.contains(player.getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event){
		Location locTo = event.getTo();
		Location locFrom = event.getFrom();
		if (locFrom.getBlockX() == locTo.getBlockX() && locFrom.getBlockZ() == locTo.getBlockZ() && locFrom.getBlockY() == locTo.getBlockY()) return;

		Portal portal = Portal.getPortal(locTo);
		if(portal == null) return;

		Player player = event.getPlayer();

		if(!portal.hasDestination()){
			Messenger.msg(player, Message.PORTAL_NO_DESTINATION);
			return;
		}

		Destination dest = portal.getDestination();

		if (!dest.isValid()) {
			Messenger.msg(player, Message.PORTAL_DESTINATION_INVALID);
			Messenger.msg(player, ChatColor.GRAY + "  You may want to delete it with \"&o/destination remove " + dest.getName() + "\"");
			return;
		}

		if (debugPlayers.contains(player.getUniqueId())) {
			Messenger.sendPortalInfo(player, portal);
			return;
		}

		dest.teleport(player);

		makeCircleEffect(player, 20, 1, 0.5);
		makeCircleEffect(player, 20, 1, 1);
		makeCircleEffect(player, 20, 1, 1.5);
		makeCircleEffect(player, 20, 1, 2);
		player.playSound(portal.getDestination().getLocation(), "block.note.chime", 0.5f, 1f);

	}

	private void makeCircleEffect(Player player, int points, double size, double yOffSet){
		for (int i = 0; i < 360; i += 360/points) {
			double angle = (i * Math.PI / 180);
			double x = size * Math.cos(angle);
			double z = size * Math.sin(angle);
			Location loc = player.getLocation().add(0, yOffSet, 0);
			loc.add(x, 0, z);

			player.spawnParticle(Particle.FIREWORKS_SPARK, loc, 0);
		}
	}
}