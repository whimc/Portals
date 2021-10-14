package edu.whimc.portals.commands.portal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to purge all unused and / or invalid portals.
 * Command: "/portal purge 'invalid'|'no-destination'|'both'"
 */
public class PortalPurge extends AbstractSubCommand {

    /**
     * Construct a PortalPurge.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalPurge(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Purge unused portals");
        super.arguments("'invalid'|'no-destination'|'both'");
    }

    /**
     * {@inheritDoc}
     *
     * Purges all unused and / or invalid portals.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        String action = args[0];
        List<Portal> targets;
        ReplaceMessage msg;

        // filters targets by invalid portals
        if (action.equalsIgnoreCase("invalid")) {
            msg = ReplaceMessage.PORTAL_PURGED_INVALID;
            targets = Portal.getPortals().stream()
                    .filter(v -> !v.isValid())
                    .collect(Collectors.toList());
        }

        // filters targets by unused portals
        else if (action.equalsIgnoreCase("no-destination")) {
            msg = ReplaceMessage.PORTAL_PURGED_NO_DESTINATION;
            targets = Portal.getPortals().stream()
                    .filter(v -> v.getDestination() == null)
                    .collect(Collectors.toList());
        }

        // sets targets as all unused and invalid portals
        else if (action.equalsIgnoreCase("both")) {
            msg = ReplaceMessage.PORTAL_PURGED_BOTH;
            targets = Portal.getPortals().stream()
                    .filter(v -> !v.isValid() || v.getDestination() == null)
                    .collect(Collectors.toList());
        }

        // send the user the command's usage and abort command execution
        else {
            Messenger.usageUnknownArg(sender, action, super.getDescription(), super.getUsage());
            return true;
        }

        // delete the target portals
        targets.stream().forEachOrdered(Portal::remove);
        Messenger.msg(sender, msg, Integer.toString(targets.size()));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Arrays.asList("invalid", "no-destination", "both").stream()
                .filter(v -> v.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }

}
