package edu.whimc.portals.commands.portal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.listeners.PortalEnterListener;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * The command to toggle debug mode for the player. Displays information about the portal the user is entering
 * instead of teleporting.
 * Command: "/portal debug"
 */
public class PortalDebug extends AbstractSubCommand {

    /**
     * Constructs a PortalDebug.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalDebug(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Gives information about the portal you're entering instead of teleporting");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Toggles debug mode.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (PortalEnterListener.playerIsDebug(player)) {
            PortalEnterListener.removeDebugPlayer(player);
            Messenger.msg(player, Message.DEBUG_DISABLE);
        } else {
            PortalEnterListener.addDebugPlayer(player);
            Messenger.msg(player, Message.DEBUG_ENABLE);
        }

        return true;
    }

}
