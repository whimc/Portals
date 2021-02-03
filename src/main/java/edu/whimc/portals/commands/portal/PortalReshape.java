package edu.whimc.portals.commands.portal;

import java.util.List;

import edu.whimc.portals.Main;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.listeners.ToolSelectListener;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a player to change the shape of a {@link Portal} using
 * predefined locations.
 *
 * @see PortalCommand
 * @see ToolSelectListener
 */
public final class PortalReshape extends AbstractSubCommand {

    public PortalReshape(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Reshape a portal to your current selection");
        super.provideArguments("portal");
        super.setRequiresPlayer(true);
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        Player player = (Player) sender;
        Location pos1 = ToolSelectListener.leftClicks.get(player.getUniqueId());
        Location pos2 = ToolSelectListener.rightClicks.get(player.getUniqueId());

        if (pos1 == null || pos2 == null){
            Messenger.msg(sender, Message.POS_BOTH_NOT_SELECTED);
            return true;
        }

        if (pos1.getWorld() == null || pos2.getWorld() == null) {
            Main.getInstance().getLogger().severe("Portal selection positions do not have valid locations");
            Messenger.msg(sender, Message.ERROR);
            return false;
        }

        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())){
            Messenger.msg(sender, Message.POS_IN_DIFF_WORLDS);
            return false;
        }

        portal.reshape(player.getWorld(), pos1.toVector(), pos2.toVector());
        Messenger.msg(sender, ReplaceMessage.PORTAL_RESHAPED, portal.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}
