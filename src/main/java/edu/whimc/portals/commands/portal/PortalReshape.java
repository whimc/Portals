package edu.whimc.portals.commands.portal;

import java.util.List;

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
 * The command to reshape a portal to the current selection.
 */
public class PortalReshape extends AbstractSubCommand {

    /**
     * Constructs a PortalReshape.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalReshape(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Reshape a portal to your current selection");
        super.arguments("portal");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Reshapes the portal to the current selection.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

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

        // reshape the portal to the current selection
        portal.reshape(player.getWorld(), pos1.toVector(), pos2.toVector());
        Messenger.msg(sender, ReplaceMessage.PORTAL_RESHAPED, portal.getName());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}
