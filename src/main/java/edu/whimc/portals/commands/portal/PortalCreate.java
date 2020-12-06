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

public class PortalCreate extends AbstractSubCommand {

    public PortalCreate(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Creates a permissionless portal using the selected location");
        super.arguments("name");
        super.requiresPlayer();
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Location pos1 = ToolSelectListener.leftClicks.get(player.getUniqueId());
        Location pos2 = ToolSelectListener.rightClicks.get(player.getUniqueId());

        if (pos1 == null || pos2 == null){
            Messenger.msg(sender, Message.POS_BOTH_NOT_SELECTED);
            return true;
        }

        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())){
            Messenger.msg(sender, Message.POS_IN_DIFF_WORLDS);
            return false;
        }

        String name = args[0];
        Portal portal = Portal.getPortal(name);
        if (portal != null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_ALREADY_EXISTS, portal.getName());
            return false;
        }

        Portal.createPortal(plugin, name, null, player.getWorld(), pos1.toVector(), pos2.toVector());
        Messenger.msg(sender, ReplaceMessage.PORTAL_CREATE_SUCCESS, name);

        return true;
    }

}
