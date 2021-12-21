package edu.whimc.portals.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import edu.whimc.portals.utils.Messenger;

/**
 * Listens for events where a portal tool is used.
 */
public class ToolSelectListener implements Listener {
	/** The map of left-click locations with the player's UUID as the key. */
	public static Map<UUID, Location> leftClicks = new HashMap<UUID, Location>();
	/** The map of right-click locations with the player's UUID as the key. */
	public static Map<UUID, Location> rightClicks = new HashMap<UUID, Location>();

	/**
	 * Add left and right-clicks preformed by player to corresponding map.
	 *
	 * @param event The PlayerInteractEvent.
	 */
	@EventHandler
	public void onHit(PlayerInteractEvent event) {
		// check if player has admin permissions for the plugin
		if (!event.getPlayer().hasPermission("portals.admin")) return;

		// check if player is left/right-clicking a block
		Action action = event.getAction();
		if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) return;

		// check if player is using the specified selection wand
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null || item.getType() != Material.WOODEN_SWORD) return;

		// get the player's UUID and click location
		UUID uuid = player.getUniqueId();
		Location loc = event.getClickedBlock().getLocation();

		// add the left-click to the corresponding map
		if (action == Action.LEFT_CLICK_BLOCK) {
			if (leftClicks.containsKey(uuid) && isSamePosition(leftClicks.get(uuid), loc)) return;
			leftClicks.put(uuid, loc);
			if (!rightClicks.containsKey(uuid)) rightClicks.put(uuid, null);
		}

		// add the right-click to the corresponding map
		if (action == Action.RIGHT_CLICK_BLOCK) {
			if (rightClicks.containsKey(uuid) && isSamePosition(rightClicks.get(uuid), loc)) return;
			rightClicks.put(uuid, loc);
			if (!leftClicks.containsKey(uuid)) leftClicks.put(uuid, null);
		}

		sendClickMessage(player, action, loc);
	}

	/**
	 * Sends a message to the player with information about where they clicked.
	 *
	 * @param player The player.
	 * @param action The action.
	 * @param loc The location of the click.
	 */
	private void sendClickMessage(Player player, Action action, Location loc) {
		// split the location into its components
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		String message = Messenger.prefix + "&aPosition ";
		if (action == Action.LEFT_CLICK_BLOCK) {
			message += "1";
		}
		if (action == Action.RIGHT_CLICK_BLOCK) {
			message += "2";
		}
		message += "&r &f&oselected at &a" + x + "&f, &a" + y + "&f, &a" + z;
		if (hasBothPositions(player)) {
			message += " &7(" + getVolume(leftClicks.get(player.getUniqueId()), rightClicks.get(player.getUniqueId())) + ")";
		}

		Messenger.msg(player, message);
	}

	/**
	 * Check if two locations are in the same position.
	 *
	 * @param loc1 The first location.
	 * @param loc2 The second location.
	 * @return Whether ot not the passed locations are the same.
	 */
	private boolean isSamePosition(Location loc1, Location loc2){
		if (loc1 == null || loc2 == null) return false;
		if (loc1.getBlockX() != loc2.getBlockX()) return false;
		if (loc1.getBlockY() != loc2.getBlockY()) return false;
		if (loc1.getBlockZ() != loc2.getBlockZ()) return false;
		return true;
	}

	/**
	 * Check if the player has set both left and right-click positions.
	 *
	 * @param player The player.
	 * @return Whether or not the player has set both positions.
	 */
	private boolean hasBothPositions(Player player) {
		UUID uuid = player.getUniqueId();
		return leftClicks.get(uuid) != null && rightClicks.get(uuid) != null;
	}

	/**
	 * Calculates the volume of the selected space.
	 *
	 * @param loc1 The fist location.
	 * @param loc2 The second location.
	 * @return The volume of the current selection.
	 */
	private int getVolume(Location loc1, Location loc2) {
		int x = Math.abs(loc1.getBlockX() - loc2.getBlockX()) + 1;
		int y = Math.abs(loc1.getBlockY() - loc2.getBlockY()) + 1;
		int z = Math.abs(loc1.getBlockZ() - loc2.getBlockZ()) + 1;

		return Math.abs(x * y * z);
	}
}
