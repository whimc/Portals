package edu.whimc.portals.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Portal;

public class Messager {

	public static final String prefix = "&7[&b&lPortals&7]&r ";
	private static final String rep = "{$}";

	public static void msg(CommandSender sender, Message message) {
		sender.sendMessage(color(message.toString()));
	}

	public static void msg(CommandSender sender, ReplaceMessage message, String replace) {
		sender.sendMessage(color(message.toString(replace)));
	}

	public static void msg(CommandSender sender, String str) {
		sender.sendMessage(color(str));
	}

	public static void msg(CommandSender sender, String... strings) {
		for (String str : strings) {
			msg(sender, str);
		}
	}

	public static void usage(CommandSender sender, String usage) {
		msg(sender, Message.INVALID_USAGE);
		msg(sender, ChatColor.ITALIC + "  " + usage);
	}

	public static String color(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public static void sendPortalInfo(CommandSender sender, Portal portal) {
		msg(sender, Message.LINE);
		msg(sender,
				"&bPortal: " + portal.toString(),
				"&bWorld: &f&o" + portal.getWorldName(),
				"&bPosition 1:",
				"  &bx: &f&o" + portal.getPos1().getX(),
				"  &by: &f&o" + portal.getPos1().getY(),
				"  &bz: &f&o" + portal.getPos1().getZ(),
				"&bPosition 2:",
				"  &bx: &f&o" + portal.getPos2().getX(),
				"  &by: &f&o" + portal.getPos2().getY(),
				"  &bz: &f&o" + portal.getPos2().getZ());
		Destination dest = portal.getDestination();
		msg(sender,
				"&bDestination: " + (dest == null ? "&7None" : dest.toString()));
		msg(sender, Message.LINE);
	}

	public static void sendDestinationInfo(CommandSender sender, Destination dest) {
		msg(sender, Message.LINE);
		msg(sender,
				"&bDestination: " + dest.toString(),
				"&bWorld: &f&o" + dest.getWorldName(),
				"&bLocation:",
				"  &bx: &f&o" + dest.getX(),
				"  &by: &f&o" + dest.getY(),
				"  &bz: &f&o" + dest.getZ(),
				"  &bpitch: &f&o" + dest.getPitch(),
				"  &byaw: &f&o" + dest.getYaw());
		List<Portal> portals = dest.getLinkedPortals();
		if (portals.size() == 0) {
			msg(sender, "&bPortals: &7None");
		} else {

			String list = "";
			for (int ind = 0; ind < portals.size(); ind++) {
				list += portals.get(ind).toString();
				if (ind != portals.size() -1) {
					list += "&8,&r ";
				}
			}
			msg(sender, "&bPortals: &r" + list);
		}
		msg(sender, Message.LINE);
	}

	public enum Message {
		NO_PERMISSION(prefix + ChatColor.RED + "You cannot use this command!"),
		MUST_BE_PLAYER(prefix + ChatColor.RED + "You must be a player to use this command!"),
		INVALID_USAGE(prefix + ChatColor.RED + "Invalid usage!"),
		PORTAL_NO_DESTINATION(prefix + ChatColor.DARK_GRAY + "This portal has no destination!"),
		PORTAL_DESTINATION_INVALID(prefix + ChatColor.DARK_GRAY + "This portal's destination is in an invalid world!"),
		PORTAL_TOOL_GIVEN(prefix + ChatColor.GREEN + "The portal tool has been added to your inventory!"),
		NO_PORTALS(prefix + "&7There are currently no portals!&r\n  &7(Use &8\"&7&o/portal create&8\" &7to create one)"),
		NO_DESTINATIONS(prefix + "&7There are currently no destinations!&r\n  &7(Use &8\"&7&o/destination create&8\" &7to create one)"),
		POS_BOTH_NOT_SELECTED(prefix + ChatColor.RED + "You have not selected both positions!"),
		POS_IN_DIFF_WORLDS(prefix + ChatColor.RED + "Your selections are in different worlds!"),
		NONE_RESERVED_WORD(prefix + "&cSorry, '&4" + Destination.NONE + "&c' is a reserved name!&r\n" +
				"  &7(use &8\"&7&o/destination clear&8\" &7to remove a destination)"),

		DEBUG_ENABLE(prefix + "&7Debug mode has been &aenabled&7!"),
		DEBUG_DISABLE(prefix + "&7Debug mode has been &cdisabled&7!"),

		LINE_PORTAL_LIST("&m----------------------&r &b&lPortals&r &m----------------------"),
		LINE_DESTINATION_LIST("&m-------------------&r &b&lDestinations&r &m--------------------&r"),
		LINE_COMMAND_LIST("&m-------------------->&r &7[&b&lPortals&7]&r &m<--------------------"),
		LINE("&m-----------------------------------------------------");

		private String message;

		private Message(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return message;
		}
	}

	public enum ReplaceMessage {
		PORTAL_DOES_NOT_EXIST(prefix + "&cThe portal '&4" + rep + "&c' does not exist!"),
		PORTAL_ALREADY_EXISTS(prefix + "&cThe portal '&4" + rep + "&c' already exists!"),
		PORTAL_CREATE_SUCCESS(prefix + "&aThe portal '&2" + rep + "&a' has been created!"),
		PORTAL_REMOVE_SUCCESS(prefix + "&aThe portal '&2" + rep + "&a' has been removed!"),
		PORTAL_INVALID(prefix + "&c'&4" + rep + "&c' is in a world that no longer exists!"),
		PORTAL_REFILLED(prefix + "&aThe portal '&2" + rep + "&a' has been refilled!"),
		PORTAL_DEST_CLEARED(prefix + "&aThe destination of '&2" + rep + "&a' has been removed!"),

		DESTINATION_DOES_NOT_EXIST(prefix + "&cThe destination '&4" + rep + "&c' does not exist!"),
		DESTINATION_ALREADY_EXISTS(prefix + "&cThe destination '&4" + rep + "&c' already exists!"),
		DESTINATION_CREATE_SUCCESS(prefix + "&aThe destination '&2" + rep + "&a' has been created!"),
		DESTINATION_CHANGE_SUCCESS(prefix + "&aThe destination '&2" + rep + "&a' has been set to your current location!"),
		DESTINATION_REMOVE_SUCCESS(prefix + "&aThe destination '&2" + rep + "&a' has been removed!"),
		DESTINATION_INVALID(prefix + "&c'&4" + rep + "&c' is in a world that no longer exists!"),
		DESTINATION_TELEPORTED(prefix + "&aYou have been teleported to '&2" + rep + "&a'"),

		INVALID_FILLER(prefix + "&c'&4" + rep + "&c' is an invalid filler type!&r\n  &7(Valid fillers: AIR, WATER, LAVA, PORTAL, WEB)"),
		SUGGEST_DELETE("  (&7You may want to delete it with \"&o" + rep + "&7\")");

		private String message;

		private ReplaceMessage(String message) {
			this.message = message;
		}

		public String toString(String replace) {
			return this.message.replace(rep, replace);
		}
	}
}
