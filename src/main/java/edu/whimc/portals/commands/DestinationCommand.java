package edu.whimc.portals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Portal;
import edu.whimc.portals.utils.Messager;
import edu.whimc.portals.utils.Messager.Message;
import edu.whimc.portals.utils.Messager.ReplaceMessage;

public class DestinationCommand implements CommandExecutor{

	private final String[] subCommands = {"create", "change", "set", "sethere", "clear", "remove", "list", "info", "teleport"};

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!sender.hasPermission("portals.destination")){
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
		
		
		if (command.equalsIgnoreCase("create")) {
			if (!(sender instanceof Player)) {
				Messager.msg(sender, Message.MUST_BE_PLAYER);
				return true;
			}
			
			if (args.length < 2) {
				Messager.usage(sender, "/destination create [name]");
				return true;
			}
			
			if (args[1].equalsIgnoreCase(Destination.NONE)) {
				Messager.msg(sender, Message.NONE_RESERVED_WORD);
				return true;
			}
			
			Destination dest = Destination.getDestination(args[1]);
			if (dest != null) {
				Messager.msg(sender, ReplaceMessage.DESTINATION_ALREADY_EXISTS, dest.getName());
				return true;
			}
			
			player = (Player) sender;
			Destination.createDestination(args[1], player.getLocation());
			
			Messager.msg(sender, ReplaceMessage.DESTINATION_CREATE_SUCCESS, args[1]);
			return true;
		}
		
		
		if (command.equalsIgnoreCase("change")) {
			
			if (!(sender instanceof Player)) {
				Messager.msg(sender, Message.MUST_BE_PLAYER);
				return true;
			}
			
			if (args.length < 2) {
				Messager.usage(sender, "/destination change [destination]");
				return true;
			}
			
			Destination dest = Destination.getDestination(args[1]);
			if (dest == null) {
				Messager.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[2]);
				return true;
			}
			
			player = (Player) sender;
			dest.setLocation(player.getLocation());
			Messager.msg(sender, ReplaceMessage.DESTINATION_CHANGE_SUCCESS, dest.getName());
			return true;
		}
		
		
		if (command.equalsIgnoreCase("set")) {
			
			if (args.length < 3) {
				Messager.usage(sender, "/destination set [portal] [destination]");
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal == null) {
				Messager.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			Destination dest = Destination.getDestination(args[2]);
			if (dest == null) {
				Messager.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[2]);
				return true;
			}
			
			portal.setDestination(dest);
			Messager.msg(sender, Messager.prefix + "&aThe destination of '&2" + portal.getName() + "&a' " + 
					"has been set to '&2" + dest.getName() + "&a'!");
			return true;
		}
		
		
		if (command.equalsIgnoreCase("sethere")){
			
			if (!(sender instanceof Player)) {
				Messager.msg(sender, Message.MUST_BE_PLAYER);
				return true;
			}
			
			if (args.length < 2){
				Messager.usage(sender, "/destination sethere [portal]");
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal == null){
				Messager.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			String destName = portal.getName();
			player = (Player) sender;
			Destination dest = Destination.getDestination(destName);
			
			if (dest == null) {
				dest = Destination.createDestination(destName, player.getLocation());
			} else {
				dest.setLocation(player.getLocation());
			}
			
			
			portal.setDestination(dest);
			Messager.msg(sender, Messager.prefix + "&aThe destination of '&2" + portal.getName() + "&a' " + 
					"has been set to your current location!");
			Messager.msg(sender, "  &7(Destination named &8'&7&o" + portal.getName() + "&8'&7)");
			return true;
		}
		
		
		if (command.equalsIgnoreCase("clear")) {
			
			if (args.length < 2) {
				Messager.usage(sender, "/destination clear [portal]");
				return true;
			}
			
			Portal portal = Portal.getPortal(args[1]);
			if (portal == null) {
				Messager.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			portal.setDestination(null);
			Messager.msg(sender, ReplaceMessage.PORTAL_DEST_CLEARED, portal.getName());
			return true;
		}
		
		
		if (command.equalsIgnoreCase("remove")){
			
			if (args.length < 2){
				Messager.usage(sender, "/destination remove [name]");
				return true;
			}
			
			Destination dest = Destination.getDestination(args[1]);
			if (dest == null) {
				Messager.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			dest.remove();
			Messager.msg(sender, ReplaceMessage.DESTINATION_REMOVE_SUCCESS, dest.getName());
			return true;
		}
		
		
		if (command.equalsIgnoreCase("list")){
			
			if (Destination.getDestinations().size() == 0) {
				Messager.msg(sender, Message.NO_DESTINATIONS);
				return true;
			}
			
			Messager.msg(sender, Message.LINE_DESTINATION_LIST);
			for (Destination dest : Destination.getDestinations()) {
				Messager.msg(sender, ChatColor.ITALIC + "- " + dest.toString());
			}
			Messager.msg(sender, Message.LINE);
			
			return true;
		}
		
		
		if (command.equalsIgnoreCase("info")){
			
			if (args.length < 2){
				Messager.usage(sender, "/destination info [name]");
				return true;
			}
			
			Destination dest = Destination.getDestination(args[1]);
			if (dest == null){
				Messager.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[1]);
				return true;
			}
			
			Messager.sendDestinationInfo(sender, dest);
			return true;
		}
		

		if (command.equalsIgnoreCase("teleport")){
			
			if (!(sender instanceof Player)) {
				Messager.msg(sender, Message.MUST_BE_PLAYER);
				return true;
			}
			
			if (args.length < 2){
				Messager.usage(sender, "/destination teleport [name]");
				return true;
			}

			Destination dest = Destination.getDestination(args[1]);
			if (dest == null) {
				Messager.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[1]);
				return true;
			}

			if (!dest.isValid()) {
				Messager.msg(sender, ReplaceMessage.DESTINATION_INVALID, dest.getName());
				return true;
			}
			
			player = (Player) sender;
			dest.teleport(player);
			Messager.msg(sender, ReplaceMessage.DESTINATION_TELEPORTED, dest.getName());
			return true;
		}

		return true;
	}

	private boolean isSubCommand(String str){
		for(String subCommand:subCommands)
			if (str.equalsIgnoreCase(subCommand)) return true;
		return false;
	}

	private void sendCommands(CommandSender sender){
		Messager.msg(sender,
				Message.LINE_COMMAND_LIST.toString(),
				"",
				"&7/destination &bcreate [name] &7- &fCreates a new destination at your current location",
				"&7/destination &bset [portal] [destination] &7- &fSets the destination of a portal",
				"&7/destination &bchange [destination] &7- &fChanges the location of a destination",
				"&7/destination &bsethere [portal] &7- &fSets the destination of a portal to your current location using the name of the portal",
				"&7/destination &bclear [portal] &7- &fRemoves the destination of a portal",
				"&7/destination &bremove [name] &7- &fRemoves a destination",
				"&7/destination &blist &7- &fLists all destination",
				"&7/destination &binfo [name] &7- &fGives info on the given destination",
				"&7/destination &bteleport [name] &7- &fTeleports you to the given destination",
				"&7/portal &7- &fLists commands for portals",
				"",
				Message.LINE.toString());
	}

}
