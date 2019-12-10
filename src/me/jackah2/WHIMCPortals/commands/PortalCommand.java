package me.jackah2.WHIMCPortals.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.jackah2.WHIMCPortals.Main;
import me.jackah2.WHIMCPortals.Portal;
import me.jackah2.WHIMCPortals.listeners.PortalEnterListener;
import me.jackah2.WHIMCPortals.listeners.ToolSelectListener;
import me.jackah2.WHIMCPortals.utils.Messager;
import me.jackah2.WHIMCPortals.utils.Messager.Message;
import me.jackah2.WHIMCPortals.utils.Messager.ReplaceMessage;

public class PortalCommand implements CommandExecutor {

	private final String[] subCommands = {"tool", "create", "remove", "setfiller", "refill", "list", "info", "debug"};
	private final ItemStack tool = item(Material.WOOD_SWORD, ChatColor.AQUA + "Portal Tool");

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission("portals.admin")){
			Messager.msg(sender, Message.NO_PERMISSION);
			return true;
		}

		if (args.length == 0){
			sendCommands(sender);
			return true;
		}

		String command = args[0];
		if (!isSubCommand(command)){
			sendCommands(sender);
			return true;
		}

		Player player;
		if (command.equalsIgnoreCase("tool")){
			if (!(sender instanceof Player)) {
				Messager.msg(sender, Message.MUST_BE_PLAYER);
				return true;
			}
			
			player = (Player) sender;
			player.getInventory().addItem(tool);
			
			Messager.msg(sender, Message.PORTAL_TOOL_GIVEN);
		}

		if (command.equalsIgnoreCase("create")){
			if (!(sender instanceof Player)) {
				Messager.msg(sender, Message.MUST_BE_PLAYER);
				return true;
			}
			
			if (args.length < 2){
				Messager.usage(sender, "/portal create [name]");
				return true;
			}
			
			player = (Player) sender;
			Location pos1 = ToolSelectListener.leftClicks.get(player.getUniqueId());
			Location pos2 = ToolSelectListener.rightClicks.get(player.getUniqueId());
			
			if (pos1 == null || pos2 == null){
				Messager.msg(sender, Message.POS_BOTH_NOT_SELECTED);
				return true;
			}
			
			if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())){
				Messager.msg(sender, Message.POS_IN_DIFF_WORLDS);
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal != null) {
				Messager.msg(sender, ReplaceMessage.PORTAL_ALREADY_EXISTS, portal.getName());
				return true;
			}
			
			Portal.createPortal(args[1], player.getWorld(), pos1.toVector(), pos2.toVector());
			Messager.msg(sender, ReplaceMessage.PORTAL_CREATE_SUCCESS, args[1]);
		}

		if (command.equalsIgnoreCase("remove")){
			if (args.length < 2){
				Messager.usage(sender, "/portal remove [portal]");
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal == null){
				Messager.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			portal.setDestination(null);
			Main.portalData.removeKey("Portals." + portal.getName());
			portal.remove();
			Main.portalData.saveConfig();
			Main.portalData.reloadConfig();
			
			Messager.msg(sender, ReplaceMessage.PORTAL_REMOVE_SUCCESS, portal.getName());
			return true;
		}
		
		if (command.equalsIgnoreCase("setfiller")) {
			
			if (args.length < 3) {
				Messager.usage(sender, "/portal setfiller [portal] [block]");
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal == null) {
				Messager.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			if (!portal.isValid()) {
				Messager.msg(sender, ReplaceMessage.PORTAL_INVALID, portal.getName());
				Messager.msg(sender, ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
				return true;
			}
			
			Material mat = Material.matchMaterial(args[2]);
			if (mat == null || !Portal.isValidFiller(mat)) {
				Messager.msg(sender, ReplaceMessage.INVALID_FILLER, args[2]);
				return true;
			}
			
			portal.setFiller(mat);
			
			Messager.msg(sender, Messager.prefix + "&aThe filler of '&2" + portal.getName() + "&a' " + 
					"has been set to '&2" + mat.toString() + "&a'!");
			return true;
		}
		
		if (command.equalsIgnoreCase("refill")) {
			if (args.length < 2){
				Messager.usage(sender, "/portal refill [portal]");
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal == null){
				Messager.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			if (!portal.isValid()) {
				Messager.msg(sender, ReplaceMessage.PORTAL_INVALID, portal.getName());
				Messager.msg(sender, ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
				return true;
			}
			
			portal.addFiller();
			Messager.msg(sender, ReplaceMessage.PORTAL_REFILLED, portal.getName());
			return true;
		}

		if (command.equalsIgnoreCase("list")){
			if (Portal.getPortals().size() == 0) {
				Messager.msg(sender, Message.NO_PORTALS);
				return true;
			}
			
			Messager.msg(sender, Message.LINE_PORTAL_LIST);
			for (Portal portal:Portal.getPortals()) {
				Messager.msg(sender, ChatColor.ITALIC + "- " + portal.toString());
			}
			Messager.msg(sender, Message.LINE);
			
			return true;
		}

		if (command.equalsIgnoreCase("info")){
			if (args.length < 2){
				Messager.usage(sender, "/portal info [name]");
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal == null){
				Messager.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			Messager.sendPortalInfo(sender, portal);
			return true;
		}
		
		if (command.equalsIgnoreCase("debug")) {
			if (!(sender instanceof Player)) {
				Messager.msg(sender, Message.MUST_BE_PLAYER);
				return true;
			}
			
			player = (Player) sender;
			
			if (PortalEnterListener.playerIsDebug(player)) {
				PortalEnterListener.removeDebugPlayer(player);
				Messager.msg(player, Message.DEBUG_DISABLE);
			} else {
				PortalEnterListener.addDebugPlayer(player);
				Messager.msg(player, Message.DEBUG_ENABLE);
			}
		}
		
		return true;
	}

	private void sendCommands(CommandSender sender){
		Messager.msg(sender,
				Message.LINE_COMMAND_LIST.toString(),
				"",
				"&7/portal &btool &7- &fGives you the portal selector tool",
				"&7/portal &bcreate [name] &7- &fCreates a portal at the location you selected",
				"&7/portal &bremove [name] &7- &fRemoves the given portal",
				"&7/portal &bsetfiller [name] [block] &7- &fSets the filler of a portal",
				"&7/portal &brefill [name] &7- &fRefills the water of a portal",
				"&7/portal &blist &7- &fLists all portals",
				"&7/portal &binfo [name] &7- &fGives info on the given portal",
				"&7/portal &bdebug &7 - &fGives information about the portal you're entering instead of teleporting",
				"&7/destination &7- &fLists commands for destinations",
				"",
				Message.LINE.toString());
	}

	private ItemStack item(Material mat, String name){
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	private boolean isSubCommand(String str){
		for (String subCommand:subCommands)
			if (str.equalsIgnoreCase(subCommand)) return true;
		return false;
	}
}