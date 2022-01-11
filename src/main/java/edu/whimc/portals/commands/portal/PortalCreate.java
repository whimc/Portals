package edu.whimc.portals.commands.portal;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.listeners.ToolSelectListener;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to create a permissionless portal using the selected location.
 * Command: "/portal create"
 */
public class PortalCreate extends AbstractSubCommand {

    /**
     * Constructs a PortalCreate.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalCreate(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Creates a permissionless portal using the selected location");
        super.arguments("name");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Creates a new permissionless portal in the selected location.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // get the player and selected position
        Player player = (Player) sender;
        Location pos1 = ToolSelectListener.leftClicks.get(player.getUniqueId());
        Location pos2 = ToolSelectListener.rightClicks.get(player.getUniqueId());

        // notify if either position does not exist and abort command execution
        if (pos1 == null || pos2 == null){
            Messenger.msg(sender, Message.POS_BOTH_NOT_SELECTED);
            return true;
        }

        // notify if positions in different worlds and abort command execution
        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())){
            Messenger.msg(sender, Message.POS_IN_DIFF_WORLDS);
            return false;
        }

        // notify if portal already exists and abort command execution
        String name = args[0];
        Portal portal = Portal.getPortal(name);
        if (portal != null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_ALREADY_EXISTS, portal.getName());
            return false;
        }

        // create new permissionless portal at the given location
        Portal.createPortal(plugin, name, null, player.getWorld(), pos1.toVector(), pos2.toVector());
        Messenger.msg(sender, ReplaceMessage.PORTAL_CREATE_SUCCESS, name);

        return true;
    }

}
