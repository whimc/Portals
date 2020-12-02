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

import edu.whimc.portals.utils.Messager;

public class ToolSelectListener implements Listener{

	public static Map<UUID, Location> leftClicks = new HashMap<UUID, Location>();
	public static Map<UUID, Location> rightClicks = new HashMap<UUID, Location>();

	@EventHandler
	public void onHit(PlayerInteractEvent event){
		if(!event.getPlayer().hasPermission("portals.admin")) return;

		Action action = event.getAction();
		if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) return;

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item == null || item.getType() != Material.WOODEN_SWORD) return;

		UUID uuid = player.getUniqueId();
		Location loc = event.getClickedBlock().getLocation();

		if(action == Action.LEFT_CLICK_BLOCK){
			if(leftClicks.containsKey(uuid) && isSamePosition(leftClicks.get(uuid), loc)) return;
			leftClicks.put(uuid, loc);
			if(!rightClicks.containsKey(uuid)) rightClicks.put(uuid, null);
		}
		if(action == Action.RIGHT_CLICK_BLOCK){
			if(rightClicks.containsKey(uuid) && isSamePosition(rightClicks.get(uuid), loc)) return;
			rightClicks.put(uuid, loc);
			if(!leftClicks.containsKey(uuid)) leftClicks.put(uuid, null);
		}

		sendClickMessage(player, action, loc);
	}

	private void sendClickMessage(Player player, Action action, Location loc) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		String message = Messager.prefix + "&aPosition ";
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

		Messager.msg(player, message);
	}

	private boolean isSamePosition(Location loc1, Location loc2){
		if(loc1 == null || loc2 == null) return false;
		if(loc1.getBlockX() != loc2.getBlockX()) return false;
		if(loc1.getBlockY() != loc2.getBlockY()) return false;
		if(loc1.getBlockZ() != loc2.getBlockZ()) return false;
		return true;
	}

	private boolean hasBothPositions(Player player) {
		UUID uuid = player.getUniqueId();
		return leftClicks.get(uuid) != null && rightClicks.get(uuid) != null;
	}

	private int getVolume(Location loc1, Location loc2) {
		int x = Math.abs(loc1.getBlockX() - loc2.getBlockX()) + 1;
		int y = Math.abs(loc1.getBlockY() - loc2.getBlockY()) + 1;
		int z = Math.abs(loc1.getBlockZ() - loc2.getBlockZ()) + 1;

		return Math.abs(x * y * z);
	}
}
